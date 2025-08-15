package com.schoolapp.student_management;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository repo;
    private final CustomisedSMSSender customisedSMSSender;
    private final StudentRepository studentRepository;

    public MessageService(MessageRepository repo,
                          CustomisedSMSSender customisedSMSSender,
                          StudentRepository studentRepository) {
        this.repo = repo;
        this.customisedSMSSender = customisedSMSSender;
        this.studentRepository = studentRepository;
    }

    public Message saveMessage(
            String admissionNumber,
            String name,
            String className,
            String stream,
            String msg,
            String messageType,
            MultipartFile image
    ) throws IOException {

        // 1. Save to DB (portal)
        Message m = new Message();
        m.setAdmissionNumber(isBlank(admissionNumber) ? null : admissionNumber);
        m.setName(isBlank(name) ? null : name);
        m.setClassName(isBlank(className) ? null : className);
        m.setStream(isBlank(stream) ? null : stream);
        m.setMsg(isBlank(msg) ? null : msg);
        m.setMessageType(messageType);
        m.setDateSent(LocalDateTime.now());

        if (image != null && !image.isEmpty()) {
            m.setImage(image.getBytes());
            m.setImageContentType(image.getContentType());
            m.setImageFilename(image.getOriginalFilename());
        } else {
            m.setImage(null);
            m.setImageContentType(null);
            m.setImageFilename(null);
        }

        Message saved = repo.save(m);

        // 2. Send SMS to parents (text only)
        if (!isBlank(msg)) {
            if ("General".equalsIgnoreCase(messageType)) {
                // Send to all active students
                List<Student> activeStudents = studentRepository.findByStudentStatusIgnoreCase("active");
                for (Student s : activeStudents) {
                    String phone = getPrimaryParentPhone(s);
                    if (!isBlank(phone)) {
                        String smsResponse = customisedSMSSender.sendSMS(phone, msg);
                        System.out.println("📤 SMS sent to " + phone + " → Provider Response: " + smsResponse);
                    }
                }
            } else if ("Particular".equalsIgnoreCase(messageType) && !isBlank(admissionNumber)) {
                studentRepository.findByAdmissionNumber(admissionNumber).ifPresent(s -> {
                    String phone = getPrimaryParentPhone(s);
                    if (!isBlank(phone)) {
                        String smsResponse = customisedSMSSender.sendSMS(phone, msg);
                        System.out.println("📤 SMS sent to " + phone + " → Provider Response: " + smsResponse);
                    }
                });
            }
        }

        return saved;
    }

    public List<Message> findAll() {
        return repo.findAll();
    }

    public Optional<Message> findById(Long id) {
        return repo.findById(id);
    }

    public List<Message> findByType(String type) {
        return repo.findByMessageTypeOrderByDateSentDesc(type);
    }

    public List<Message> findByAdmissionNumber(String admissionNumber) {
        return repo.findByAdmissionNumberOrderByDateSentDesc(admissionNumber);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Fetches the main parent phone number for a student:
     * Father > Mother > Guardian
     */
    private String getPrimaryParentPhone(Student s) {
        if (!isBlank(s.getFatherPhone())) {
            return s.getFatherPhone();
        }
        if (!isBlank(s.getMotherPhone())) {
            return s.getMotherPhone();
        }
        if (!isBlank(s.getGuardianPhone())) {
            return s.getGuardianPhone();
        }
        return null;
    }
}
