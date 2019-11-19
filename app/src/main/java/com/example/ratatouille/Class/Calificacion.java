package com.example.ratatouille.Class;

public class Calificacion {
    private String para;
    private String de;
    private String comentario;
    private double calificacion;
    private String idSolicitud;

    public Calificacion(String para, String de, String comentario, double calificacion, String idSolicitud) {
        this.para = para;
        this.de = de;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.idSolicitud = idSolicitud;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Calificacion(String para, String de, String comentario, double calificacion) {
        this.para = para;
        this.de = de;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}
