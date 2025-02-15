package xyz.danicostas.filmapp.model.service;

public class UserSession {

    private String userId;
    private String username;
    private String name;
    private String email;
    
    private UserSession() {}
    private static UserSession instance;
    public static UserSession getInstance() {
        return instance == null ? instance = new UserSession() : instance;
    }

    public void setUser(String name, String userId, String username, String email) {
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getName() {  return name; }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "UserSession{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
