import { useState } from "react";
import api from "../api/axios";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  // registration state
  const [regUsername, setRegUsername] = useState("");
  const [regEmail, setRegEmail] = useState("");
  const [regPassword, setRegPassword] = useState("");
  const [regError, setRegError] = useState("");
  const [regSuccess, setRegSuccess] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/api/auth/login", {
        username,
        password,
      });

      localStorage.setItem("token", res.data.token);
      setError("");

      window.location.href = "/groups";
    } catch (err) {
      console.error("Login failed:", err);
      setError(err.response?.data?.message || err.response?.data || "Login failed");
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setRegError("");
    setRegSuccess("");

    if (!regUsername.trim() || !regEmail.trim() || !regPassword.trim()) {
      setRegError("Please fill all fields");
      return;
    }

    try {
      await api.post("/api/auth/register", {
        username: regUsername,
        email: regEmail,
        password: regPassword,
      });

      setRegSuccess("Registration successful. You can now log in.");
      setRegUsername("");
      setRegEmail("");
      setRegPassword("");
    } catch (err) {
      console.error("Registration failed:", err);
      setRegError(err.response?.data?.message || err.response?.data || "Registration failed");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-section">
        <h2>Login</h2>
        <form onSubmit={handleLogin}>
          <input
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          {error && <div className="error">{error}</div>}

          <button type="submit">Login</button>
        </form>
      </div>

      <div className="auth-section">
        <h2>Register</h2>
        <form onSubmit={handleRegister}>
          <input
            placeholder="Username"
            value={regUsername}
            onChange={(e) => setRegUsername(e.target.value)}
          />

          <input
            placeholder="Email"
            value={regEmail}
            onChange={(e) => setRegEmail(e.target.value)}
          />

          <input
            type="password"
            placeholder="Password"
            value={regPassword}
            onChange={(e) => setRegPassword(e.target.value)}
          />

          {regError && <div className="error">{regError}</div>}
          {regSuccess && <div className="success">{regSuccess}</div>}

          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  );
}