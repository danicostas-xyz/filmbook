package xyz.danicostas.filmapp.model.entity;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String username;
    private Map<String, List<String>> filmLists;
    private Map<String, Review> reviews;
}
