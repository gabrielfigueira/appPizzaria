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
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaAdapter;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.util.ModoDominio;
import br.com.gabrielfigueira.apppizzaria.util.SOHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

public class ComandaListaController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnClickListener {
    private Button btnCadastrar;
    private ListView lstComanda;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_lista);

        lstComanda = (ListView)findViewById(R.id.lstComanda);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);
        lstComanda.setLongClickable(true);
        lstComanda.setOnItemLongClickListener(this);
        lstComanda.setOnItemClickListener(this);

        context = this;

        setTitle("Comandas");
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            preencherListView();
        } catch (ParseException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("Pizzaria App");
            dlg.setMessage(ex.getMessage());
            dlg.setCancelable(false);
            dlg.setPositiveButton("OK", null);
            dlg.show();
        }
    }

    private void preencherListView() throws ParseException {
        List<Comanda> lista = new ComandaDAO(this).pesquisarPorCliente("");
        ComandaAdapter adp = new ComandaAdapter(this, lista);
        lstComanda.setAdapter(adp);
        adp.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Comanda comanda = (Comanda)parent.getItemAtPosition(position);
        Intent it = new Intent(context,ComandaCorpoController.class);
        it.putExtra("id", comanda.getId());
        startActivity(it);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Comanda comanda = (Comanda)parent.getItemAtPosition(position);

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Pizzaria App");
        dlg.setMessage("Tem certeza que deseja deletar a comanda " + comanda.getMesa() + "?");
        dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new ComandaProdutoDAO(getApplicationContext()).deletar_por_comanda(comanda.getId());
                new ComandaDAO(getApplicationContext()).deletar(comanda.getId());

                //Consumo WEBSERVICE
                try {
                    String strResposta = new WebService(context).execute("Excluir","https://pizzariaapi.herokuapp.com/api/comandas/excluir", comanda.toJson().toString()).get();
                    JSONObject resposta = new JSONObject(strResposta);

                    if (!resposta.isNull("response") && resposta.getInt("response") < 0)
                        throw new Exception("Erro ao executar serviço!");

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
                        this,
                        ComandaFormController.class
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
        int id = data.getIntExtra("id", 0);
        if (requestCode == ModoDominio.inserir.getValor()){
            if (resultCode == RESULT_OK) {

                if (id != 0){
                    Intent it = new Intent(this,ComandaCorpoController.class);
                    it.putExtra("id", id);
                    startActivityForResult(it, ModoDominio.alterar.getValor());
                }

            }
        }else if (requestCode == ModoDominio.alterar.getValor()){
            //Consumo WEBSERVICE
            try {
                if (SOHelper.possuiRedeDisponivel(this)) {
                    ComandaDAO comandaDAO = new ComandaDAO(context);
                    Comanda comanda = comandaDAO.pesquisarPorId(id, true);
                    if (comanda.getData_sincronizacao() == null)
                        comanda.setData_sincronizacao(new Date());

                    String strResposta = new WebService(this).execute("Atualizar", "https://pizzariaapi.herokuapp.com/api/comandas/salvar", comanda.toJson().toString()).get();
                    JSONObject resposta = new JSONObject(strResposta);

                    //Caso de algum erro, zere a data de sincronização porque não foi inserido no BD do webservice, abrindo para futura sincronização.
                    if (!resposta.isNull("response") && resposta.getInt("response") < 0) {
                        comanda.setData_sincronizacao(null);
                        throw new Exception("Erro ao executar serviço!");
                    }
                    //Caso tiver sido inserido, devemos atualizar o id centralizado e a data de sincronização, este já foi preenchido anterior.
                    if (comanda.getId_centralizado() == 0) {
                        if (!resposta.isNull("response") && resposta.getInt("response") > 0)
                            comanda.setId_centralizado(resposta.getInt("response"));
                        comandaDAO.atualizar(comanda);
                    }
                }
            }catch (Exception ex){
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