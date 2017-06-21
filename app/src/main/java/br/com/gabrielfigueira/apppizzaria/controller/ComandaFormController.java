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

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ClienteDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.util.SOHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

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
                comanda = new ComandaDAO(this).pesquisarPorId(id, false);
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

            }catch(Exception ex){
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancelar){

            super.onBackPressed();

        }else if (v.getId() == R.id.btnSalvar){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            Intent it= getIntent();
            try {
                ComandaDAO comandaDAO = new ComandaDAO(this);
                comanda.setMesa(edtMesa.getText().toString());
                comanda.setCliente((Cliente)spnCliente.getSelectedItem());
                comanda.setData_hora_abertura(new Date());
                if ( id == 0){
                    id = comandaDAO.inserir(comanda);
                    comanda.setId(id);
                }else{
                    id = comandaDAO.atualizar(comanda);
                }

                if (it != null){
                    it.putExtra("id", comanda.getId());
                    setResult(RESULT_OK, it);
                }

                dlg.setTitle("Pizzaria App");
                dlg.setMessage("Operação realizada com sucesso!"+ id);
                dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dlg.show();

            }catch(Exception ex){
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent it= getIntent();
        if ( it != null)
            setResult(RESULT_CANCELED, it);
        super.onBackPressed();
    }
}
