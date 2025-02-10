package xyz.danicostas.filmapp.model.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {
    private final FirebaseAuth auth;
    public AuthManager() {
        this.auth = FirebaseAuth.getInstance();
    }
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }
    public void signOut() {
        auth.signOut();
    }
}
