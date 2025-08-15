// File: LoginForm.js
import React, { useState } from "react";
import "./LoginForm.css";

function LoginForm({ onLoginSuccess }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();

      if (data.success) {
        onLoginSuccess({
          role: data.role,
          admissionNo: data.admissionNo,
          employeeId: data.employeeId,
          username: data.username,
        });
      } else {
        setErrorMessage(data.message || "Login failed.");
      }
    } catch (err) {
      console.error("Login error:", err);
      setErrorMessage("Network or server error.");
    }
  };

  return (
    <div className="login-form-container">
      <h2 className="login-title">System Login</h2>
      <form onSubmit={handleSubmit} className="login-form">
        <label>Username:</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />

        <label>Password:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        {errorMessage && <div className="login-error">{errorMessage}</div>}

        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default LoginForm;
