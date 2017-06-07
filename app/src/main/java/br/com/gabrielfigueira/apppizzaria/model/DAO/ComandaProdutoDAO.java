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
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;

public class ComandaProdutoDAO extends DBContext {
    private SQLiteDatabase db;

    public ComandaProdutoDAO(Context context){
        super(context);
    }

    public int inserir(Comanda comanda){
        //Definir permissão de escrita
        this.db = getWritableDatabase();

        long id = this.db.insert("comanda_produto", null, preparaContent(comanda));

        return (int)id;

    }

    public int atualizar(Comanda comanda){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(comanda.getId()) };
        long id = this.db.update("comanda", preparaContent(comanda), where, whereArgs);
        return (int)id;

    }

    public int deletar(int id){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(id) };
        long ret = db.delete("comanda", where, whereArgs);
        return (int)ret;
    }

    public Comanda pesquisarPorId(int id) throws ParseException {
        try {
            String sql = "SELECT comanda_produto.*, produto.descricao as 'produto_descricao' FROM comanda_produto left join produto on comanda_produto.produto_id = produto.id WHERE comanda_produto.id=?";
            String where[] = new String[]{Integer.toString(id)};

            //Definir permissão de leitura
            this.db = getReadableDatabase();

            //Realizar a consulta
            Cursor c = this.db.rawQuery(sql, where);

            if (c.moveToFirst()) {

                return getComandafromCursor(c);
            } else {
                return null;
            }
        }catch (Exception ex){
            throw ex;
        }
    }
    public List<Comanda> pesquisarPorProduto(String produto_descricao) throws ParseException {
        //Definir permissão de leitura
        this.db = getReadableDatabase();

//        String sql = "SELECT comanda_produto.*, produto.nome as 'produto_descricao' FROM comanda_produto left join produto on comanda_produto.produto_id = produto.id WHERE produto.descricao like ?";
        String sql = "SELECT comanda_produto.*, comanda_produto.produto_id as 'produto_descrica' FROM comanda_produto";
        String where[] = new String[]{"%" + produto_descricao.toUpperCase() + "%"};

        //Realizar a consulta
        Cursor c = this.db.rawQuery(sql, null);

        List<Comanda> lista = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                lista.add(getComandafromCursor(c));
            }while(c.moveToNext());
            return lista;
        }else{
            return null;
        }
    }

    private Comanda getComandafromCursor(Cursor cursor) throws ParseException {
        Comanda comanda = new Comanda();
        comanda.setId(cursor.getInt(cursor.getColumnIndex("id")));
        comanda.setMesa(cursor.getString(cursor.getColumnIndex("mesa")));
        comanda.setData_hora_abertura(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_hora_abertura"))));
        comanda.setData_hora_finalizacao(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_hora_finalizacao"))));
        comanda.setMesa(cursor.getString(cursor.getColumnIndex("mesa")));
        comanda.setDesconto(cursor.getDouble(cursor.getColumnIndex("desconto")));
        comanda.setId_centralizado(cursor.getInt(cursor.getColumnIndex("id_centralizado")));
        comanda.setData_sincronizacao(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_sincronizacao"))));

        Cliente cliente = new Cliente();
        cliente.setId(cursor.getInt(cursor.getColumnIndex("cliente_id")));
        cliente.setNome(cursor.getString(cursor.getColumnIndex("cliente_nome")));
        comanda.setCliente(cliente);
        return comanda;
    }

    private ContentValues preparaContent(Comanda comanda){
        ContentValues cv = new ContentValues();
        cv.put("mesa", comanda.getMesa());
        cv.put("data_hora_abertura", DataHelper.dateToStr(comanda.getData_hora_abertura()));
        cv.put("data_hora_finalizacao", DataHelper.dateToStr(comanda.getData_hora_finalizacao()));
        cv.put("desconto", comanda.getDesconto());
        cv.put("id_centralizado", comanda.getId_centralizado());
        cv.put("data_sincronizacao", DataHelper.dateToStr(comanda.getData_sincronizacao()));
        cv.put("cliente_id", comanda.getCliente() != null? comanda.getCliente().getId(): null);
        return cv;
    }
}
