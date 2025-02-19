package xyz.danicostas.filmapp.model.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserSession {

    private String userId;
    private MutableLiveData<String> username = new MutableLiveData<>();
    private String name;
    private String email;
    
    private UserSession() {}
    private static UserSession instance;
    public static UserSession getInstance() {
        return instance == null ? instance = new UserSession() : instance;
    }

    public void setUser(String name, String userId, String email) {
        this.name = name;
        this.userId = userId;
        this.email = email;
    }

    public String getName() {  return name; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public LiveData<String> getUsernameLiveData() {
        return username;
    }

    public void setUsername(String name) {
        username.setValue(name);
    }

    public void clearUserData() {
        this.name = null;
        this.userId = null;
        this.username.setValue(null);
        this.email = null;
    }

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
