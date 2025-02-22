package xyz.danicostas.filmapp.model.entity;

import java.util.List;

/**
 * Representa la respuesta de la API al obtener palabras clave (keywords) asociadas a una película
 * por su ID.
 */
public class ApiResponseKeywordsByFilmId {

    /**
     * Identificador de la película.
     */
    private int id;

    /**
     * Lista de palabras clave asociadas a la película.
     */
    private List<KeywordOrGenres> keywords;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public List<KeywordOrGenres> getKeywords() { return keywords; }
    public void setKeywords(List<KeywordOrGenres> keywords) { this.keywords = keywords; }

    @Override
    public String toString() {
        return "ApiResponseKeywordsByFilmId{" +
                "id=" + id +
                ", keywords=" + keywords +
                '}';
    }
}
