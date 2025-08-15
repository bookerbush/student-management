package com.schoolapp.student_management;

import jakarta.persistence.*;

@Entity
public class LoginPageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String welcomeMessage;
    private String backgroundImageUrl;

    public LoginPageContent() {}

    public LoginPageContent(String title, String welcomeMessage, String backgroundImageUrl) {
        this.title = title;
        this.welcomeMessage = welcomeMessage;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getWelcomeMessage() { return welcomeMessage; }
    public String getBackgroundImageUrl() { return backgroundImageUrl; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setWelcomeMessage(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }
    public void setBackgroundImageUrl(String backgroundImageUrl) { this.backgroundImageUrl = backgroundImageUrl; }
}
