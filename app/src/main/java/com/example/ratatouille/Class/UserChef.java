package com.example.ratatouille.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserChef implements Serializable {

    String name;
    int age;
    String photoDownloadURL;
    String dir;
    double lat;
    double longi;
    String experiencia;
    String certificados;
    String años;
    boolean status;
    List<String> tools;
    List<String> recipeIds;
    String userId;
    List<String> foodTypes;

    public List<String> getFoodType() {
        return foodTypes;
    }

    public void setFoodType(List<String> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public UserChef(String name, int age, String photoDownloadURL, String dir, double lat, double longi, String experiencia, String certificados, String años, boolean status, List<String> tools, List<String> recipeIds, String userId, List<String> foodTypes) {
        this.name = name;
        this.age = age;
        this.photoDownloadURL = photoDownloadURL;
        this.dir = dir;
        this.lat = lat;
        this.longi = longi;
        this.experiencia = experiencia;
        this.certificados = certificados;
        this.años = años;
        this.status = status;
        this.tools = tools;
        this.recipeIds = recipeIds;
        this.userId = userId;
        this.foodTypes = foodTypes;
    }

    public UserChef(String name, String dir, int age) {
        this.name = name;
        this.age = age;
        this.dir = dir;
        this.tools = new ArrayList<>();
        this.recipeIds = new ArrayList<>();
        this.status = true;
        this.experiencia = "";
        this.certificados = "";
    }

    public UserChef() {
    }


    public String getUserId() {
        return userId;
    }

    public List<String> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<String> recipeIds) {
        this.recipeIds = recipeIds;
    }

        public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getCertificados() {
        return certificados;
    }

    public void setCertificados(String certificados) {
        this.certificados = certificados;
    }

    public String getAños() {
        return años;
    }

    public void setAños(String años) {
        this.años = años;
    }

    public String getPhotoDownloadURL() {
        return photoDownloadURL;
    }

    public void setPhotoDownloadURL(String photoDownloadURL) {
        this.photoDownloadURL = photoDownloadURL;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
