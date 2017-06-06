package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.text.ParseException;
import java.util.List;
import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaAdapter;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;

public class ComandaListaController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnClickListener {
    private Button btnCadastrar;
    private ListView lstComanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_lista);

        lstComanda = (ListView)findViewById(R.id.lstComanda);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);
        lstComanda.setLongClickable(true);
        lstComanda.setOnItemClickListener(this);

        setTitle("Comandas");
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            preencherListView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void preencherListView() throws ParseException {
        List<Comanda> lista = new ComandaDAO(this).pesquisarPorCliente("");
        ComandaAdapter adp = new ComandaAdapter(this, lista);
        lstComanda.setAdapter(adp);
        adp.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Comanda comanda = (Comanda)parent.getItemAtPosition(position);
        Intent it = new Intent(getApplicationContext(),ComandaFormController.class);
        it.putExtra("id", comanda.getId());
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Comanda comanda = (Comanda)parent.getItemAtPosition(position);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Comanda App");
        dlg.setMessage("Tem certeza que deseja deletar a comanda " + comanda.getMesa() + "?");
        dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ComandaDAO(getApplicationContext()).deletar(comanda.getId());
                try {
                    preencherListView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        dlg.setNegativeButton("NÃO", null);
        dlg.show();

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCadastrar){
            try {
                Intent it = new Intent(
                        this,
                        ComandaFormController.class
                );
                //Abrir a Atividade
                startActivity(it);
            } catch (Exception ex){

            }
        }
    }
}