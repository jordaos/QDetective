package br.ufc.quixada.qdetective.entity;

/**
 * Created by jordao on 09/12/17.
 */

public enum CategoriaDenuncia {
    VIAS_PUBLICAS("Vias públicas de Acesso"),
    EQUIPAMENTOS_COMUNICATARIOS("Equipamentos Comunitários"),
    LIMPEZA_URBANA("Limpeza Urbana e Saneamento");

    private String descricao;

    private CategoriaDenuncia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
}
