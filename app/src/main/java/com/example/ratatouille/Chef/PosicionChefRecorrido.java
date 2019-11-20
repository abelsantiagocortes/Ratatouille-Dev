package com.example.ratatouille.Chef;


import com.mapbox.mapboxsdk.geometry.LatLng;

public class PosicionChefRecorrido {
    private LatLng posicion;
    private String solicitudId;

    public PosicionChefRecorrido(LatLng posicion, String solicitudId) {
        this.posicion = posicion;
        this.solicitudId = solicitudId;
    }

    public String getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(String solicitudId) {
        this.solicitudId = solicitudId;
    }

    public LatLng getPosicion() {
        return posicion;
    }

    public void setPosicion(LatLng posicion) {
        this.posicion = posicion;
    }
}
