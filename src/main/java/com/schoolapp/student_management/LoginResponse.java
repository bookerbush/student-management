package com.schoolapp.student_management;

public class LoginResponse {
    private boolean success;
    private String message;
    private String role;
    private String employeeId;
    private String admissionNo;

    public LoginResponse(boolean success, String message, String role, String employeeId, String admissionNo) {
        this.success = success;
        this.message = message;
        this.role = role;
        this.employeeId = employeeId;
        this.admissionNo = admissionNo;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getRole() { return role; }
    public String getEmployeeId() { return employeeId; }
    public String getAdmissionNo() { return admissionNo; }
}
