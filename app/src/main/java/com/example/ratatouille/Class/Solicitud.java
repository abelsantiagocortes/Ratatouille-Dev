package com.example.ratatouille.Class;

public class Solicitud {
    private String idCliente;
    private String idChef;
    private String idSolicitud;
    //private Agree acuerdoSolicitud;
    //private double distancia;


    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdChef() {
        return idChef;
    }

    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }

    public Solicitud(String idCliente, String idChef, String idSolicitud) {
        this.idCliente = idCliente;
        this.idChef = idChef;
        this.idSolicitud = idSolicitud;
    }

    public Solicitud(String idCliente, String idChef) {
        this.idCliente = idCliente;
        this.idChef = idChef;
    }
    public Solicitud() {

    }


}
