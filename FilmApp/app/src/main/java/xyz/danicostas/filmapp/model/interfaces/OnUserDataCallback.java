package xyz.danicostas.filmapp.model.interfaces;

import xyz.danicostas.filmapp.model.entity.User;

public interface OnUserDataCallback {
    void onSuccess(User user);
    void onFailure(String errorMessage);
}
