package br.com.gabrielfigueira.apppizzaria.model.Entidades;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

/**
 * Created by figueira on 06/06/17.
 */

public class Produto{
    private int id;
    private String descricao;
    private Date data_sincronizacao;
    private int id_centralizado;

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

    public Date getData_sincronizacao(){return data_sincronizacao;}

    public void setData_sincronizacao(Date data_sincronizacao){this.data_sincronizacao = data_sincronizacao;}

    public int getId_centralizado (){return id_centralizado;}

    public void setId_centralizado(int  id_centralizado){this.id_centralizado = id_centralizado;}

    @Override
    public String toString() {
        return Integer.toString(id).trim() + " - " + descricao.trim();
    }

    public JSONObject toJson() throws Exception {
        JSONObject obj = new JSONObject();
        if (id_centralizado != 0)
            obj.put("id", id_centralizado);
        obj.put("descricao", descricao);
        obj.put("data_sincronizacao", DataHelper.dateToStr(data_sincronizacao));
        return obj;
    }
}
