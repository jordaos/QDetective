package br.ufc.quixada.qdetective.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.qdetective.entity.CategoriaDenuncia;
import br.ufc.quixada.qdetective.entity.Denuncia;

/**
 * Created by jordao on 08/12/17.
 */

public class DenunciaDAO {
    private DataBaseHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public DenunciaDAO(Context context) {
        helper = DataBaseHelper.getInstance(context);
    }

    public List<Denuncia> listar() {
        db = helper.getReadableDatabase();
        cursor = db.query(DataBaseHelper.DenunciaTable.NOME_TABELA,
                DataBaseHelper.DenunciaTable.COLUNAS,
                null, null, null, null,
                DataBaseHelper.DenunciaTable.DATA + " DESC");

        List<Denuncia> lista = new ArrayList<Denuncia>();

        while (cursor.moveToNext()) {
            Denuncia denuncia = criarDenuncia(cursor);
            lista.add(denuncia);
        }
        db.close();
        return lista;
    }

    public Denuncia buscarPorId(long id) {
        db = helper.getReadableDatabase();
        String selecao = DataBaseHelper.DenunciaTable._ID + " = ?";
        String[] argumentos = {String.valueOf(id)};

        cursor = db.query(DataBaseHelper.DenunciaTable.NOME_TABELA,
                DataBaseHelper.DenunciaTable.COLUNAS,
                selecao,
                argumentos,
                null, null, null);

        cursor.moveToFirst();
        Denuncia Denuncia = criarDenuncia(cursor);
        db.close();
        return Denuncia;
    }

    public long salvar(Denuncia denuncia) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.DenunciaTable.CATEGORIA, denuncia.getCategoria().ordinal());
        values.put(DataBaseHelper.DenunciaTable.DATA, denuncia.getData().getTime());
        values.put(DataBaseHelper.DenunciaTable.DESCRICAO, denuncia.getDescricao());
        values.put(DataBaseHelper.DenunciaTable.LATITUDE, denuncia.getLatitude());
        values.put(DataBaseHelper.DenunciaTable.LONGITUDE, denuncia.getLongitude());
        values.put(DataBaseHelper.DenunciaTable.USUARIO, denuncia.getUsuario());
        values.put(DataBaseHelper.DenunciaTable.URL_MIDIA, denuncia.getUriMidia());

        db = helper.getWritableDatabase();
        long idNovoRegistro = db.insert(DataBaseHelper.DenunciaTable.NOME_TABELA, null, values);
        db.close();
        return idNovoRegistro;
    }


    public int excluirPorId(long id) {
        String selecao = DataBaseHelper.DenunciaTable._ID + " = ?";
        String[] argumentos = {String.valueOf(id)};

        db = helper.getWritableDatabase();
        int linhasApagadas  = db.delete(DataBaseHelper.DenunciaTable.NOME_TABELA, selecao, argumentos);
        db.close();
        return linhasApagadas;
    }

    public int excluir(Denuncia Denuncia) {
        return this.excluirPorId(Denuncia.getId());
    }


    private Denuncia criarDenuncia(Cursor cursor) {
        Integer _id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable._ID));
        String usuario = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.USUARIO));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.DESCRICAO));
        String url_midia = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.URL_MIDIA));
        Double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.LONGITUDE));
        int categoria = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.CATEGORIA));
        Date data = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseHelper.DenunciaTable.DATA)));
        Log.d("desc", descricao);
        Log.d("midia", url_midia);
        Log.d("lat", latitude+"");
        Log.d("long", longitude+"");
        Log.d("cat", categoria+"");
        Log.d("data", cursor.getLong(7)+"");

        Denuncia denuncia = new Denuncia(_id, descricao, data, longitude, latitude, url_midia, usuario, CategoriaDenuncia.values()[categoria]);
        return denuncia;
    }

    public void close() {
        helper.close();
        db = null;
    }
}
