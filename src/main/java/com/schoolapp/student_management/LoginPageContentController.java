package com.schoolapp.student_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login-content")
@CrossOrigin(origins = "*")
public class LoginPageContentController {

    @Autowired
    private LoginPageContentRepository repository;

    @GetMapping
    public List<LoginPageContent> getAllContent() {
        return repository.findAll();
    }

    @PostMapping
    public LoginPageContent addContent(@RequestBody LoginPageContent content) {
        return repository.save(content);
    }

    @PutMapping("/{id}")
    public LoginPageContent updateContent(@PathVariable Long id, @RequestBody LoginPageContent content) {
        LoginPageContent existing = repository.findById(id).orElseThrow();
        existing.setTitle(content.getTitle());
        existing.setWelcomeMessage(content.getWelcomeMessage());
        existing.setBackgroundImageUrl(content.getBackgroundImageUrl());
        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
