package br.com.gabrielfigueira.apppizzaria.model.Entidades;

/**
 * Created by figueira on 06/06/17.
 */

public class Produto{
    private int id;
    private String descricao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
