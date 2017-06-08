package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;

/**
 * Created by Fabricio on 08/06/2017.
 */

public class ProdutoDAO {
    public ProdutoDAO (Context context){

    }

    public List<Produto> pesquisarPorDescricao(String descricao){
        List<Produto> lista = new ArrayList<>();
        Produto pro = new Produto();
        pro.setId(1);
        pro.setDescricao("Macarronada");
        lista.add(pro);
        pro = new Produto();
        pro.setId(1);
        pro.setDescricao("Macarronada");
        lista.add(pro);
        return lista;
    }
}
