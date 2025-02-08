package xyz.danicostas.filmapp.model.persistence;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import xyz.danicostas.filmapp.model.entity.Film;

public class DaoPelicula {
    private static final String COLLECTION_NAME = "peliculas";
    private final FirebaseFirestore db;

    public DaoPelicula() {
        db = FirebaseFirestore.getInstance();
    }

    public void createPelicula(Film pelicula, OnCompleteListener<DocumentReference> listener) {
        db.collection(COLLECTION_NAME)
                .add(pelicula)  // Firestore genera automáticamente un ID para la película
                .addOnCompleteListener(listener);
    }

    public void getPeliculaById(String peliculaID, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection(COLLECTION_NAME)
                .document(peliculaID)
                .get()
                .addOnCompleteListener(listener);
    }

    public void getAllPeliculas(OnCompleteListener<QuerySnapshot> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(listener);
    }

    public void updatePelicula(String peliculaID, Film pelicula, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME)
                .document(peliculaID)
                .set(pelicula)  // Reemplaza los datos del documento existente
                .addOnCompleteListener(listener);
    }

    public void deletePelicula(String peliculaID, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME)
                .document(peliculaID)
                .delete()
                .addOnCompleteListener(listener);
    }
}
