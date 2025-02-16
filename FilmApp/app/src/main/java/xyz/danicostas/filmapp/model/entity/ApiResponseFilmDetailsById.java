package xyz.danicostas.filmapp.model.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponseFilmDetailsById {
    public boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("belongs_to_collection")
    public Object belongsToCollection;
    public int budget;
    public List<KeywordOrGenres> genres;
    public String homepage;
    public int id;
    @SerializedName("imdb_id")
    public String imdbId;
    @SerializedName("origin_country")
    public List<String> originCountry;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("original_title")
    public String originalTitle;
    public String overview;
    public double popularity;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("production_companies")
    public List<ProductionCompany> productionCompanies;
    @SerializedName("production_countries")
    public List<ProductionCountry> productionCountries;
    @SerializedName("release_date")
    public String releaseDate;
    public int revenue;
    public int runtime;
    @SerializedName("spoken_languages")
    public List<SpokenLanguage> spokenLanguages;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;

    public static class ProductionCompany {
        public int id;
        @SerializedName("logo_path")
        public String logoPath;
        public String name;
        @SerializedName("origin_country")
        public String originCountry;
    }

    public static class ProductionCountry {
        @SerializedName("iso_3166_1")
        public String isoCode;
        public String name;
    }

    public static class SpokenLanguage {
        @SerializedName("iso_639_1")
        public String isoCode;
        @SerializedName("english_name")
        public String englishName;
        public String name;
    }
}
