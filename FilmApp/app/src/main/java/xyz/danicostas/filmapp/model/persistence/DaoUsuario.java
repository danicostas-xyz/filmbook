package xyz.danicostas.filmapp.model.persistence;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.User;

public class DaoUsuario {
    private static DaoUsuario instance;
    private static final String COLLECTION_NAME = "usuarios";
    private final FirebaseFirestore db;

    private DaoUsuario() {
        db = FirebaseFirestore.getInstance();
    }

    public static DaoUsuario getInstance() {
        return instance == null ? instance = new DaoUsuario() : instance;
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Convertir el documento a un objeto User
                            User user = document.toObject(User.class);
                            if (user != null) {
                                // Si la conversión fue exitosa, notificamos al listener
                                listener.onComplete(task); // Enviamos la tarea con el resultado
                            } else {
                                // Si no pudimos convertir a User, notificamos el error
                                listener.onComplete(task);
                            }
                        } else {
                            // Si el documento no existe, notificamos el error
                            listener.onComplete(task);
                        }
                    } else {
                        // Si la tarea no fue exitosa, notificamos el error
                        listener.onComplete(task);
                    }
                });
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


    public void addMovieToUserList(String userId, Film film, OnCompleteListener<DocumentReference> callback) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("El userId no puede ser nulo o vacío.");
        }
        if (film == null) {
            throw new IllegalArgumentException("El objeto film no puede ser nulo.");
        }

        db.collection("users")
                .document(userId)
                .collection("films")
                .add(film)
                .addOnCompleteListener(task -> {
                    if (callback != null) {
                        // Aquí invocamos el callback con el resultado de la operación
                        callback.onComplete(task);
                    }
                })
                .addOnFailureListener(e -> {
                    // Para depuración, puedes registrar errores aquí
                    Log.e("FirestoreError", "Error al añadir película", e);
                });
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

    public List<FilmList> getUserFilmLists(String userId) {
        // TODO
        List<FilmList> filmLists = new ArrayList<>();
        db.collection(COLLECTION_NAME)
                .document(userId) // Accede al documento del usuario específico
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class); // Convierte el documento a tu clase User
                        if (user != null && user.getListasDeListas() != null) {
                            List<FilmList> lista = user.getListasDeListas(); // Obtiene la lista de películas
                            for (FilmList filmList : lista) {
                                Log.d("FirestoreData", "Película: " + filmList.toString());
                                filmLists.add(filmList);
                            }
                        } else {
                            Log.d("FirestoreData", "El usuario no tiene listas.");
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Error al cargar las listas", e));

//        List<Film> listaPeliculas = Arrays.asList(
//                new Film(),new Film(),new Film(),new Film(), new Film(), new Film(),new Film(), new Film(), new Film(), new Film(), new Film(), new Film(), new Film(),new Film(), new Film()
//        );
//        List<FilmList> listaDeListas = Arrays.asList(
//                new FilmList(), new FilmList(),new FilmList(),new FilmList(),new FilmList(),new FilmList()
//        );
//
//        listaDeListas.get(0).setContent(listaPeliculas);
//        listaDeListas.get(0).setListName("Favoritas");
//        listaDeListas.get(1).setContent(listaPeliculas);
//        listaDeListas.get(1).setListName("Anime");
//        listaDeListas.get(2).setContent(listaPeliculas);
//        listaDeListas.get(2).setListName("Pendientes");
//        listaDeListas.get(3).setContent(listaPeliculas);
//        listaDeListas.get(3).setListName("Watch List");
//        listaDeListas.get(4).setContent(listaPeliculas);
//        listaDeListas.get(4).setListName("Sci-Fi");
//        listaDeListas.get(5).setContent(listaPeliculas);
//        listaDeListas.get(5).setListName("Españolas");

        return filmLists;
    
    }
}
