package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;

public class ComandaCorpoController extends AppCompatActivity {
    private EditText edtMesa;
    private EditText edtCliente_nome;
    private Button btnEditar;
    private Button btnCadastrar;

    private int id;
    private Comanda comanda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_corpo);

        edtMesa = (EditText)findViewById(R.id.edtMesa);
        edtCliente_nome = (EditText)findViewById(R.id.edtCliente_nome);
        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);


        setTitle("Comanda");
        Intent it = getIntent();
        if (it != null){
            try{
                id = it.getIntExtra("id",0);
                comanda = new ComandaDAO(this).pesquisarPorId(id);
                if (comanda == null){
                    comanda = new Comanda();
                }
                edtMesa.setText(comanda.getMesa());
                edtCliente_nome.setText(comanda.getCliente().getNome());


            }catch(Exception e){
                Log.e("ERRO", e.getMessage());
            }
        }
    }
}
