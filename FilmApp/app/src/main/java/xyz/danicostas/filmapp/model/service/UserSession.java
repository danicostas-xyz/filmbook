package xyz.danicostas.filmapp.model.service;

public class UserSession {
    private static UserSession instance;
    private String userId;
    private String username;
    private String email;
    
    private UserSession() {} // Constructor privado

    public static UserSession getInstance() {
        return instance == null ? instance = new UserSession() : instance;
    }

    public void setUser(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}
