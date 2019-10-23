package com.example.ratatouille;

public class Solicitud {
    private String idCliente;
    private String idChef;
    private String nombreCliente;
    private double distancia;

    public Solicitud(String idCliente, String idChef, String nombreCliente, double distancia) {
        this.idCliente = idCliente;
        this.idChef = idChef;
        this.nombreCliente = nombreCliente;
        this.distancia = distancia;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
