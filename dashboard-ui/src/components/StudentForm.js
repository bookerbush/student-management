import React, { useState, useEffect } from 'react';
import './StudentForm.css';

const StudentForm = () => {
  const [formData, setFormData] = useState({
    // Basic Info
    studentId: '', firstName: '', middleName: '', lastName: '',
    gender: '', dateOfBirth: '', placeOfBirth: '', nationality: 'Kenyan',

    // Parent/Guardian
    fatherName: '', fatherPhone: '', motherName: '', motherPhone: '',
    guardianName: '', guardianPhone: '', relationshipToStudent: '',
    parentEmail: '', parentAddress: '',

    // Enrollment
    admissionNumber: '', dateOfAdmission: '', classEnrolled: '',
    stream: '', admissionType: '', studentType: '',

    // Boarding
    boardingStatus: false, dormitory: '', houseParent: '',

    // Health
    medicalConditions: '', bloodGroup: '', emergencyContactName: '',
    emergencyContactPhone: '', emergencyContactRelation: '',

    // Other
    religion: '', disabilityStatus: false, disabilityDescription: '',
    passportPhoto: '', remarks: '', studentStatus: '',
  });

  const [imagePreview, setImagePreview] = useState(null);
  const [message, setMessage] = useState('');

  // ✅ FIXED: Auto-generate Student ID and Admission Number with "0001", "0002" formatting
useEffect(() => {
  const getNextStudentId = async () => {
    try {
      const response = await fetch("http://localhost:8080/students/latest-id");
      const latestId = await response.json();
      const nextId = latestId ? latestId + 1 : 1;
      const formatted = String(nextId).padStart(4, '0');
      setFormData(prev => ({
        ...prev,
        studentId: formatted,
        admissionNumber: formatted
      }));
    } catch (error) {
      console.error("Failed to fetch latest ID", error);
    }
  };

  getNextStudentId();
}, []);


  const handleChange = (e) => {
    const { name, value, type, checked, files } = e.target;
    if (type === 'file') {
      const file = files[0];
      setFormData(prev => ({ ...prev, passportPhoto: file }));
      const reader = new FileReader();
      reader.onloadend = () => setImagePreview(reader.result);
      reader.readAsDataURL(file);
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: type === 'checkbox' ? checked : value
      }));
    }
  };

  const handleReset = () => {
    window.location.reload();
  };

