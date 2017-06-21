package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.adapter.ProdutoAdapater;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.util.ModoDominio;
import br.com.gabrielfigueira.apppizzaria.util.SOHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

public class ProdutoListaController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnClickListener {
    private Button btnCadastrar;
    private ListView lstProduto;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produto_lista);
        lstProduto = (ListView) findViewById(R.id.lstProduto);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(this);
        lstProduto.setLongClickable(true);
        lstProduto.setOnItemLongClickListener(this);
        lstProduto.setOnItemClickListener(this);
        context = this;

        setTitle("Produtos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            preencherListView();
        } catch (Exception ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Pizzaria App");
            dlg.setMessage(ex.getMessage());
            dlg.setCancelable(false);
            dlg.setPositiveButton("OK", null);
            dlg.show();
        }
    }

    private void preencherListView() throws Exception {
        List<Produto> lista = new ProdutoDAO(this).pesquisarPorDescricao("");
        ProdutoAdapater adp = new ProdutoAdapater(this, lista);
        lstProduto.setAdapter(adp);
        adp.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCadastrar) {
            Intent it = new Intent(this, ProdutoFormController.class);
            startActivity(it);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Produto produto = (Produto) parent.getItemAtPosition(position);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Pizzaria App");
        dlg.setMessage("Tem certeza que deseja deletar o produto " + produto.getDescricao() + "?");
        dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ProdutoDAO(context).deletaProduto(produto.getId());
                //Consumo WEBSERVICE
                try {
                    if (SOHelper.possuiRedeDisponivel(context)) {
                        String strResposta = new WebService(context).execute("Excluir", "https://pizzariaapi.herokuapp.com/api/produtos/excluir", produto.toJson().toString()).get();
                        JSONObject resposta = new JSONObject(strResposta);

                        if (!resposta.isNull("response") && resposta.getInt("response") < 0)
                            throw new Exception("Erro ao executar serviço!");
                    }
                }catch (Exception ex){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                    dlg.setTitle("Pizzaria App");
                    dlg.setMessage(ex.getMessage());
                    dlg.setCancelable(false);
                    dlg.setPositiveButton("OK", null);
                    dlg.show();
                }
                onResume();
            }
        });
        dlg.setNegativeButton("NÃO", null);
        dlg.show();

        return true;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Produto produto = (Produto) parent.getItemAtPosition(position);
        Intent it = new Intent(getApplicationContext(), ProdutoFormController.class);
        it.putExtra("id", produto.getId());
        startActivity(it);
    }
}
