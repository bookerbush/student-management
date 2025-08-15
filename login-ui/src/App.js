// File: App.js
import React, { useState } from "react";
import LoginForm from "./LoginForm";
import Dashboard from "./Dashboard";

function App() {
  const [loginData, setLoginData] = useState(null);

  return (
    <>
      {!loginData ? (
        <LoginForm onLoginSuccess={setLoginData} />
      ) : (
        <Dashboard userRole={loginData.role} employeeId={loginData.employeeId} admissionNo={loginData.admissionNo} />
      )}
    </>
  );
}

export default App;
