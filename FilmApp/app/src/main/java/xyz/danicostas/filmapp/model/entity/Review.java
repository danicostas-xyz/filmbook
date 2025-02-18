package xyz.danicostas.filmapp.model.entity;

import java.util.Date;

public class Review {
    private String id;
    private String userId;
    private String title;
    private String overview;
    private String posterPath;
    private String filmId;

    private String filmTitle;
    private Date date;
    private int voteAverage;

    public Review() {}

    public Review(String id, String userId, String title, String overview, String posterPath, String filmId, String filmTitle, Date date, int voteAverage) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.filmId = filmId;
        this.filmTitle = filmTitle;
        this.date = date;
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + overview + '\'' +
                ", filmId='" + filmId + '\'' +
                ", filmTitle='" + filmTitle + '\'' +
                ", date=" + date +
                ", rating=" + voteAverage +
                '}';
    }
}
