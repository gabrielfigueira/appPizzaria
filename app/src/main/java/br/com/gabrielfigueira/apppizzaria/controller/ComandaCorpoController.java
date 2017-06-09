package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaAdapter;
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaProdutoAdapter;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.ComandaProduto;

public class ComandaCorpoController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnClickListener {
    private EditText edtMesa;
    private EditText edtCliente_nome;
    private Button btnEditar;
    private FloatingActionButton btnCadastrar;

    private ListView lstComandaProduto;

    private int id;
    private Comanda comanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_corpo);

        edtMesa = (EditText)findViewById(R.id.edtMesa);
        edtMesa.setFocusable(false);
        edtCliente_nome = (EditText)findViewById(R.id.edtCliente_nome);
        edtCliente_nome.setFocusable(false);

        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(this);

        btnCadastrar = (FloatingActionButton) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);

        lstComandaProduto = (ListView)findViewById(R.id.lstProdutoComanda);
        lstComandaProduto.setOnItemClickListener(this);


        setTitle("Comanda");
        Intent it = getIntent();
        if (it != null){
            try{
                id = it.getIntExtra("id", 0);
            }catch(Exception e){
                Log.e("ERRO", e.getMessage());
            }
        }
    }

    private void atualizaTela() throws ParseException {
        comanda = new ComandaDAO(this).pesquisarPorId(id);
        if (comanda == null){
            comanda = new Comanda();
        }
        edtMesa.setText(comanda.getMesa());
        edtCliente_nome.setText(comanda.getCliente().getNome());

        preencherListView();
    }

    private void preencherListView() throws ParseException {
        List<ComandaProduto> lista = new ComandaProdutoDAO(this).pesquisarPorProduto("");
        ComandaProdutoAdapter adp = new ComandaProdutoAdapter(this, lista);
        lstComandaProduto.setAdapter(adp);
        adp.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            atualizaTela();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final ComandaProduto produto = (ComandaProduto)adapterView.getItemAtPosition(position);
        Intent it = new Intent(getApplicationContext(),ComandaProdutoFormController.class);
        it.putExtra("id", produto.getId());
        startActivity(it);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnEditar){
            Intent it = new Intent(getApplicationContext(),ComandaFormController.class);
            it.putExtra("id", comanda.getId());
            startActivity(it);
        }else if(view.getId() == R.id.btnCadastrar) {
            Intent it = new Intent(getApplicationContext(),ComandaProdutoFormController.class);
            it.putExtra("id", 0);
            startActivity(it);
        }
    }
}
