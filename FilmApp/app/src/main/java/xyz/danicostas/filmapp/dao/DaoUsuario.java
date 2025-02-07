package xyz.danicostas.filmapp.dao;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.User;

public class DaoUsuario {
    private static final String COLLECTION_NAME = "usuarios";
    private final FirebaseFirestore db;

    public DaoUsuario() {
        db = FirebaseFirestore.getInstance();
    }

    public void createUser(String userId, User newUser, OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document(userId)
                .set(newUser)
                .addOnCompleteListener(onCompleteListener);
    }


    public void getUserById(String id, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getUserByUsername(String username, OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(listener);
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

    public void addMovieToUserList(String userId, Film pelicula, OnCompleteListener<Void> listener) {
        // Obtenemos la referencia al documento del usuario
        DocumentReference userRef = db.collection(COLLECTION_NAME).document(userId);

        // Crear un mapa con los datos de la película
        Map<String, Object> movieData = new HashMap<>();
        movieData.put("titulo", pelicula.getTitulo());
        movieData.put("calificacion", pelicula.getCalificacion());
        movieData.put("directorID", pelicula.getDirectorID());
        movieData.put("generos", pelicula.getGeneros());

        // Suponiendo que el campo 'listasPeliculas' es un array de FilmList
        userRef.update("listaPeliculas", FieldValue.arrayUnion(movieData))
                .addOnCompleteListener(listener);
    }

    public void removeMovieFromList(String userId, String listType, String movieId, OnCompleteListener<Void> listener) {
        // Si necesitas eliminar una película específica de FilmList, necesitas hacerlo dentro de la lista correspondiente
        db.collection(COLLECTION_NAME).document(userId)
                .update("listaPeliculas." + listType, FieldValue.arrayRemove(movieId))
                .addOnCompleteListener(listener);
    }

    public void addComment(String userId, String commentId, Map<String, Object> comment, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME).document(userId)
                .update("comentarios." + commentId, comment)
                .addOnCompleteListener(listener);
    }

    public void removeComment(String userId, String commentId, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME).document(userId)
                .update("comentarios." + commentId, FieldValue.delete())
                .addOnCompleteListener(listener);
    }
}
