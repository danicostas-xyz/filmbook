package xyz.danicostas.filmapp.model.entity;

import java.util.List;

public class ApiResponseKeywordsByFilmId {
    public int id;
    public List<Keyword> keywords;

    public static class Keyword {
        public int id;
        public String name;
    }
}
