package xyz.danicostas.filmapp.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
    private String nombre;
    private String email;
    private String username;
    private String password;
    private List<FilmList> listasDeListas;
    private Map<String, Review> comentarios;
    // Lo mejor sería tener una List<Map<String, List<Film>>>
    // Y eliminar la clase FilmList, que no aporta nada
    // Y para comentarios, tener una List<Review> (porque solo hay una lista de reviews
    // y entonces no hay que poner varias listas con títulos distintos
    private List<User> listaAmigos;
    private String url;

    public User() {
    }

    public User(String nombre, String username, String password, List<FilmList> listasDeListas,
                Map<String, Review> comentarios) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.listasDeListas = listasDeListas;
        this.comentarios = comentarios;
        listasDeListas.add(new FilmList("Favorites", new ArrayList<Film>()));
    }

    public User(String username, String url) {
        this.username = username;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FilmList> getListasDeListas() {
        return listasDeListas;
    }

    public void setListasDeListas(List<FilmList> listasDeListas) {
        this.listasDeListas = listasDeListas;
    }

    public Map<String, Review> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<String, Review> comentarios) {
        this.comentarios = comentarios;
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
}
