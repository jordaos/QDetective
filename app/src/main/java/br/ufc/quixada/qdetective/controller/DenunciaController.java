package br.ufc.quixada.qdetective.controller;

import android.content.Context;

import java.util.List;

import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.Denuncia;

/**
 * Created by jordao on 08/12/17.
 */

public class DenunciaController {
    DenunciaDAO denunciaDAO;

    public DenunciaController(Context context) {
        denunciaDAO = new DenunciaDAO(context);
    }

    public void addDenuncia(Denuncia denuncia) {
        denunciaDAO.salvar(denuncia);
    }

    public List<Denuncia> getAll() {
        return denunciaDAO.listar();
    }
}
