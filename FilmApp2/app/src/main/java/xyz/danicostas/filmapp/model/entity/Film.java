package xyz.danicostas.filmapp.model.entity;

import java.util.List;
import java.util.Map;

public class Film {
    private String filmTitle;
    private double rating;
    private String directorId;
    private List<String> genres;
    private Map<String, Review> reviews;
}
