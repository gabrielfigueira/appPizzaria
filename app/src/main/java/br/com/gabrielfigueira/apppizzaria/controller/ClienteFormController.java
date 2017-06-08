package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ClienteDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;

public class ClienteFormController extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome;
    private EditText edtCpf;
    private EditText edtLogradouro;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCep;
    private EditText edtCidade;
    private EditText edtTelefone;
    private EditText edtEmail;
    private Button btnSalvar;
    private Button btnCancelar;
    private Cliente cliente;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_form_controller);

        edtNome = (EditText)findViewById(R.id.edtNome);
        edtCpf = (EditText)findViewById(R.id.edtCpf);
        edtLogradouro = (EditText)findViewById(R.id.edtLogradouro);
        edtNumero = (EditText)findViewById(R.id.edtNumero);
        edtBairro = (EditText)findViewById(R.id.edtBairro);
        edtCep = (EditText)findViewById(R.id.edtCep);
        edtCidade = (EditText)findViewById(R.id.edtCidade);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
//        edtEmail = (EditText)findViewById(R.id.edtEmail);

        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        setTitle("Comanda");

        Intent it = getIntent();
        if (it != null) {
            try {
                id = it.getIntExtra("id", 0);
                cliente = new ClienteDAO(this).pesquisarPorId(id);
                if (cliente == null) {
                    cliente = new Cliente();
                }
                edtNome.setText(cliente.getNome());
                edtCpf.setText(cliente.getCpf());
                edtLogradouro.setText(cliente.getLogradouro());
                edtNumero.setText(cliente.getNumero());
                edtBairro.setText(cliente.getBairro());
                edtCep.setText(cliente.getCep());
                edtCidade.setText(cliente.getCidade());
                edtTelefone.setText(cliente.getTelefone());
//                edtEmail.setText(cliente.getEmail());

            } catch (Exception e) {
                Log.e("ERRO", e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCancelar){
            super.onBackPressed();
        }else if (v.getId() == R.id.btnSalvar){
            try {
                cliente.setNome(edtNome.getText().toString());
                cliente.setCpf(edtCpf.getText().toString());
                cliente.setLogradouro(edtLogradouro.getText().toString());
                cliente.setNumero(Integer.parseInt(edtNumero.getText().toString()));
                cliente.setBairro(edtBairro.getText().toString());
                cliente.setCep(edtCep.getText().toString());
                cliente.setCidade(edtCidade.getText().toString());
                cliente.setTelefone(edtTelefone.getText().toString());
//                cliente.setEmail(edtEmail.getText().toString());

                if ( id == 0){
                    id = new ClienteDAO(this).inserir(cliente);
                }else{
                    id = new ClienteDAO(this).atualizar(cliente);
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
                Log.e("ERRO", e.getMessage());
            }

            System.out.println(id);

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Pizzaria App");
            dlg.setMessage("Operação realizada com sucesso!"+ id);
            dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dlg.show();
        }
    }
}
