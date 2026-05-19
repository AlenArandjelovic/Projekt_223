import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

export default function Groups() {
  const [groups, setGroups] = useState([]);
  const [myMemberships, setMyMemberships] = useState([]);
  const [name, setName] = useState("");

  useEffect(() => {
    loadGroups();
  }, []);

  const loadGroups = async () => {
    const [allRes, myRes] = await Promise.all([
      api.get("/api/groups"),
      api.get("/api/groups/my-memberships"),
    ]);

    setGroups(allRes.data);
    setMyMemberships(myRes.data);
  };

  const createGroup = async () => {
    if (!name.trim()) return;

    await api.post("/api/groups", { name });
    setName("");
    loadGroups();
  };

  const joinGroup = async (id) => {
    try {
      await api.post(`/api/groups/${id}/join`);
      loadGroups();
    } catch (err) {
      console.log(err.response?.data);
    }
  };

  const deleteGroup = async (id) => {
    try {
      await api.delete(`/api/groups/${id}`);
      loadGroups();
    } catch (err) {
      console.log(err.response?.data);
    }
  };

  const isMember = (groupId) =>
    myMemberships.some((m) => m.group?.id === groupId);

  const isOwner = (groupId) =>
    myMemberships.some((m) => m.group?.id === groupId && m.role === "OWNER");

  const myGroups = myMemberships.map((m) => m.group);

  return (
    <div className="page page-groups">
      <div className="groups-header">
        <h2>Groups</h2>
      </div>

      <div className="group-row">
        <input
          placeholder="Group name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button className="button" onClick={createGroup}>
          Create
        </button>
      </div>

      <div className="section-card">
        <h3>All Groups</h3>
        <ul className="group-list">
          {groups.map((g) => (
            <li key={g.id} className="group-item">
              <span className="group-name">{g.name}</span>
              <div className="group-actions">
                {!isMember(g.id) ? (
                  <button className="button" onClick={() => joinGroup(g.id)}>
                    Join
                  </button>
                ) : (
                  <>
                    <Link className="button" to={`/groups/${g.id}`}>
                      Open
                    </Link>
                    {isOwner(g.id) && (
                      <button className="button" onClick={() => deleteGroup(g.id)}>
                        Delete
                      </button>
                    )}
                  </>
                )}
              </div>
            </li>
          ))}
        </ul>
      </div>

      <div className="section-card">
        <h3>My Groups</h3>
        <ul className="group-list">
          {myGroups.map((g) => (
            <li key={g.id} className="group-item">
              <span className="group-name">{g.name}</span>
              <div className="group-actions">
                <Link className="button" to={`/groups/${g.id}`}>
                  Open
                </Link>
                {isOwner(g.id) && (
                  <button className="button" onClick={() => deleteGroup(g.id)}>
                    Delete
                  </button>
                )}
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
