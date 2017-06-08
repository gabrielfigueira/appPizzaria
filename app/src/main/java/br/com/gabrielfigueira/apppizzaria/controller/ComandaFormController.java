package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.text.format.Time;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ClienteDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;

public class ComandaFormController extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMesa;
    private Spinner spnCliente;

    private Button btnSalvar;
    private Button btnCancelar;
    private Comanda comanda;

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comada_form);

        edtMesa = (EditText)findViewById(R.id.edtMesa);
        spnCliente = (Spinner) findViewById(R.id.spnCliente);

        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
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
                List<Cliente> lista = new ClienteDAO(this).pesquisarPorCliente("");
                int idx = -1;
                for(Cliente cliente: lista){
                    if (cliente.getId() == comanda.getCliente().getId()){
                        idx = lista.indexOf(cliente);
                        break;
                    }
                }
                ArrayAdapter<Cliente> dataAdapter = new ArrayAdapter<Cliente>(this,
                        android.R.layout.simple_spinner_item, lista);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCliente.setAdapter(dataAdapter);
                if (idx >= 0)
                    spnCliente.setSelection(idx);

            }catch(Exception e){
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
                comanda.setMesa(edtMesa.getText().toString());
                comanda.setCliente((Cliente)spnCliente.getSelectedItem());
                comanda.setData_hora_abertura(new Date());

                if ( id == 0){
                    id = new ComandaDAO(this).inserir(comanda);
                }else{
                    id = new ComandaDAO(this).atualizar(comanda);
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
