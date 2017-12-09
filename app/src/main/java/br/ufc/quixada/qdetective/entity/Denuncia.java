package br.ufc.quixada.qdetective.entity;

import java.util.Date;

/**
 * Created by jordao on 08/12/17.
 */

public class Denuncia {
    private Integer id;
    private String descricao;
    private Date data;
    private Double longitude;
    private Double latitude;
    private String uriMidia;
    private String usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getUriMidia() {
        return uriMidia;
    }

    public void setUriMidia(String uriMidia) {
        this.uriMidia = uriMidia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}