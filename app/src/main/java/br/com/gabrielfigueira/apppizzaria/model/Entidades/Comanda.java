package br.com.gabrielfigueira.apppizzaria.model.Entidades;

import android.util.Log;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class Comanda {
    private int id;
    private String mesa;
    private Date data_hora_abertura;
    private Date data_hora_finalizacao;
    private double desconto;
    private int id_centralizado;
    private Date data_sincronizacao;
    private Cliente cliente;
    public Comanda(){
        cliente = new Cliente();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public Date getData_hora_abertura() {
        return data_hora_abertura;
    }

    public void setData_hora_abertura(Date data_hora_abertura) {
        this.data_hora_abertura = data_hora_abertura;
    }

    public Date getData_hora_finalizacao() {
        return data_hora_finalizacao;
    }

    public void setData_hora_finalizacao(Date data_hora_finalizacao) {
        this.data_hora_finalizacao = data_hora_finalizacao;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public int getId_centralizado() {
        return id_centralizado;
    }

    public void setId_centralizado(int id_centralizado) {
        this.id_centralizado = id_centralizado;
    }

    public Date getData_sincronizacao() {
        return data_sincronizacao;
    }

    public void setData_sincronizacao(Date data_sincronizacao) {
        this.data_sincronizacao = data_sincronizacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("mesa", mesa);
        } catch(Exception ex) {
            Log.e("ERRO:", ex.getMessage());
        }
        return obj;
    }
}
