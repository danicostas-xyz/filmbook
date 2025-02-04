package xyz.danicostas.filmapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import xyz.danicostas.filmapp.model.entity.User;

public class DaoBBDD {
    private static final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db;

    public  DaoBBDD() {
        db = FirebaseFirestore.getInstance();
    }

    public void createUser(User user, OnCompleteListener<DocumentReference> listener) {
        db.collection(COLLECTION_NAME).add(user).addOnCompleteListener(listener);
    }

    public void getUserById(String id, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(listener);
    }
    public void getUserByUsername(String username, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION_NAME).whereEqualTo("name", username).get().addOnCompleteListener(listener);
    }


    public void getAllUsers(OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION_NAME).get().addOnCompleteListener(listener);
    }

    public void updateUser(String id, User user, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME).document(id).set(user).addOnCompleteListener(listener);
    }

    public void deleteUser(String id, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME).document(id).delete().addOnCompleteListener(listener);
    }
}
