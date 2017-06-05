package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Comanda;

public class ComandaDAO extends SQLiteOpenHelper {
    private final String sql_create =
            "create table comanda("+
                    "id integer primary key AUTOINCREMENT,"+
                    "mesa text null, " +
                    "data_hora_abertura text null, " +
                    "data_sincronizacao text null, " +
                    "data_hora_finalizacao text null, " +
                    "desconto real null, " +
                    "id_centralizado int null, "+
                    "cliente_id int null"+
                    ");";
    private SQLiteDatabase db;

    public ComandaDAO(Context context){
        super(context,"apppizzaria.db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL(sql_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE comanda;");
        db.execSQL(sql_create);
    }

    public int inserir(Comanda comanda){
        //Definir permissão de escrita
        this.db = getWritableDatabase();

        long id = this.db.insert("comanda", null, preparaContent(comanda));

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
            String sql = "SELECT comanda.*, cliente.nome as 'cliente_nome' FROM comanda join cliente on comanda.cliente_id = cliente.id WHERE id=?";
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
    public List<Comanda> pesquisarPorCliente(String cliente_nome) throws ParseException {

        String sql = "SELECT comanda.*, cliente.nome as 'cliente_nome' FROM comanda join cliente on comanda.cliente_id = cliente.id WHERE cliente.nome LIKE ?";
        String where[] = new String[]{"%" + cliente_nome.toUpperCase() + "%"};

        //Definir permissão de leitura
        this.db = getReadableDatabase();

        //Realizar a consulta
        Cursor c = this.db.rawQuery(sql, where);

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
        comanda.setData_hora_abertura(DataHelper.strToTimestamp(cursor.getString(cursor.getColumnIndex("data_hora_abertura"))));
        comanda.setData_hora_finalizacao(DataHelper.strToTimestamp(cursor.getString(cursor.getColumnIndex("data_hora_finalizacao"))));
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
        cv.put("data_hora_abertura", comanda.getData_hora_abertura().toString());
        cv.put("data_hora_finalizacao", comanda.getData_hora_finalizacao().toString());
        cv.put("desconto", comanda.getDesconto());
        cv.put("id_centralizado", comanda.getId_centralizado());
        cv.put("data_sincronizacao", comanda.getData_sincronizacao().toString());
        cv.put("cliente_id", comanda.getCliente() != null? comanda.getCliente().getId(): null);
        return cv;
    }
}
