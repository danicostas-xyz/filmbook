package xyz.danicostas.filmapp.model.service;

import java.util.List;
import java.util.Map;

import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.entity.User;

public class UserService {
    private static UserService instance;
    private UserService(){};
    public static UserService getInstance() {
        return instance == null ? instance = new UserService() : instance;
    }
}
