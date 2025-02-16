package xyz.danicostas.filmapp.model.entity;

public class KeywordOrGenres {
    public int id;
    public String name;

    @Override
    public String toString() {
        return "KeywordOrGenres{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
