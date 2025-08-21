package com.example.demo.Exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

public class BadRequest implements StandardResponse, Serializable {

    private static final long serialVersionUID = -685883078337947581L;

    private int code;
    private String message;
    private Object content;

    @CreationTimestamp
    private LocalDateTime timestamp;

    // All-args constructor
    public BadRequest(int code, String message, Object content, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Constructor with only content
    public BadRequest(Object content) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor without timestamp argument
    public BadRequest(int code, String message, Object content) {
        this.code = code;
        this.message = message;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // toString
    @Override
    public String toString() {
        return "BadRequest{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                ", timestamp=" + timestamp +
                '}';
    }
}
