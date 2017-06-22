package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.gabrielfigueira.apppizzaria.model.Entidades.Cliente;
import br.com.gabrielfigueira.apppizzaria.util.DataHelper;
import br.com.gabrielfigueira.apppizzaria.util.WebService;

/**
 * Created by Fabricio on 04/06/2017.
 */

public class ClienteDAO extends DBContext{
    private Context context;
    private SQLiteDatabase db;
    private String response;

    public ClienteDAO(Context context){
        super(context);
        this.context = context;
    }

    public int inserir(Cliente cliente){
        //Definir permissão de escrita
        this.db = getWritableDatabase();

        long id = this.db.insert("cliente", null, preparaContent(cliente));
        return (int)id;
    }

    public int atualizar(Cliente cliente){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(cliente.getId()) };
        long id = this.db.update("cliente", preparaContent(cliente), where, whereArgs);

        return (int)id;

    }

    public int deletar(int id){
        this.db = getWritableDatabase();
        String where = "id = ?";
        String whereArgs[] = new String[]{Integer.toString(id) };
        long ret = db.delete("cliente", where, whereArgs);
        return (int)ret;
    }

    public Cliente pesquisarPorId(int id) throws ParseException {
        try {
            String sql = "SELECT * from cliente WHERE id=?";
            String where[] = new String[]{Integer.toString(id)};

            //Definir permissão de leitura
            this.db = getReadableDatabase();

            //Realizar a consulta
            Cursor c = this.db.rawQuery(sql, where);

            if (c.moveToFirst()) {

                return getClientefromCursor(c);
            } else {
                return null;
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<Cliente> pesquisarPorNome(String nome) throws Exception {
        //Definir permissão de leitura
        this.db = getReadableDatabase();

        String sql = "SELECT * FROM cliente";
        String where[] = new String[]{"%" + nome.toUpperCase() + "%"};

        //Realizar a consulta
        Cursor c = this.db.rawQuery(sql, null);

        List<Cliente> lista = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                lista.add(getClientefromCursor(c));
            }while(c.moveToNext());
            return lista;
        }else{
            return null;
        }
    }

//    public List<Cliente> pesquisarPorNome(String nome){
//        List<Cliente> lista = new ArrayList<>();
//        Cliente c = new Cliente();
//        c.setId(1);
//        c.setNome("Fabricio");
//        lista.add(c);
//        c = new Cliente();
//        c.setId(2);
//        c.setNome("Gabriel");
//        lista.add(c);
//        return lista;
//    }

    private Cliente getClientefromCursor(Cursor cursor) throws ParseException {
        Cliente cliente = new Cliente();
        cliente.setId(cursor.getInt(cursor.getColumnIndex("id")));
        cliente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        cliente.setCpf(cursor.getString(cursor.getColumnIndex("cpf")));
        cliente.setLogradouro(cursor.getString(cursor.getColumnIndex("logradouro")));
        cliente.setNumero(cursor.getInt(cursor.getColumnIndex("numero")));
        cliente.setBairro(cursor.getString(cursor.getColumnIndex("bairro")));
        cliente.setCep(cursor.getString(cursor.getColumnIndex("cep")));
        cliente.setCidade( cursor.getString(cursor.getColumnIndex("cidade")));
        cliente.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
        cliente.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        cliente.setId_centralizado(cursor.getInt(cursor.getColumnIndex("id_centralizado")));
        cliente.setData_sincronizacao(DataHelper.strToDate(cursor.getString(cursor.getColumnIndex("data_sincronizacao"))));

        return cliente;
    }

    private ContentValues preparaContent(Cliente cliente){
        ContentValues cv = new ContentValues();
        cv.put("nome", cliente.getNome());
        cv.put("cpf", cliente.getCpf());
        cv.put("logradouro", cliente.getLogradouro());
        cv.put("numero", cliente.getNumero());
        cv.put("bairro", cliente.getBairro());
        cv.put("cep", cliente.getCep());
        cv.put("cidade", cliente.getCidade());
        cv.put("telefone", cliente.getTelefone());
        cv.put("email", cliente.getEmail());
        cv.put("id_centralizado", cliente.getId_centralizado());
        cv.put("data_sincronizacao", DataHelper.dateToStr(cliente.getData_sincronizacao()));
        return cv;
    }
}
