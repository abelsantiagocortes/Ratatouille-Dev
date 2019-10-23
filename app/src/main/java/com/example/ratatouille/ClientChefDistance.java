package com.example.ratatouille;

import android.graphics.Bitmap;

public class ClientChefDistance implements Comparable<ClientChefDistance>{

    String idClient;
    String idChef;
    String chefName;
    Bitmap[] imgChef;
    double distance;


    public Bitmap[] getImgChef() {
        return imgChef;
    }

    public void setImgChef(Bitmap[] imgChef) {
        this.imgChef = imgChef;
    }

    public ClientChefDistance(String idClient, String idChef, String chefName, Bitmap[] imgChef, double distance) {
        this.idClient = idClient;
        this.idChef = idChef;
        this.chefName = chefName;
        this.imgChef = imgChef;
        this.distance = distance;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }


    public String getIdChef() {
        return idChef;
    }

    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ClientChefDistance(String idClient, String idChef, String chefName, double distance) {
        this.idClient = idClient;
        this.idChef = idChef;
        this.chefName = chefName;
        this.distance = distance;
    }

    @Override
    public int compareTo(ClientChefDistance o) {
        if(this.distance<o.distance)
        {
            return -1;
        }
        else if (this.distance>o.distance)
        {
            return 1;
        }
        return 0;
    }
}
