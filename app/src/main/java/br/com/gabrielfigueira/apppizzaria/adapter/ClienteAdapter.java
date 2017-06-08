package br.com.gabrielfigueira.apppizzaria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;

/**
 * Created by figueira on 08/06/17.
 */

public class ClienteAdapter extends BaseAdapter {
    private List<Cliente> lista;
    private LayoutInflater layout;

    public ClienteAdapter(Context contexto, List<Cliente> lista){
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
        Cliente cliente = lista.get(position);
        convertView = layout.inflate(R.layout.comanda_lista_item, null);

        //Associar os atributos do objeto aos elementos da lista
        TextView tv1 = (TextView)convertView.findViewById(R.id.txtMesa);
        tv1.setText(String.format("%s", cliente.getNome()));


//
//        TextView tv3 = (TextView)convertView.findViewById(R.id.txtUrl);
//        tv2.setText(playlist.getUrl());
        return convertView;
    }
}
