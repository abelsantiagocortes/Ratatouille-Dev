package com.example.ratatouille.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Agree implements Serializable {

    List<String> ingreChef;
    List<String> ingreClient;
    List<String> toolsChef;
    List<String> toolsClient;
    boolean clienteAccept;
    boolean chefAccept;
    Recipe receta;
    String agreementId;
    String solicitudId;

    public String getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(String solicitudId) {
        this.solicitudId = solicitudId;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Agree() {
        ingreChef = new ArrayList<>();
        ingreClient = new ArrayList<>();
        toolsChef = new ArrayList<>();
        toolsClient = new ArrayList<>();
        receta = new Recipe();
        clienteAccept= false;
        chefAccept= false;
    }


    public List<String> getIngreChef() {
        return ingreChef;
    }

    public void setIngreChef(List<String> ingreChef) {
        this.ingreChef = ingreChef;
    }

    public List<String> getIngreClient() {
        return ingreClient;
    }

    public void setIngreClient(List<String> ingreClient) {
        this.ingreClient = ingreClient;
    }

    public List<String> getToolsChef() {
        return toolsChef;
    }

    public void setToolsChef(List<String> toolsChef) {
        this.toolsChef = toolsChef;
    }

    public List<String> getToolsClient() {
        return toolsClient;
    }

    public void setToolsClient(List<String> toolsClient) {
        this.toolsClient = toolsClient;
    }

    public boolean isClienteAccept() {
        return clienteAccept;
    }

    public void setClienteAccept(boolean clienteAccept) {
        this.clienteAccept = clienteAccept;
    }

    public boolean isChefAccept() {
        return chefAccept;
    }

    public void setChefAccept(boolean chefAccept) {
        this.chefAccept = chefAccept;
    }

    public Recipe getReceta() {
        return receta;
    }

    public void setReceta(Recipe receta) {
        this.receta = receta;
    }

    public String getIdChef() {
        return idChef;
    }

    public void setIdChef(String idChef) {
        this.idChef = idChef;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    String idChef;
    String idClient;

}
