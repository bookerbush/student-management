// StudentReportsUI.js
import React, { useState, useEffect } from "react";
import "./StudentReportsUI.css";

export default function StudentReportsUI() {
  const [activeTab, setActiveTab] = useState("assignments");
  const [assignments, setAssignments] = useState([]);
  const [assessmentData, setAssessmentData] = useState([]);
  const [loading, setLoading] = useState(false);

  // Student info from login
  const className = localStorage.getItem("studentClass");
  const stream = localStorage.getItem("studentStream");
  const admissionNo = localStorage.getItem("studentId");

  useEffect(() => {
    if (activeTab === "assignments") {
      fetchAssignments();
    } else if (activeTab === "results") {
      fetchAssessmentResults();
    }
  }, [activeTab]);

  const fetchAssignments = () => {
    setLoading(true);
    fetch(`http://localhost:8080/api/assignments?class=${className}&stream=${stream}`)
      .then((res) => res.json())
      .then((data) => {
        setAssignments(Array.isArray(data) ? data : []);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching assignments:", err);
        setLoading(false);
      });
  };

  const fetchAssessmentResults = () => {
    if (!admissionNo) return;
    setLoading(true);
    // UPDATED: now points to new student endpoint
    fetch(`http://localhost:8080/student/assessments/list?studentid=${admissionNo}`)
      .then((res) => res.json())
      .then((data) => {
        setAssessmentData(Array.isArray(data) ? data : []);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching assessments:", err);
        setLoading(false);
      });
  };

  const handleDownload = (id) => {
    fetch(`http://localhost:8080/api/assignments/download/${id}`)
      .then((res) => res.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `assignment_${id}.doc`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);
      })
      .catch((err) => console.error("Error downloading file:", err));
  };

  return (
    <div className="student-reports-container">
      <div className="student-reports-header">
        <h2>Student Portal</h2>
      </div>

      <div className="student-reports-tabs">
        <button
          onClick={() => setActiveTab("assignments")}
          className={activeTab === "assignments" ? "active" : ""}
        >
          Assignments
        </button>
        <button
          onClick={() => setActiveTab("results")}
          className={activeTab === "results" ? "active" : ""}
        >
          Assessment Results
        </button>
      </div>

      <div className="student-reports-content">
        {activeTab === "assignments" && (
          <div>
            {loading ? (
              <p>Loading assignments...</p>
            ) : assignments.length === 0 ? (
              <p>No assignments found for your class and stream.</p>
            ) : (
              <table className="assignments-table">
                <thead>
                  <tr>
                    <th>Subject</th>
                    <th>Teacher</th>
                    <th>Date</th>
                    <th>Download</th>
                  </tr>
                </thead>
                <tbody>
                  {assignments.map((a) => (
                    <tr key={a.assigno}>
                      <td>{a.subject}</td>
                      <td>{a.teacher}</td>
                      <td>{new Date(a.date).toLocaleDateString()}</td>
                      <td>
                        <button
                          className="download-btn"
                          onClick={() => handleDownload(a.assigno)}
                        >
                          ⬇ Download
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}

        {activeTab === "results" && (
          <div>
            {loading ? (
              <p>Loading assessment results...</p>
            ) : assessmentData.length === 0 ? (
              <p>No assessment records found for this student.</p>
            ) : (
              <table className="assignments-table">
                <thead>
                  <tr>
                    <th>Subject</th>
                    <th>Assessment</th>
                    <th>Strand</th>
                    <th>Sub Strand</th>
                    <th>Indicator</th>
                    <th>Rating</th>
                    <th>Comment</th>
                    <th>Term</th>
                    <th>Date Recorded</th>
                    <th>Teacher ID</th>
                  </tr>
                </thead>
                <tbody>
                  {assessmentData.map((row) => (
                    <tr key={row.id}>
                      <td>{row.subject}</td>
                      <td>{row.assess}</td>
                      <td>{row.strand}</td>
                      <td>{row.subStrand}</td>
                      <td>{row.performanceIndicator}</td>
                      <td>{row.rating}</td>
                      <td>{row.comment}</td>
                      <td>{row.term}</td>
                      <td>{row.dateRecorded}</td>
                      <td>{row.teacherId}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </div>

      <footer className="student-reports-footer">
        <p>KELVIN'S PRIMARY SCHOOL © 2025</p>
      </footer>
    </div>
  );
}
