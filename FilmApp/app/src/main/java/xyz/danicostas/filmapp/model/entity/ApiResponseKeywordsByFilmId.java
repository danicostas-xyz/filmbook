package xyz.danicostas.filmapp.model.entity;

import java.util.List;

public class ApiResponseKeywordsByFilmId {
    public int id;

    @Override
    public String toString() {
        return "ApiResponseKeywordsByFilmId{" +
                "id=" + id +
                ", keywords=" + keywords +
                '}';
    }

    public List<KeywordOrGenres> keywords;
}
