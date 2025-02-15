package xyz.danicostas.filmapp.model.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String username;
    private List<FilmList> listasDeListas;
    private List<Review> listOfReviews;
    // Lo mejor sería tener una List<Map<String, List<Film>>>
    // Y eliminar la clase FilmList, que no aporta nada
    // Y para comentarios, tener una List<Review> (porque solo hay una lista de reviews
    // y entonces no hay que poner varias listas con títulos distintos
    private List<User> listaAmigos;
    private String url;

    public User(String id, String name, String email, String username, List<FilmList> listasDeListas,
                List<Review> listOfReviews, List<User> listaAmigos) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.listasDeListas = listasDeListas;
        this.listOfReviews = listOfReviews;
        this.listaAmigos = listaAmigos;
        listasDeListas.add(new FilmList("Favorites", new ArrayList<Film>()));
    }

    public User() {
    }

    public User(String username, String url) {
        this.username = username;
        this.url = url;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<FilmList> getListasDeListas() {
        return listasDeListas;
    }

    public void setListasDeListas(List<FilmList> listasDeListas) {
        this.listasDeListas = listasDeListas;
    }

    public List<Review> getListOfReviews() {
        return listOfReviews;
    }

    public void setListOfReviews(List<Review> listOfReviews) {
        this.listOfReviews = listOfReviews;
    }

    public List<User> getListaAmigos() {
        return listaAmigos;
    }

    public void setListaAmigos(List<User> listaAmigos) {
        this.listaAmigos = listaAmigos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", listasDeListas=" + listasDeListas +
                ", listOfReviews=" + listOfReviews +
                ", listaAmigos=" + listaAmigos +
                ", url='" + url + '\'' +
                '}';
    }
}
