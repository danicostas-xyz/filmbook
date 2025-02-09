package xyz.danicostas.filmapp.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xyz.danicostas.filmapp.model.entity.FilmList;

public class User {
    private String nombre;
    private String email;
    private String username;
    private String password;
    private List<FilmList> listaPeliculas;
    private Map<String, Comentario> comentarios;
    private List<User> listaAmigos;
    private String url;


    public User() {
    }


    public User(String nombre, String username, String password, List<FilmList> listaPeliculas, Map<String, Comentario> comentarios) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.listaPeliculas = listaPeliculas;
        this.comentarios = comentarios;
        listaPeliculas.add(new FilmList("Favorites", new ArrayList<Film>()));
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

    public List<FilmList> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<FilmList> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public Map<String, Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<String, Comentario> comentarios) {
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
