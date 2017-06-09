package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.ComandaProduto;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

public class ComandaProdutoDAO extends DBContext {
    private SQLiteDatabase db;

    public ComandaProdutoDAO(Context context){
        super(context);
    }

    public int inserir(ComandaProduto produto){
        //Definir permissão de escrita
        this.db = getWritableDatabase();

        long id = this.db.insert("comanda_produto", null, preparaContent(produto));

        return (int)id;

    }

    public int atualizar(ComandaProduto produto){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(produto.getId()) };
        long id = this.db.update("comanda_produto", preparaContent(produto), where, whereArgs);
        return (int)id;

    }

    public int deletar(int id){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(id) };
        long ret = db.delete("comanda_produto", where, whereArgs);
        return (int)ret;
    }

    public int deletar_por_comanda(int comanda_id){
        this.db = getWritableDatabase();
        String where = "comanda_id = ?";
        String whereArgs[] = new String[]{Integer.toString(comanda_id) };
        long ret = db.delete("comanda_produto", where, whereArgs);
        return (int)ret;
    }

    public ComandaProduto pesquisarPorId(int id) throws ParseException {
        try {
            String sql = "SELECT comanda_produto.*, produto.descricao as 'produto_descricao' FROM comanda_produto left join produto on comanda_produto.produto_id = produto.id WHERE comanda_produto.id=?";
            String where[] = new String[]{Integer.toString(id)};

            //Definir permissão de leitura
            this.db = getReadableDatabase();

            //Realizar a consulta
            Cursor c = this.db.rawQuery(sql, where);

            if (c.moveToFirst()) {

                return getComandaProdutofromCursor(c);
            } else {
                return null;
            }
        }catch (Exception ex){
            throw ex;
        }
    }
    public List<ComandaProduto> pesquisarPorProduto(int comanda_id, String produto_descricao) throws ParseException {
        //Definir permissão de leitura
        this.db = getReadableDatabase();

//        String sql = "SELECT comanda_produto.*, produto.nome as 'produto_descricao' FROM comanda_produto left join produto on comanda_produto.produto_id = produto.id WHERE produto.descricao like ?";
        String sql = "SELECT comanda_produto.*, comanda_produto.produto_id as 'produto_descricao' FROM comanda_produto where comanda_id = ?";
        String where[] = new String[]{/*"%" + produto_descricao.toUpperCase() + "%"*/ Integer.toString(comanda_id).trim() };

        //Realizar a consulta
        Cursor c = this.db.rawQuery(sql, where);

        List<ComandaProduto> lista = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                lista.add(getComandaProdutofromCursor(c));
            }while(c.moveToNext());
            return lista;
        }else{
            return null;
        }
    }

    private ComandaProduto getComandaProdutofromCursor(Cursor cursor) throws ParseException {
        ComandaProduto produto = new ComandaProduto();
        produto.setId(cursor.getInt(cursor.getColumnIndex("id")));
        produto.setQuantidade(Double.parseDouble(cursor.getString(cursor.getColumnIndex("quantidade"))));
        produto.setData_hora_entrega(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_hora_entrega"))));

        Produto pro1 = new Produto();
        pro1.setId(cursor.getInt(cursor.getColumnIndex("produto_id")));
        pro1.setDescricao(cursor.getString(cursor.getColumnIndex("produto_descricao")));
        produto.setProduto(pro1);

        Comanda com1 = new Comanda();
        com1.setId(cursor.getInt(cursor.getColumnIndex("comanda_id")));
        produto.setComanda(com1);
        return produto;
    }

    private ContentValues preparaContent(ComandaProduto produto){
        ContentValues cv = new ContentValues();
        cv.put("quantidade", produto.getQuantidade());
        cv.put("data_hora_entrega", DataHelper.dateToStr(produto.getData_hora_entrega()));
        cv.put("comanda_id", produto.getComanda() != null? produto.getComanda().getId(): null);
        cv.put("produto_id", produto.getProduto() != null? produto.getProduto().getId(): null);
        return cv;
    }
}
