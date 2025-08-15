package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByMessageTypeOrderByDateSentDesc(String messageType);
    List<Message> findByAdmissionNumberOrderByDateSentDesc(String admissionNumber);
    List<Message> findByAdmissionNumber(String admissionNumber);

}
