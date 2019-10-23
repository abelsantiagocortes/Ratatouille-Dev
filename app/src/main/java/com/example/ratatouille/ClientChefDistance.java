package com.example.ratatouille;

public class ClientChefDistance implements Comparable<ClientChefDistance>{

    String idClient;
    String idChef;
    String chefName;
    int imgChef;
    double distance;

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

    public int getImgChef() {
        return imgChef;
    }

    public void setImgChef(int imgChef) {
        this.imgChef = imgChef;
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
