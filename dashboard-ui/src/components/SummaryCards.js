import React, { useEffect, useState } from "react";
import axios from "axios";

export const SummaryCards = () => {
  const [counts, setCounts] = useState({
    total: 0,
    boys: 0,
    girls: 0,
  });

  const fetchSummary = async () => {
    try {
      const res = await axios.get("http://localhost:8080/students/summary");
      setCounts(res.data);
    } catch (err) {
      console.error("❌ Failed to fetch student summary", err);
    }
  };

  useEffect(() => {
    fetchSummary(); // Fetch once when component mounts

    const interval = setInterval(() => {
      fetchSummary(); // Refresh every 30 seconds
    }, 30000);

    return () => clearInterval(interval); // Clean up interval
  }, []);

  const summaries = [
    { label: "TOTAL STUDENTS", value: counts.total, color: "#e53935" }, // red
    { label: "BOYS", value: counts.boys, color: "#757575" },            // grey
    { label: "GIRLS", value: counts.girls, color: "#1e88e5" },          // blue
    { label: "STAFF MEMBERS", value: 0, color: "#fb8c00" },             // orange (placeholder)
  ];

  return (
    <>
      {summaries.map((item, index) => (
        <div
          key={index}
          className="summary-card"
          style={{
            backgroundColor: item.color,
            color: "white",
          }}
        >
          <h4>{item.label}</h4>
          <h2>{item.value}</h2>
        </div>
      ))}
    </>
  );
};
