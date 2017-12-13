package br.ufc.quixada.qdetective.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DenunciaServer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("descricao")
    @Expose
    private String descricao;
    @SerializedName("data")
    @Expose
    private long data;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("uriMidia")
    @Expose
    private String uriMidia;
    @SerializedName("usuario")
    @Expose
    private String usuario;
    @SerializedName("categoria")
    @Expose
    private String categoria;

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

    public long getData() {
        return data;
    }

    public void setData(long data) {
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public DenunciaServer(Integer id, String descricao, long data, Double longitude, Double latitude, String uriMidia, String usuario, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.longitude = longitude;
        this.latitude = latitude;
        this.uriMidia = uriMidia;
        this.usuario = usuario;
        this.categoria = categoria;
    }
}