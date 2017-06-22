package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.adapter.ClienteAdapter;
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaAdapter;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ClienteDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.util.ModoDominio;
import br.com.gabrielfigueira.apppizzaria.util.SOHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

public class ClienteListaController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnClickListener{
    private Button btnCadastrar;
    private ListView lstCliente;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_lista);

        lstCliente = (ListView)findViewById(R.id.lstCliente);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);
        lstCliente.setLongClickable(true);
        lstCliente.setOnItemLongClickListener(this);
        lstCliente.setOnItemClickListener(this);
        context = this;

        setTitle("Clientes");
    }
    @Override
    protected void onResume(){
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
        List<Cliente> lista = new ClienteDAO(context).pesquisarPorNome("");
        ClienteAdapter adp = new ClienteAdapter(context, lista);
        lstCliente.setAdapter(adp);
        adp.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cliente cliente = (Cliente)parent.getItemAtPosition(position);
        Intent it = new Intent(getApplicationContext(),ClienteFormController.class);
        it.putExtra("id", cliente.getId());
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Cliente cliente = (Cliente)parent.getItemAtPosition(position);

        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle("Cliente");
        dlg.setMessage("Tem certeza que deseja deletar a cliente " + cliente.getNome() + "?");
        dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ClienteDAO(getApplicationContext()).deletar(cliente.getId());
                //Consumo WEBSERVICE
                try {
                    if (SOHelper.possuiRedeDisponivel(context)) {
                        String strResposta = new WebService(context).execute("Excluir", "https://pizzariaapi.herokuapp.com/api/clientes/excluir", cliente.toJson().toString()).get();
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
    public void onClick(View v) {
        if (v.getId() == R.id.btnCadastrar){
            try {
                Intent it = new Intent(
                        context,
                        ClienteFormController.class
                );
                //Abrir a Atividade
                startActivityForResult(it, ModoDominio.inserir.getValor());
            } catch (Exception ex){
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int id = data.getIntExtra("id", 0);
            //Consumo WEBSERVICE
            try {
                if (SOHelper.possuiRedeDisponivel(context)) {
                    ClienteDAO clienteDAO = new ClienteDAO(context);
                    Cliente cliente = clienteDAO.pesquisarPorId(id);

                    String acao = "";
                    if (cliente.getId_centralizado() == 0) {
                        cliente.setData_sincronizacao(new Date());
                        acao = "Inserir";
                    }else {
                        acao = "Alterar";
                    }
                    String strResposta = new WebService(context).execute(acao, "https://pizzariaapi.herokuapp.com/api/clientes/salvar", cliente.toJson().toString()).get();
                    JSONObject resposta = new JSONObject(strResposta);

                    //Caso de algum erro, zere a data de sincronização porque não foi inserido no BD do webservice, abrindo para futura sincronização.
                    if (!resposta.isNull("response") && resposta.getInt("response") < 0) {
                        if (acao.equals("Inserir"))
                            cliente.setData_sincronizacao(null);

                        throw new Exception("Erro ao executar serviço!");
                    }
                    //Caso tiver sido inserido, devemos atualizar o id centralizado e a data de sincronização, este já foi preenchido anterior.
                    if (acao.equals("Inserir")) {
                        if (!resposta.isNull("response") && resposta.getInt("response") > 0)
                            cliente.setId_centralizado(resposta.getInt("response"));
                        clienteDAO.atualizar(cliente);
                    }
                }
            } catch (Exception ex) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("Pizzaria App");
                dlg.setMessage(ex.getMessage());
                dlg.setCancelable(false);
                dlg.setPositiveButton("OK", null);
                dlg.show();
            }
        }
    }
}
