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
import java.util.Date;
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
            startActivityForResult(it, ModoDominio.inserir.getValor());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int id = data.getIntExtra("id", 0);
            //Consumo WEBSERVICE
            try {
                ProdutoDAO produtoDAO = new ProdutoDAO(this);
                Produto produto = produtoDAO.pesquisarPorId(id);

                if (SOHelper.possuiRedeDisponivel(this)) {
                    String acao = "";
                    if (produto.getId_centralizado() == 0) {
                        produto.setData_sincronizacao(new Date());
                        acao = "Inserir";
                    }else {
                        acao = "Alterar";
                    }
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
            } catch (Exception ex) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }
}
