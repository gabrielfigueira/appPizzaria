package br.com.gabrielfigueira.apppizzaria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.ComandaProduto;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class ComandaProdutoAdapter extends BaseAdapter {
    private List<ComandaProduto> lista;
    private LayoutInflater layout;

    public ComandaProdutoAdapter(Context contexto, List<ComandaProduto> lista){
        this.layout = LayoutInflater.from(contexto);
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return  (lista!=null)?lista.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (lista!=null)?lista.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return (lista.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComandaProduto produto = lista.get(position);
        convertView = layout.inflate(R.layout.comanda_produto_lista_item, null);

        //Associar os atributos do objeto aos elementos da lista
        TextView tv1 = (TextView)convertView.findViewById(R.id.txtLinha);
        tv1.setText(String.format("%s - %s (%s)", Integer.toString(produto.getId()).trim() , produto.getProduto().getDescricao().trim(), Double.toString(produto.getQuantidade()).trim()));
        return convertView;
    }
}
