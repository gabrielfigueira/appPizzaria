package br.com.gabrielfigueira.apppizzaria.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.R;
import br.com.gabrielfigueira.apppizzaria.adapter.ComandaProdutoAdapter;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaDAO;
import br.com.gabrielfigueira.apppizzaria.model.DAO.ComandaProdutoDAO;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.ComandaProduto;

public class ComandaCorpoController extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnClickListener {
    private EditText edtMesa;
    private EditText edtCliente_nome;
    private Button btnEditar;
    private FloatingActionButton btnCadastrar;
    private CheckBox chkSomentePendentes;
    private MenuItem mnuDeletar;
    private MenuItem mnuEntrega;

    private ListView lstComandaProduto;

    private int id;
    private Comanda comanda;

    private List<ComandaProduto> listaComandasSelecionadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comanda_corpo);

        edtMesa = (EditText)findViewById(R.id.edtMesa);
        edtMesa.setFocusable(false);
        edtCliente_nome = (EditText)findViewById(R.id.edtCliente_nome);
        edtCliente_nome.setFocusable(false);

        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(this);

        btnCadastrar = (FloatingActionButton) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);

        lstComandaProduto = (ListView)findViewById(R.id.lstProdutoComanda);
        lstComandaProduto.setOnItemClickListener(this);
        lstComandaProduto.setOnItemLongClickListener(this);

        chkSomentePendentes = (CheckBox)findViewById(R.id.chkSomentePendentes);
        chkSomentePendentes.setOnClickListener(this);
        chkSomentePendentes.setChecked(false);

        setTitle("Comanda");

        Intent it = getIntent();
        if (it != null){
            try{
                id = it.getIntExtra("id", 0);
            }catch(Exception e){
                Log.e("ERRO", e.getMessage());
            }
        }

        listaComandasSelecionadas = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comanda_corpo_menu, menu);

        mnuEntrega = menu.findItem(R.id.mnuEntrega);
        mnuDeletar = menu.findItem(R.id.mnuDeletar);

        mnuEntrega.setVisible(false);
        mnuDeletar.setVisible(false);
        return true;
    }

    private void atualizaTela() throws Exception {
        comanda = new ComandaDAO(this).pesquisarPorId(id, false);
        if (comanda == null){
            comanda = new Comanda();
        }
        edtMesa.setText(comanda.getMesa());
        edtCliente_nome.setText(comanda.getCliente().getNome());
        listaComandasSelecionadas.clear();
        lstComandaProduto.setBackgroundColor(Color.TRANSPARENT);

        if (mnuEntrega != null)
            mnuEntrega.setVisible(false);
        if (mnuDeletar != null)
            mnuDeletar.setVisible(false);

        preencherListView();
    }

    private void preencherListView() throws ParseException {
        List<ComandaProduto> lista = new ComandaProdutoDAO(this).pesquisar(comanda.getId(), chkSomentePendentes.isChecked());
        ComandaProdutoAdapter adp = new ComandaProdutoAdapter(this, lista);
        lstComandaProduto.setAdapter(adp);
        adp.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            atualizaTela();

        } catch (Exception ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Pizzaria App");
            dlg.setMessage(ex.getMessage());
            dlg.setCancelable(false);
            dlg.setPositiveButton("OK", null);
            dlg.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        final ComandaProduto produto = (ComandaProduto)adapterView.getItemAtPosition(position);
        boolean achou = false;
        if (listaComandasSelecionadas.isEmpty()) {
            Intent it = new Intent(getApplicationContext(), ComandaProdutoFormController.class);
            it.putExtra("id", produto.getId());
            it.putExtra("comanda_id", comanda.getId());
            startActivity(it);
        }else {
            for(ComandaProduto pro : listaComandasSelecionadas){
                if (pro.getId() == produto.getId()){
                    listaComandasSelecionadas.remove(pro);
                    achou = true;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    break;
                }
            }
            if (!achou){
                listaComandasSelecionadas.add(produto);
                view.setSelected(true);
                view.setBackgroundColor(Color.LTGRAY);
            }

            mnuEntrega.setVisible(false);
            mnuDeletar.setVisible(false);

            if (listaComandasSelecionadas.size() >= 1)
                mnuEntrega.setVisible(true);
            if (listaComandasSelecionadas.size() == 1)
                mnuDeletar.setVisible(true);

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnEditar){
            Intent it = new Intent(getApplicationContext(),ComandaFormController.class);
            it.putExtra("id", comanda.getId());
            startActivity(it);
        }else if(view.getId() == R.id.btnCadastrar) {
            Intent it = new Intent(getApplicationContext(),ComandaProdutoFormController.class);
            it.putExtra("id", 0);
            it.putExtra("comanda_id", comanda.getId());
            startActivity(it);
        }else if (view.getId() == R.id.chkSomentePendentes){
            onResume();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final ComandaProduto produto = (ComandaProduto)parent.getItemAtPosition(position);

        boolean achou = false;
        for(ComandaProduto pro : listaComandasSelecionadas){
            if (pro.getId() == produto.getId()){
                listaComandasSelecionadas.remove(pro);
                achou = true;
                view.setBackgroundColor(Color.TRANSPARENT);
                break;
            }
        }
        if (!achou){
            listaComandasSelecionadas.add(produto);
            view.setBackgroundColor(Color.LTGRAY);
        }
        mnuEntrega.setVisible(false);
        mnuDeletar.setVisible(false);

        if (listaComandasSelecionadas.size() >= 1)
            mnuEntrega.setVisible(true);
        if (listaComandasSelecionadas.size() == 1)
            mnuDeletar.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        if (menuItem.getItemId() == R.id.mnuDeletar){
            final ComandaProduto produto = listaComandasSelecionadas.get(0);

            dlg.setTitle("Pizzaria App");
            dlg.setMessage("Tem certeza que deseja deletar o produto da comanda " + (produto.getProduto() != null? produto.getProduto().getDescricao(): "") + "?");
            dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ComandaProdutoDAO(getApplicationContext()).deletar(produto.getId());
                    onResume();
                }
            });
            dlg.setNegativeButton("NÃO", null);
            dlg.show();
        }else {
            dlg.setTitle("Pizzaria App");
            dlg.setMessage("Tem certeza que deseja entregar produtos selecionados?");
            dlg.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ComandaProdutoDAO dao = new ComandaProdutoDAO(getApplicationContext());
                    try {
                        for(ComandaProduto pro : listaComandasSelecionadas){
                            dao.entregarProduto(pro.getId());
                        }
                        onResume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dlg.setNegativeButton("NÃO", null);
            dlg.show();


        }
        return false;
    }
}