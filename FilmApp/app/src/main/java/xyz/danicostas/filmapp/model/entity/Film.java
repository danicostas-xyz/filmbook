package xyz.danicostas.filmapp.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Film implements Serializable {
    private String peliculaID;
    private String titulo;
    private double calificacion;
    private String directorID;
    private String posterURL;
    private List<String> generos;

    public String getPeliculaID() {
        return peliculaID;
    }

    public void setPeliculaID(String peliculaID) {
        this.peliculaID = peliculaID;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    private Map<String, Comentario> comentarios;


    public Film(String peliculaID, String titulo, double calificacion, String directorID, List<String> generos, Map<String, Comentario> comentarios) {
        this.peliculaID = peliculaID;
        this.titulo = titulo;
        this.calificacion = calificacion;
        this.directorID = directorID;
        this.generos = generos;
        this.comentarios = comentarios;
    }

    public Film(String theDeparted, double calificacion, String directorID6, List<String> list) {
        this.titulo = theDeparted;
        this.calificacion = calificacion;
        this.directorID = directorID6;
        this.generos = list;
    }

    public Film() {

    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getDirectorID() {
        return directorID;
    }

    public void setDirectorID(String directorID) {
        this.directorID = directorID;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public Map<String, Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<String, Comentario> comentarios) {
        this.comentarios = comentarios;
    }


    public static class Comentario {
        private String texto;
        private String usuarioID;
        private String fecha;
        private String peliculaID;
        private String peliculaTitulo;
        private int valoracion;


        public Comentario(String texto, String usuarioID, String fecha, String peliculaID, String peliculaTitulo, int valoracion) {
            this.texto = texto;
            this.usuarioID = usuarioID;
            this.fecha = fecha;
            this.peliculaID = peliculaID;
            this.peliculaTitulo = peliculaTitulo;
            this.valoracion = valoracion;
        }


        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }

        public String getUsuarioID() {
            return usuarioID;
        }

        public void setUsuarioID(String usuarioID) {
            this.usuarioID = usuarioID;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getPeliculaID() {
            return peliculaID;
        }

        public void setPeliculaID(String peliculaID) {
            this.peliculaID = peliculaID;
        }

        public String getPeliculaTitulo() {
            return peliculaTitulo;
        }

        public void setPeliculaTitulo(String peliculaTitulo) {
            this.peliculaTitulo = peliculaTitulo;
        }

        public int getValoracion() {
            return valoracion;
        }

        public void setValoracion(int valoracion) {
            this.valoracion = valoracion;
        }
    }
}
