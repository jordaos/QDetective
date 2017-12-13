package br.ufc.quixada.qdetective.controller;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.DenunciaServer;
import br.ufc.quixada.qdetective.service.DenunciaService;
import retrofit2.Call;

/**
 * Created by jordao on 08/12/17.
 */

public class DenunciaController {
    DenunciaDAO denunciaDAO;
    DenunciaService denunciaService;

    public DenunciaController(Context context) {
        denunciaDAO = new DenunciaDAO(context);
        denunciaService = new DenunciaService();
    }

    public void addDenuncia(Denuncia denuncia) {
        denunciaDAO.salvar(denuncia);
    }

    public List<Denuncia> getAll() {
        return denunciaDAO.listar();
    }

    public Denuncia getById(int id) {
        return denunciaDAO.buscarPorId(id);
    }

    public void update(Denuncia denuncia) {
        int x = denunciaDAO.atualizar(denuncia);
        Log.d("affecteds", x+"");
    }

    public Call<List<DenunciaServer>> getAllFromServer() {
        return denunciaService.getAll();
    }

    public Call<DenunciaServer> getByIdFromServer(int id) {
        return denunciaService.getById(id);
    }
}
