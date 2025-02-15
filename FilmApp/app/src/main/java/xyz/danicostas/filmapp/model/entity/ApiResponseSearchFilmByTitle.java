package xyz.danicostas.filmapp.model.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponseSearchFilmByTitle {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Film> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Film> getResults() {
        return results;
    }

    public void setResults(List<Film> results) {
        this.results = results;
    }

}
