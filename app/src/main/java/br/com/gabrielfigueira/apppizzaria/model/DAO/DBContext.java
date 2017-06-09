package br.com.gabrielfigueira.apppizzaria.model.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fabricio on 07/06/2017.
 */

public class DBContext extends SQLiteOpenHelper {
    private final String sql_comanda_create =
            "create table comanda("+
                    "id integer primary key AUTOINCREMENT,"+
                    "mesa text null, " +
                    "data_hora_abertura text null, " +
                    "data_sincronizacao text null, " +
                    "data_hora_finalizacao text null, " +
                    "desconto real null, " +
                    "id_centralizado integer null, "+
                    "cliente_id integer null"+
                    ");";
    private final String sql_comanda_produto_create =
            "create table comanda_produto("+
                    "id integer primary key AUTOINCREMENT,"+
                    "quantidade real null, " +
                    "data_hora_entrega text null, " +
                    "comanda_id integer null,"+
                    "produto_id integer null"+
                    ");";

    private final String sql_cliente_create =
            "create table cliente("+
                    "id integer primary key AUTOINCREMENT,"+
                    "nome text null, " +
                    "cpf text null, " +
                    "logradouro text null, " +
                    "numero integer null, " +
                    "bairro text null, " +
                    "cep text null, " +
                    "cidade text null, " +
                    "telefone text null, " +
                    "email text null, " +
                    "data_cadastro text null, " +
                    "data_sincronizacao text null, " +
                    "id_centralizado integer null"+
                    ");";

    /*PEGAR COM OS DEMAIS MEMBROS DO GRUPO*/
    private final String sql_produto_create = "create table produto(id integer primary key AUTOINCREMENT, descricao text null);";

    private SQLiteDatabase db;
    public DBContext(Context context){
        super(context,"apppizzaria.db",null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_produto_create);
        db.execSQL(sql_cliente_create);
        db.execSQL(sql_comanda_create);
        db.execSQL(sql_comanda_produto_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoOld, int versaoNew) {
        switch (versaoNew){
            case 1:
                db.execSQL(sql_produto_create);
                db.execSQL(sql_cliente_create);
                db.execSQL(sql_comanda_create);
                db.execSQL(sql_comanda_produto_create);
                break;
            default:
                if (versaoOld >= 5)
                    db.execSQL("DROP TABLE IF EXISTS comanda;");

                if (versaoOld >= 6)
                    db.execSQL("DROP TABLE IF EXISTS comanda_produto;");

                /*PEGAR AS DEMAIS PARTES COM OS DEMAIS MEMBROS DO GRUPO*/
                if (versaoOld >= 5)
                    db.execSQL("DROP TABLE IF EXISTS cliente;");

                if (versaoOld >= 6)
                    db.execSQL("DROP TABLE IF EXISTS produto;");

                db.execSQL(sql_cliente_create);
                db.execSQL(sql_produto_create);
                db.execSQL(sql_comanda_create);
                db.execSQL(sql_comanda_produto_create);
                break;
        }
    }
}