const handleSubmit = (e) => {
  e.preventDefault();

  // Remove the actual File from JSON stringified data
  const dataToSend = { ...formData };
  delete dataToSend.passportPhoto;

  const formDataToSend = new FormData();
  formDataToSend.append("studentData", JSON.stringify(dataToSend));
  if (formData.passportPhoto) {
    formDataToSend.append("passportPhoto", formData.passportPhoto);
  }

  fetch("http://localhost:8080/students/upload", {
    method: "POST",
    body: formDataToSend
  })
    .then(res => {
      if (res.ok) {
        setMessage("✅ Student saved successfully!");

        // Fetch next auto-generated ID
        fetch("http://localhost:8080/students/latest-id")
          .then(res => res.json())
          .then(data => {
            const nextNumber = String(data + 1).padStart(4, '0');
            setFormData(prev => ({
              ...prev,
              studentId: nextNumber,
              admissionNumber: nextNumber,
              // Clear the rest of the form except ID fields
              firstName: '', middleName: '', lastName: '',
              gender: '', dateOfBirth: '', placeOfBirth: '', nationality: 'Kenyan',
              fatherName: '', fatherPhone: '', motherName: '', motherPhone: '',
              guardianName: '', guardianPhone: '', relationshipToStudent: '',
              parentEmail: '', parentAddress: '',
              dateOfAdmission: '', classEnrolled: '',
              stream: '', admissionType: '', studentType: '',
              boardingStatus: false, dormitory: '', houseParent: '',
              medicalConditions: '', bloodGroup: '',
              emergencyContactName: '', emergencyContactPhone: '', emergencyContactRelation: '',
              religion: '', disabilityStatus: false, disabilityDescription: '',
              passportPhoto: '', remarks: '', studentStatus: ''
            }));
            setImagePreview(null);
          });
      } else {
        throw new Error("Failed to save student");
      }
    })
    .catch((error) => {
      console.error("❌ Submission error:", error);
      setMessage("❌ An error occurred while saving the student.");
    });
};



  const renderInput = (label, name, type = 'text', options = []) => (
    <div className="form-group" key={name}>
      <label>{label}</label>
      {type === 'select' ? (
        <select name={name} value={formData[name]} onChange={handleChange}>
          {options.map(opt => (
            <option key={opt} value={opt}>{opt}</option>
          ))}
        </select>
      ) : type === 'checkbox' ? (
        <input type="checkbox" name={name} checked={formData[name]} onChange={handleChange} />
      ) : type === 'file' ? (
        <input type="file" name={name} onChange={handleChange} />
      ) : (
        <input type={type} name={name} value={formData[name]} onChange={handleChange} />
      )}
    </div>
  );

  return (
    <div className="form-container">
      <form onSubmit={handleSubmit}>
        <div className="row">
          <div className="section">
            <h3>Basic Information</h3>
            {renderInput("Student ID", "studentId")}
            {renderInput("First Name", "firstName")}
            {renderInput("Middle Name", "middleName")}
            {renderInput("Last Name", "lastName")}
            {renderInput("Gender", "gender", "select", ["Male", "Female"])}
            {renderInput("Date of Birth", "dateOfBirth", "date")}
            {renderInput("Place of Birth", "placeOfBirth")}
            {renderInput("Nationality", "nationality", "select", ["Kenyan", "Ugandan", "Tanzanian", "Rwandese", "Burundian", "South Sudanese", "Somali", "Congolese", "Ethiopian", "Eritrean", "Other"])}
          </div>

          <div className="section">
            <h3>Parent / Guardian Info</h3>
            {renderInput("Father Name", "fatherName")}
            {renderInput("Father Phone", "fatherPhone")}
            {renderInput("Mother Name", "motherName")}
            {renderInput("Mother Phone", "motherPhone")}
            {renderInput("Guardian Name", "guardianName")}
            {renderInput("Guardian Phone", "guardianPhone")}
            {renderInput("Relationship to Student", "relationshipToStudent", "select", ["Brother", "Sister", "Uncle", "Grandpa", "Grandmum", "Aunty"])}
            {renderInput("Parent Email", "parentEmail", "email")}
            {renderInput("Parent Address", "parentAddress")}
          </div>

          <div className="section">
            <h3>Enrollment Info</h3>
            {renderInput("Admission Number", "admissionNumber")}
            {renderInput("Date of Admission", "dateOfAdmission", "date")}
            {renderInput("Class Enrolled", "classEnrolled")}
            {renderInput("Stream", "stream")}
            {renderInput("Admission Type", "admissionType")}
            {renderInput("Student Type", "studentType")}
            <div className="passport-preview">
              {imagePreview && <img src={imagePreview} alt="Preview" />}
            </div>
          </div>
        </div>

        <div className="row">
          <div className="section">
            <h3>Boarding Info</h3>
            {renderInput("Boarding Status", "boardingStatus", "checkbox")}
            {renderInput("Dormitory", "dormitory")}
            {renderInput("House Parent", "houseParent")}
          </div>

          <div className="section">
            <h3>Health Info</h3>
            {renderInput("Medical Conditions", "medicalConditions")}
            {renderInput("Blood Group", "bloodGroup", "select", ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"])}
            {renderInput("Emergency Contact Name", "emergencyContactName")}
            {renderInput("Emergency Phone", "emergencyContactPhone")}
            {renderInput("Emergency Relation", "emergencyContactRelation")}
          </div>

          <div className="section">
            <h3>Other Info</h3>
            {renderInput("Religion", "religion", "select", ["Christian", "Muslim", "Hindu", "Other"])}
            {renderInput("Disability Status", "disabilityStatus", "checkbox")}
            {renderInput("Disability Description", "disabilityDescription")}
            {renderInput("Passport Photo", "passportPhoto", "file")}
            {renderInput("Remarks", "remarks")}
            {renderInput("Student Status", "studentStatus")}
          </div>
        </div>

        <div className="form-actions">
          <button type="submit">Submit</button>
          <button type="button" onClick={handleReset}>Reset</button>
        </div>

        {message && <p className="message">{message}</p>}
      </form>
    </div>
  );
};

export default StudentForm;
