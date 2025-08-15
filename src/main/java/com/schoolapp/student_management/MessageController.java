package com.schoolapp.student_management;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*") // For quick testing; later restrict to your frontend URL
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    // Create (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createMessage(
            @RequestParam(required = false) String admissionNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String msg,
            @RequestParam String messageType, // "General" or "Particular"
            @RequestPart(name = "image", required = false) MultipartFile image
    ) {
        try {
            Message saved = service.saveMessage(admissionNumber, name, className, stream, msg, messageType, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // List all
    @GetMapping
    public List<Message> listAll() {
        return service.findAll();
    }

    // Get one
    @GetMapping("/{id}")
    public ResponseEntity<Message> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Filter by type
    @GetMapping("/type/{messageType}")
    public List<Message> byType(@PathVariable String messageType) {
        return service.findByType(messageType);
    }

    // Filter by admission number
    @GetMapping("/admission/{admissionNumber}")
    public List<Message> byAdmission(@PathVariable String admissionNumber) {
        return service.findByAdmissionNumber(admissionNumber);
    }

    // ✅ NEW: Get messages for ParentMessagesView
    @GetMapping("/parent/{admissionNumber}")
    public ResponseEntity<List<Message>> getMessagesForParent(@PathVariable String admissionNumber) {
        List<Message> messages = service.findByAdmissionNumber(admissionNumber);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    // Download the stored image (if any) for a message
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        var messageOpt = service.findById(id);
        if (messageOpt.isEmpty() || messageOpt.get().getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        var m = messageOpt.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                m.getImageContentType() != null ? m.getImageContentType() : "application/octet-stream"
        ));
        headers.setContentLength(m.getImage().length);
        headers.setContentDispositionFormData("attachment", safeFilename(m.getImageFilename()));

        return new ResponseEntity<>(m.getImage(), headers, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private String safeFilename(String name) {
        if (name == null) return "image";
        try {
            return URLEncoder.encode(name, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return "image";
        }
    }
}
