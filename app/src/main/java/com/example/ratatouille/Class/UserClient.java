package com.example.ratatouille.Class;

import java.util.ArrayList;
import java.util.List;

public class UserClient {

    String name;
    int age;
    String photoDownloadURL;
    String dir;
    public double lat;
    public double longi;
    String description;
    List<String> tools;
    boolean status;
    String userId;
    int cantRats;

    public int getCantRats() {
        return cantRats;
    }

    public void setCantRats(int cantRats) {
        this.cantRats = cantRats;
    }

    public UserClient(String name, String dir, int age, int cantRats) {
        this.name = name;
        this.age = age;
        this.dir = dir;
        this.tools = new ArrayList<>();
        this.cantRats=cantRats;
    }
    public UserClient() {
    }

    public String getUserId() {
        return userId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhotoDownloadURL() {
        return photoDownloadURL;
    }

    public void setPhotoDownloadURL(String photoDownloadURL) {
        this.photoDownloadURL = photoDownloadURL;
    }
}
