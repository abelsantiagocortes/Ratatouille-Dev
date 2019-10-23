package com.example.ratatouille;

public class Solicitud {
    private String idCliente;
    private String idChef;
    //private String nombreCliente;
    //private double distancia;

    public Solicitud(String idCliente, String idChef) {
        this.idCliente = idCliente;
        this.idChef = idChef;
    }
    public Solicitud() {

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

}
