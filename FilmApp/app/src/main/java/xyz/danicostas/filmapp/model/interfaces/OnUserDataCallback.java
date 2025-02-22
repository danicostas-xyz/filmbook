package xyz.danicostas.filmapp.model.interfaces;

import xyz.danicostas.filmapp.model.entity.User;

/**
 * Interfaz para manejar los callbacks de obtención de datos de usuario.
 */
public interface OnUserDataCallback {
    /**
     * Se ejecuta cuando la obtención de datos del usuario es exitosa.
     *
     * @param u Objeto {@link User} que contiene los datos del usuario.
     */
    void onSuccess(User u);

    /**
     * Se ejecuta cuando la obtención de datos del usuario falla.
     *
     * @param errorMessage Mensaje de error con la descripción del fallo.
     */
    void onFailure(String errorMessage);
}