package xyz.danicostas.filmapp.model.entity;

import java.io.Serializable;
import java.util.List;

public class FilmList implements Serializable {
    private String listName;
    private List<Film> content;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<Film> getContent() {
        return content;
    }

    public void setContent(List<Film> content) {
        this.content = content;
    }
}
