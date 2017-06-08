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
import android.widget.Spinner;

import java.util.Date;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.ComandaProduto;
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

public class ComandaProdutoFormController extends AppCompatActivity implements View.OnClickListener {
    private EditText edtQuantidade;
    private Spinner spnProduto;
    private Button btnSalvar;
    private Button btnCancelar;

    private int id;
    private ComandaProduto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_produto_form);

        edtQuantidade = (EditText)findViewById(R.id.edtQuantidade);
        spnProduto = (Spinner)findViewById(R.id.spnProduto);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        setTitle("Comanda - produto");

        Intent it = getIntent();
        if (it != null){
            try{
                id = it.getIntExtra("id",0);
                produto = new ComandaProdutoDAO(this).pesquisarPorId(id);
                if (produto == null){
                    produto = new ComandaProduto();
                }
                edtQuantidade.setText(Double.toString(produto.getQuantidade()));
                List<Produto> lista = new ProdutoDAO(this).pesquisarPorDescricao("");
                int idx = -1;
                for(Produto pro: lista){
                    if (pro.getId() == produto.getProduto().getId()){
                        idx = lista.indexOf(pro);
                        break;
                    }
                }
                ArrayAdapter<Produto> dataAdapter = new ArrayAdapter<Produto>(this,
                        android.R.layout.simple_spinner_item, lista);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProduto.setAdapter(dataAdapter);
                if (idx >= 0)
                    spnProduto.setSelection(idx);

            }catch(Exception e){
                Log.e("ERRO", e.getMessage());
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCancelar){
            super.onBackPressed();
        }else if (view.getId() == R.id.btnSalvar){
            try {
                produto.setQuantidade(Double.parseDouble(edtQuantidade.getText().toString()));
                produto.setProduto((Produto) spnProduto.getSelectedItem());

                if ( id == 0){
                    produto.setData_hora_entrega(null);
                    id = new ComandaProdutoDAO(this).inserir(produto);
                }else{
                    id = new ComandaProdutoDAO(this).atualizar(produto);
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
                Log.e("ERRO", e.getMessage());
            }

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
