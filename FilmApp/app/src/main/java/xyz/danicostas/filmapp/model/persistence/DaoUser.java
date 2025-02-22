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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.interfaces.OnUserDataCallback;

public class DaoUser {
    private static DaoUser instance;
    private static final String COLLECTION_NAME = "usuarios";
    private final FirebaseFirestore db;

    private DaoUser() {
        db = FirebaseFirestore.getInstance();
    }

    public static DaoUser getInstance() {
        return instance == null ? instance = new DaoUser() : instance;
    }

    public void createUser(String userId, User user, OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .document(userId)
                .set(user)
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

    /**
     *
     * El problema era un típico caso de asincronía: el Log.d mostraba la referencia de MutableLiveData, pero aún no tenía los datos. Ahora, la estructura con LiveData en FilmListsViewModel y RecyclerView.Adapter maneja correctamente los datos cuando están listos.
     * Tú dijiste:
     * pero bueno, sigo sin entender. antes dao devolvía una List a servicio, que devolvía una list a ViewModel, que ahí tenía un método que era MutableLiveData.setValue(list). Ahora desde el dao se devuelve un MutableLiveData que se asigna al MutableLiveData del ViewModel. NO ENTIENDO POR FAVOR EXPLÍCAME. SI EL MUTABLELIVEDATA TENÍA EL MÉTODO SETVALUE ANTES. POR QUÉ NO FUNCIONABA Y AHORA SÍ
     * ChatGPT Plus
     *
     * Vale, Dani, vamos a desglosarlo bien porque entiendo la confusión.
     *
     * Antes, lo que hacías era esto (más o menos en términos de flujo):
     *
     *     DaoUsuario obtenía una List<FilmList> de Firestore y la devolvía como una List<FilmList>.
     *     GestorUser recibía esa List<FilmList> y la devolvía también como List<FilmList>.
     *     FilmListsViewModel hacía listaPeliculasLiveData.setValue(lista) con la lista obtenida.
     *
     * ¿Por qué esto no funcionaba?
     *
     * Porque setValue(lista) en un MutableLiveData solo notifica a los observadores si la lista que le pasas es una NUEVA referencia de objeto. Pero como en ese flujo la List<FilmList> iba pasando por capas sin realmente "cambiar" como objeto en memoria, LiveData no detectaba cambios y no notificaba a la vista.
     * ¿Por qué funciona ahora?
     *
     * Ahora, lo que haces es devolver un MutableLiveData<List<FilmList>> directamente desde el DAO y propagar ese MutableLiveData en todas las capas (en vez de pasar una List<FilmList> y asignarla manualmente).
     *
     *     DaoUsuario obtiene la lista y la almacena en un MutableLiveData.
     *     GestorUser simplemente devuelve ese mismo MutableLiveData.
     *     FilmListsViewModel asigna el MutableLiveData devuelto por GestorUser.
     *
     * ¿Qué cambia aquí?
     * Ahora, en el DAO, cuando actualizas los datos en Firestore y haces listaLiveData.setValue(filmLists), el mismo MutableLiveData viaja a través de todas las capas y se mantiene el vínculo con la UI. Como MutableLiveData ya está "vivo", cuando su valor cambia, se notifica automáticamente a los observadores en la interfaz.
     *
     * En cambio, en la versión anterior, cuando hacías setValue(lista), LiveData no veía un "nuevo objeto", sino la misma referencia de siempre, y no reaccionaba.
     * Resumen
     *
     * Antes:
     * ❌ setValue(lista) en ViewModel, pero como la lista en sí no era una nueva instancia, no se notificaban los cambios.
     *
     * Ahora:
     * ✅ MutableLiveData<List<FilmList>> se propaga desde el DAO hasta la UI, y cuando en el DAO se actualiza con setValue(), la UI lo detecta automáticamente.
     *
     * La clave es que el MutableLiveData ahora se crea en el DAO y no en el ViewModel. Esto mantiene una única referencia viva en toda la arquitectura, en lugar de asignar manualmente la lista en cada capa.
     * Tú dijiste:
     * Entonces lo correcto sería tener el mutable live data siempre en el dao o qué?
     * ChatGPT Plus
     *
     * Sí, Dani, lo más correcto y limpio es mantener el MutableLiveData en el DAO o en el repositorio (que es donde se maneja la lógica de acceso a datos), no en el ViewModel.
     *
     * La razón es que el DAO es responsable de interactuar con la base de datos y manejar la lógica de datos, mientras que el ViewModel debe actuar como un intermediario entre la lógica de datos y la UI, pero no debe ser responsable de la obtención de datos o su manipulación directa.
     * ¿Por qué mantener el MutableLiveData en el DAO?
     *
     *     Encapsulamiento de la lógica de acceso a datos: El DAO es donde deberías manejar las interacciones con la base de datos, ya sea Firestore o cualquier otra fuente de datos. Esto asegura que la lógica de acceso a datos esté centralizada y sea reutilizable.
     *
     *     Reactivo y en tiempo real: Al tener el MutableLiveData en el DAO, puedes actualizarlo en tiempo real a medida que los datos cambian. Si algo cambia en la base de datos (como un nuevo objeto o un cambio en la lista), el MutableLiveData notifica a la UI automáticamente, sin que el ViewModel necesite hacer mucho más trabajo.
     *
     *     Flujo simplificado: El ViewModel simplemente observa el LiveData que le pasa el DAO y actualiza la UI cuando hay cambios. No necesita preocuparse por el manejo de datos en sí. Esto separa claramente las responsabilidades y hace que el código sea más modular.
     *
     * ¿Qué debería hacer el ViewModel?
     *
     * El ViewModel simplemente se encarga de observar ese LiveData y proporcionar los datos a la vista (UI). La lógica de manipulación de datos (agregar, eliminar, modificar) también se delega al DAO, y el ViewModel simplemente pasa los resultados a la UI.
     *
     * Flujo correcto:
     *
     *     DAO: Gestiona la obtención de datos, cambios y actualizaciones, manteniendo el MutableLiveData para notificar cambios.
     *     ViewModel: Observa ese LiveData y lo proporciona a la UI.
     *     UI: Observa el LiveData proporcionado por el ViewModel y actualiza la interfaz cuando hay un cambio.
     *
     * Este enfoque mantiene todo organizado y coherente con la arquitectura MVVM.
     */

    /**
     * Recupera las listas de películas de un usuario desde Firestore y las envuelve en un LiveData.
     *
     * @param userId El ID del usuario cuya lista de películas se va a recuperar.
     * @return Un {@link MutableLiveData} que contiene una lista de {@link FilmList}.
     *         La lista se actualizará cuando se complete la operación en Firestore.
     */
    public MutableLiveData<List<FilmList>> getUserFilmLists(String userId) {
        MutableLiveData<List<FilmList>> listaLiveData = new MutableLiveData<>();
        List<FilmList> listaDeListas = new ArrayList<>();

        db.collection(COLLECTION_NAME)
                .document(userId) // Accede al documento del usuario específico
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convierte el documento a User
                        User user = documentSnapshot.toObject(User.class);

                        if (user != null && user.getListasDeListas() != null) {
                            // Obtiene la List<FilmList> del User
                            List<FilmList> listaFirebase = user.getListasDeListas();
                            // Agrega las listas a la lista local
                            listaDeListas.addAll(listaFirebase);
                        } else {
                            Log.d("FirestoreData", "El usuario no tiene listas.");
                        }
                    }
                    listaLiveData.setValue(listaDeListas);
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Error al cargar las listas", e));

        // Esta línea se ejecuta antes que la consulta a Firebase termine, por lo que, en este punto,
        // está vacía. Es por eso que se usa un MutableLiveData y no una lista normal. Cuando la
        // consulta a Firebase finalice, se ejecuta listaLiveData.setValue() para actualizar la lista.
        return listaLiveData;
    }


    /**
     * This method queries Firestore to retrieve the document associated with the given user ID.
     * If the document exists, it converts it into a `User` object and invokes the success callback.
     * If the document does not exist, it triggers the failure callback with an appropriate message.
     * In case of a Firestore retrieval error, it logs the error message.
     *
     * @param userId   the ID of the user whose data is to be retrieved.
     * @param callback a OnUserDataCallback custom interface, that has two methods (success & failure)
     */

    public void getUserData(String userId, OnUserDataCallback callback) {
        db.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = (documentSnapshot.toObject(User.class));
                        callback.onSuccess(user);
                    }
                    else callback.onFailure("El usuario no existe.");
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Error al cargar datos", e));
    }
}
