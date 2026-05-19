package ch.wiss.m223.model.dto;

public class MessageResponse {

    private Long id;
    private String message;
    private String type;
    private String content;
    private String sender;

    public MessageResponse() {
    }

    // für einfache Messages (Login, Error etc.)
    public MessageResponse(String message) {
        this.message = message;
        this.content = message;
    }

    // für GroupController / komplexe Responses
    public MessageResponse(Long id, String content, String sender) {
        this.id = id;
        this.message = content;
        this.type = sender;
        this.content = content;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.content = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.sender = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.message = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
        this.type = sender;
    }
}