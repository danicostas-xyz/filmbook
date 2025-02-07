package xyz.danicostas.filmapp.model.entity;

public class Comentario {
    private String texto;
    private String peliculaID;
    private String peliculaTitulo;
    private String usuarioID;
    private String fecha;
    private int valoracion;

    public Comentario() {
        // Constructor vacío requerido por Firestore
    }

    public Comentario(String texto, String peliculaID, String peliculaTitulo, String usuarioID, String fecha, int valoracion) {
        this.texto = texto;
        this.peliculaID = peliculaID;
        this.peliculaTitulo = peliculaTitulo;
        this.usuarioID = usuarioID;
        this.fecha = fecha;
        this.valoracion = valoracion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
