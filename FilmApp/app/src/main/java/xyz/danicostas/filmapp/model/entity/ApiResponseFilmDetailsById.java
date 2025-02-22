package xyz.danicostas.filmapp.model.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Clase que representa la respuesta de la API con los detalles de una película por su ID.
 */
public class ApiResponseFilmDetailsById {
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("belongs_to_collection")
    private Object belongsToCollection;
    private int budget;
    private List<KeywordOrGenres> genres;
    private String homepage;
    private int id;
    @SerializedName("imdb_id")
    private String imdbId;
    @SerializedName("origin_country")
    private List<String> originCountry;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    private String overview;
    private double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;
    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;
    @SerializedName("release_date")
    private String releaseDate;
    private int revenue;
    private int runtime;
    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;

    // Getters y Setters
    public boolean isAdult() { return adult; }
    public void setAdult(boolean adult) { this.adult = adult; }
    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }
    public Object getBelongsToCollection() { return belongsToCollection; }
    public void setBelongsToCollection(Object belongsToCollection) { this.belongsToCollection = belongsToCollection; }
    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }
    public List<KeywordOrGenres> getGenres() { return genres; }
    public void setGenres(List<KeywordOrGenres> genres) { this.genres = genres; }
    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    public List<String> getOriginCountry() { return originCountry; }
    public void setOriginCountry(List<String> originCountry) { this.originCountry = originCountry; }
    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }
    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }
    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }
    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public List<ProductionCompany> getProductionCompanies() { return productionCompanies; }
    public void setProductionCompanies(List<ProductionCompany> productionCompanies) { this.productionCompanies = productionCompanies; }
    public List<ProductionCountry> getProductionCountries() { return productionCountries; }
    public void setProductionCountries(List<ProductionCountry> productionCountries) { this.productionCountries = productionCountries; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public int getRevenue() { return revenue; }
    public void setRevenue(int revenue) { this.revenue = revenue; }
    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }
    public List<SpokenLanguage> getSpokenLanguages() { return spokenLanguages; }
    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) { this.spokenLanguages = spokenLanguages; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isVideo() { return video; }
    public void setVideo(boolean video) { this.video = video; }
    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }
    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    @Override
    public String toString() {
        return "ApiResponseFilmDetailsById{" +
                "adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", belongsToCollection=" + belongsToCollection +
                ", budget=" + budget +
                ", genres=" + genres +
                ", homepage='" + homepage + '\'' +
                ", id=" + id +
                ", imdbId='" + imdbId + '\'' +
                ", originCountry=" + originCountry +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", productionCompanies=" + productionCompanies +
                ", productionCountries=" + productionCountries +
                ", releaseDate='" + releaseDate + '\'' +
                ", revenue=" + revenue +
                ", runtime=" + runtime +
                ", spokenLanguages=" + spokenLanguages +
                ", status='" + status + '\'' +
                ", tagline='" + tagline + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }

    // Clases internas

    /**
     * Clase interna que representa una compañía de producción de la película.
     */
    public static class ProductionCompany {
        private int id;
        @SerializedName("logo_path")
        private String logoPath;
        private String name;
        @SerializedName("origin_country")
        private String originCountry;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getLogoPath() { return logoPath; }
        public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getOriginCountry() { return originCountry; }
        public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

        @Override
        public String toString() {
            return "ProductionCompany{" +
                    "id=" + id +
                    ", logoPath='" + logoPath + '\'' +
                    ", name='" + name + '\'' +
                    ", originCountry='" + originCountry + '\'' +
                    '}';
        }
    }

    /**
     * Clase interna que representa un país de producción de la película.
     */
    public static class ProductionCountry {
        @SerializedName("iso_3166_1")
        private String isoCode;
        private String name;

        public String getIsoCode() { return isoCode; }
        public void setIsoCode(String isoCode) { this.isoCode = isoCode; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @Override
        public String toString() {
            return "ProductionCountry{" +
                    "isoCode='" + isoCode + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * Clase interna que representa un idioma hablado en la película.
     */
    public static class SpokenLanguage {
        @SerializedName("iso_639_1")
        private String isoCode;
        @SerializedName("english_name")
        private String englishName;
        private String name;

        public String getIsoCode() { return isoCode; }
        public void setIsoCode(String isoCode) { this.isoCode = isoCode; }
        public String getEnglishName() { return englishName; }
        public void setEnglishName(String englishName) { this.englishName = englishName; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @Override
        public String toString() {
            return "SpokenLanguage{" +
                    "isoCode='" + isoCode + '\'' +
                    ", englishName='" + englishName + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}

