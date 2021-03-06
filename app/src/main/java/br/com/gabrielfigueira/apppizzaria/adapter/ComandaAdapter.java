package br.com.gabrielfigueira.apppizzaria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class ComandaAdapter extends BaseAdapter {
    private List<Comanda> lista;
    private LayoutInflater layout;

    public ComandaAdapter(Context contexto, List<Comanda> lista){
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
        Comanda comanda = lista.get(position);
        convertView = layout.inflate(R.layout.comanda_lista_item, null);

        //Associar os atributos do objeto aos elementos da lista
        TextView tv1 = (TextView)convertView.findViewById(R.id.txtMesa);
        tv1.setText(String.format("%s (%s)", comanda.getMesa(), comanda.getData_hora_abertura().toString()));

        TextView tv2 = (TextView)convertView.findViewById(R.id.txtCliente);
        tv2.setText(comanda.getCliente().getNome());
//
//        TextView tv3 = (TextView)convertView.findViewById(R.id.txtUrl);
//        tv2.setText(playlist.getUrl());
        return convertView;
    }
}
