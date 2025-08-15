// File: EmployeeList.js
import React, { useEffect, useState } from "react";
import "./EmployeeList.css";

export const EmployeeList = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/employees")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch employees");
        return res.json();
      })
      .then((data) => {
        setEmployees(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading staff report...</p>;
  if (error) return <p style={{ color: "red" }}>Error: {error}</p>;

  return (
    <div className="employee-list">
      <h2>Staff Report</h2>
      <table>
        <thead>
          <tr>
            <th>Photo</th>
            <th>Employee ID</th>
            <th>Full Name</th>
            <th>Role</th>
            <th>National ID</th>
            <th>Next of Kin</th>
            <th>Next of Kin No</th>
            <th>Salary</th>
            <th>KRA PIN</th>
            <th>SHA</th>
            <th>Telephone</th>
            <th>NSSF No</th>
          </tr>
        </thead>
        <tbody>
          {employees.length === 0 ? (
            <tr>
              <td colSpan="12" style={{ textAlign: "center" }}>
                No staff data available.
              </td>
            </tr>
          ) : (
            employees.map((emp) => (
              <tr key={emp.employeeId}>
                <td>
                  {emp.photo ? (
                    <img
                      src={`data:image/jpeg;base64,${emp.photo}`}
                      alt="Employee"
                      width="50"
                      height="50"
                      style={{ borderRadius: "50%" }}
                    />
                  ) : (
                    "No Photo"
                  )}
                </td>
                <td>{emp.employeeId}</td>
                <td>{emp.fullname}</td>
                <td>{emp.role}</td>
                <td>{emp.nationalid}</td>
                <td>{emp.nextofkin}</td>
                <td>{emp.nextofkinNo}</td>
                <td>{emp.salary}</td>
                <td>{emp.krapin}</td>
                <td>{emp.sha}</td>
                <td>{emp.telephone}</td>
                <td>{emp.nssfno}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};
