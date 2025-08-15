import React, { useState } from "react";
import "./MessageForm.css";

const MessageForm = () => {
  const [formData, setFormData] = useState({
    admissionNumber: "",
    name: "",
    className: "",
    stream: "",
    msg: "",
    messageType: "General",
    image: null,
  });

  const [status, setStatus] = useState("");
  const [messageCount, setMessageCount] = useState(0); // ✅ Track total messages sent

  // Fetch student details from backend
  const fetchStudentDetails = async (admissionNumber) => {
    if (!admissionNumber.trim()) return;

    try {
      const res = await fetch(
        `http://localhost:8080/students/by-adm/${admissionNumber}`
      );

      if (res.ok) {
        const student = await res.json();
        setFormData((prev) => ({
          ...prev,
          name: `${student.firstName || ""} ${student.lastName || ""}`.trim(),
          className: student.classEnrolled || "",
          stream: student.stream || "",
        }));
        setStatus(""); // clear any old error
      } else {
        setStatus("⚠️ No student found");
        setFormData((prev) => ({
          ...prev,
          name: "",
          className: "",
          stream: "",
        }));
      }
    } catch (err) {
      console.error("Error fetching student:", err);
      setStatus("❌ Error fetching student");
    }
  };

  // Handle typing and file input
  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image") {
      setFormData((prev) => ({ ...prev, image: files[0] }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  // Only trigger search when pressing Enter on admission number field
  const handleAdmissionKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault(); // stop form submit
      fetchStudentDetails(formData.admissionNumber);
    }
  };

  // Submit message
  const handleSubmit = async (e) => {
    e.preventDefault();

    // 1️⃣ Validation for "Particular"
    if (formData.messageType === "Particular") {
      if (
        !formData.admissionNumber.trim() ||
        !formData.name.trim() ||
        !formData.className.trim() ||
        !formData.stream.trim()
      ) {
        alert(
          "Kindly enter admission number for student you wish the parents to receive the message"
        );
        document.querySelector("input[name='admissionNumber']").focus();
        return; // stop sending
      }
    }

    // 2️⃣ Check for "General" but fields have data
    if (formData.messageType === "General") {
      if (
        formData.admissionNumber.trim() ||
        formData.name.trim() ||
        formData.className.trim() ||
        formData.stream.trim()
      ) {
        const proceed = window.confirm(
          "You are about to send a general message, but student details are filled. Are you sure?"
        );
        if (!proceed) {
          alert(
            "If your intention was to send a general message, then clear these fields or choose the 'Particular' option."
          );
          return; // stop sending
        }
      }
    }

    setStatus("Sending...");

    try {
      const data = new FormData();
      Object.keys(formData).forEach((key) => {
        if (formData[key] !== null) {
          data.append(key, formData[key]);
        }
      });

      const res = await fetch("http://localhost:8080/messages", {
        method: "POST",
        body: data,
      });

      if (res.ok) {
        // Increment counter and show simple success
        setMessageCount((prev) => prev + 1);
        setStatus(
          `✅ Message sent successfully! (Total sent: ${messageCount + 1})`
        );

        // Reset form
        setFormData({
          admissionNumber: "",
          name: "",
          className: "",
          stream: "",
          msg: "",
          messageType: "General",
          image: null,
        });
        document.getElementById("image").value = null;
      } else {
        setStatus("❌ Failed to send message");
      }
    } catch (err) {
      console.error(err);
      setStatus("⚠️ Error connecting to server");
    }
  };

  return (
    <div className="message-form-container">
      <h2>Send Message</h2>
      <form onSubmit={handleSubmit} className="message-form">
        <input
          type="text"
          name="admissionNumber"
          placeholder="Admission Number"
          value={formData.admissionNumber}
          onChange={handleChange}
          onKeyDown={handleAdmissionKeyDown}
        />
        <input
          type="text"
          name="name"
          placeholder="Student Name"
          value={formData.name}
          readOnly
        />
        <input
          type="text"
          name="className"
          placeholder="Class"
          value={formData.className}
          readOnly
        />
        <input
          type="text"
          name="stream"
          placeholder="Stream"
          value={formData.stream}
          readOnly
        />

        <textarea
          name="msg"
          placeholder="Type your message here..."
          value={formData.msg}
          onChange={handleChange}
        ></textarea>

        <select
          name="messageType"
          value={formData.messageType}
          onChange={handleChange}
        >
          <option value="General">General</option>
          <option value="Particular">Particular</option>
        </select>

        <input
          type="file"
          id="image"
          name="image"
          accept="image/*"
          onChange={handleChange}
        />

        <button type="submit">Send</button>
      </form>

      {status && <p className="status">{status}</p>}
    </div>
  );
};

export default MessageForm;
