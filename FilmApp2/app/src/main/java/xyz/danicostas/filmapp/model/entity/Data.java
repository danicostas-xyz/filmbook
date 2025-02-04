package xyz.danicostas.filmapp.model.entity;

import java.util.Map;

/**
 * This class simulates a database as a Java object parsed from bbdd.json
 */
public class Data {
    private Map<String, Film> films;
    private Map<String, User> users;
    private Map<String, Director> directors;
}
