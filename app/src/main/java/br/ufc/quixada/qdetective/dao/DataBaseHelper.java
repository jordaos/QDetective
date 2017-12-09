package br.ufc.quixada.qdetective.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jordao on 08/12/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String NOME_BD = "qDetectiveDB";
    private static final int VERSAO_BD = 1;
    private static DataBaseHelper instance;

    public static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DenunciaTable.CRIAR_TABELA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DenunciaTable.EXCLUIR_TABELA);
        onCreate(db);
    }

    public static class DenunciaTable {

        public static final String NOME_TABELA = "denuncia";
        public static final String _ID = "_id";
        public static final String USUARIO = "usuario";
        public static final String DESCRICAO = "descricao";
        public static final String DATA = "data";
        public static final String URL_MIDIA = "url_midia";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CATEGORIA = "categoria";

        public static final String[] COLUNAS =
                new String[]{_ID, USUARIO, DESCRICAO, DATA, URL_MIDIA, LATITUDE, LONGITUDE, CATEGORIA};

        public static final String CRIAR_TABELA =
                "CREATE TABLE IF NOT EXISTS " + NOME_TABELA + "(" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        USUARIO + " TEXT, " +
                        DESCRICAO + " TEXT, " +
                        URL_MIDIA + " TEXT, " +
                        LATITUDE + " REAL, " +
                        LONGITUDE + " REAL, " +
                        CATEGORIA + " INTEGER, " +
                        DATA + " DATE);";

        public static final String EXCLUIR_TABELA = "DROP TABLE " + NOME_TABELA + ";";
    }
}
