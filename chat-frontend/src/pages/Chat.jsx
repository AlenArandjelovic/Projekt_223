import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../api/axios";

export default function Chat() {
  const { groupId } = useParams();
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (groupId) {
      loadMessages();
    }
  }, [groupId]);

  const loadMessages = async () => {
    try {
      const res = await api.get(`/api/groups/${groupId}/messages`);
      setMessages(res.data);
      setError("");
    } catch (err) {
      setError(err.response?.data || "Fehler beim Laden der Nachrichten");
      setMessages([]);
    }
  };

  const sendMessage = async () => {
    try {
      await api.post(`/api/groups/${groupId}/messages`, {
        content: text,
      });

      setText("");
      loadMessages();
      setError("");
    } catch (err) {
      setError(err.response?.data || "Fehler beim Senden der Nachricht");
    }
  };

  if (!groupId) {
    return <div>Gruppe nicht gefunden.</div>;
  }

  return (
    <div>
      <h3>Chat</h3>

      {error && (
        <div style={{ color: "red", marginBottom: "10px" }}>
          {typeof error === "object" ? JSON.stringify(error) : error}
        </div>
      )}

      <div>
        {!Array.isArray(messages) ? (
          <div>Antwort vom Server ist ungültig.</div>
        ) : messages.length === 0 ? (
          <div>Keine Nachrichten.</div>
        ) : (
          messages.map((m) => (
            <div key={m.id}>
              <b>{m.sender ?? m.type ?? "Unbekannt"}:</b> {m.content ?? m.message}
            </div>
          ))
        )}
      </div>

      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
      />

      <button onClick={sendMessage}>Send</button>
    </div>
  );
}