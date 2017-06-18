package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;

public class ProdutoFormController extends AppCompatActivity implements View.OnClickListener {
    private EditText edtDescricao;
    private Button btnSalvar;
    private Button btnCancelar;
    private Produto produto;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produto_form);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnSalvar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        setTitle("Produto");

        Intent it = getIntent();
        if (it != null) {
            try {
                id = it.getIntExtra("id", 0);
                produto = new ProdutoDAO(this).pesquisarPorId(id);
                if (produto == null) {
                    produto = new Produto();
                }
                edtDescricao.setText(produto.getDescricao());
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancelar) {
            super.onBackPressed();
        } else if (v.getId() == R.id.btnSalvar) {
            try {
                produto.setDescricao(edtDescricao.getText().toString());
                if (id == 0) {
                    id = new ProdutoDAO(this).inserir(produto);
                } else {
                    id = new ProdutoDAO(this).atualizar(produto);
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
                Log.e("ERRO", e.getMessage());
            }
            System.out.println(id);

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Pizzaria App");
            dlg.setMessage("Operação realizada com sucesso!" + id);
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
