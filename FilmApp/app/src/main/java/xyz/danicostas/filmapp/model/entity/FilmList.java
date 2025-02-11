package xyz.danicostas.filmapp.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilmList implements Serializable {
    private String listName;
    private List<Film> content;

    public FilmList() {
    }

    public FilmList(String listName, List<Film> content) {
        this.listName = listName;
        this.content = content;
    }

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

    @Override
    public String toString() {
        return "FilmList{" +
                "listName='" + listName + '\'' +
                ", content=" + content +
                '}';
    }
}
