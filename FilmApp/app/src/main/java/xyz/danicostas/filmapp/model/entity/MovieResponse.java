package xyz.danicostas.filmapp.model.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<FilmTMDB> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<FilmTMDB> getResults() {
        return results;
    }

    public void setResults(List<FilmTMDB> results) {
        this.results = results;
    }

    public static class FilmTMDB {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("original_title")
        private String originalTitle;

        @SerializedName("overview")
        private String overview;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("release_date")
        private String releaseDate;

        @SerializedName("vote_average")
        private double voteAverage;

        @SerializedName("vote_count")
        private int voteCount;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }
    }
}
