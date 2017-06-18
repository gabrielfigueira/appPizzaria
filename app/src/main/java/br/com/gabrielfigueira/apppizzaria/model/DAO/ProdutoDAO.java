package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Produto;
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

/**
 * Created by Fabricio on 08/06/2017.
 */

public class ProdutoDAO extends DBContext {
    private SQLiteDatabase db;


    public ProdutoDAO (Context context){super(context);}

    public int inserir (Produto produto){
        this.db = getWritableDatabase();
        long id =  this.db.insert("produto", null, preparaContent(produto));

        return (int) id;
    }
    public int atualizar  (Produto produto){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(produto.getId())};
        long id =  this.db.update("produto", preparaContent(produto), where, whereArgs);
        return (int)id;

    }
    public int deletaProduto (int produto_id){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(produto_id)};
        long ret = this.db.delete("produto", where,whereArgs);
        return (int) ret;
    }

    public Produto pesquisarPorId(int id) throws ParseException {
        try {
            String sql = "SELECT * from produto WHERE id =?";
            String where [] = new String[]{Integer.toString(id)};

            this.db = getWritableDatabase();
            Cursor c = this.db.rawQuery(sql, where);

            if (c.moveToFirst()){
                return getProdutofromCursor(c);
            }else {
                return null;
            }
        }catch (Exception ex){
            throw ex;
        }
    }


    public List<Produto>pesquisarPorDescricao(String descricao) throws ParseException{
        this.db =  getReadableDatabase();

        String sql =  "SELECT * FROM produto";
//        String  where [] =  new String[]{"%" + descricao.toUpperCase() + "%"};

        Cursor c = this.db.rawQuery(sql,null);

        List<Produto> lista = new ArrayList<>();
        if (c.moveToFirst()){
            do{
                lista.add(getProdutofromCursor(c));
            }while (c.moveToNext());
            return lista;
        }else {
            return lista;
        }
    }
    private Produto getProdutofromCursor(Cursor cursor) throws ParseException {
        Produto produto =  new Produto();
        produto.setId(cursor.getInt(cursor.getColumnIndex("id")));
        produto.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        produto.setData_sincronizacao(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_sincronizacao"))));
        produto.setId_centralizado(cursor.getInt(cursor.getColumnIndex("id_centralizado")));

        return produto;
    }

    private ContentValues preparaContent(Produto produto) {
        ContentValues cv =  new ContentValues();
        cv.put("descricao",produto.getDescricao());
        cv.put("data_sincronizacao", DataHelper.dateToStr(produto.getData_sincronizacao()));
        cv.put("id_centralizado", produto.getId_centralizado());

        return cv;
    }
}

