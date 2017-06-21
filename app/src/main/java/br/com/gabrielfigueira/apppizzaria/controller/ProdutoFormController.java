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

import org.json.JSONObject;

import java.util.Date;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.util.SOHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

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
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            try {
                String acao = "";
                produto.setDescricao(edtDescricao.getText().toString());

                ProdutoDAO produtoDAO = new ProdutoDAO(this);
                if (id == 0) {
                    id = produtoDAO.inserir(produto);
                    acao = "Inserir";
                } else {
                    id = produtoDAO.atualizar(produto);
                    acao = "Alterar";
                }
                if (produto.getId() == 0)
                    produto.setId(id);

                //Consumo WEBSERVICE
                try {
                    if (SOHelper.possuiRedeDisponivel(this)) {
                        if (acao.equals("Inserir"))
                            produto.setData_sincronizacao(new Date());

                        String strResposta = new WebService(this).execute(acao, "https://pizzariaapi.herokuapp.com/api/produtos/salvar", produto.toJson().toString()).get();
                        JSONObject resposta = new JSONObject(strResposta);

                        //Caso de algum erro, zere a data de sincronização porque não foi inserido no BD do webservice, abrindo para futura sincronização.
                        if (!resposta.isNull("response") && resposta.getInt("response") < 0) {
                            if (acao.equals("Inserir"))
                                produto.setData_sincronizacao(null);

                            throw new Exception("Erro ao executar serviço!");
                        }
                        //Caso tiver sido inserido, devemos atualizar o id centralizado e a data de sincronização, este já foi preenchido anterior.
                        if (acao.equals("Inserir")) {
                            if (!resposta.isNull("response") && resposta.getInt("response") > 0)
                                produto.setId_centralizado(resposta.getInt("response"));
                            produtoDAO.atualizar(produto);
                        }
                    }
                }catch (Exception ex){
                    dlg.setTitle("Pizzaria App");
                    dlg.setMessage(ex.getMessage());
                    dlg.setCancelable(false);
                    dlg.setPositiveButton("OK", null);
                    dlg.show();
                }

                dlg.setTitle("Pizzaria App");
                dlg.setMessage("Operação realizada com sucesso!");
                dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dlg.show();

            } catch (Exception ex) {
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }
}
