package com.schoolapp.student_management;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "smstable")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nullable fields
    @Column(name = "admission_number")
    private String admissionNumber;

    private String name;

    @Column(name = "class") // DB column will be 'class'
    private String className;

    private String stream;

    @Column(columnDefinition = "TEXT")
    private String msg;

    // Store image as bytes (optional)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image; // nullable

    private String imageContentType; // e.g. "image/png"
    private String imageFilename;    // original filename

    @Column(name = "message_type")
    private String messageType; // "General" or "Particular"

    @Column(name = "date_sent")
    private LocalDateTime dateSent;  // when saved

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getStream() { return stream; }
    public void setStream(String stream) { this.stream = stream; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public String getImageContentType() { return imageContentType; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }

    public LocalDateTime getDateSent() { return dateSent; }
    public void setDateSent(LocalDateTime dateSent) { this.dateSent = dateSent; }
}
