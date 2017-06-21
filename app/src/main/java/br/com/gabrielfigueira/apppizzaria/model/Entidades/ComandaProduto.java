package br.com.gabrielfigueira.apppizzaria.model.Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

/**
 * Created by figueira on 06/06/17.
 */

public class ComandaProduto {
    private int id;
    private double quantidade;
    private Date data_hora_entrega;

    private Comanda comanda;
    private Produto produto;

    public ComandaProduto(){
        produto = new Produto();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData_hora_entrega() {
        return data_hora_entrega;
    }

    public void setData_hora_entrega(Date data_hora_entrega) {
        this.data_hora_entrega = data_hora_entrega;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public JSONObject toJson() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("produto_id", produto != null && produto.getId_centralizado() != 0?produto.getId_centralizado(): null);
        obj.put("quantidade", quantidade);
        obj.put("data_hora_entrega", DataHelper.dateToStr(data_hora_entrega));
        return obj;
    }
}
