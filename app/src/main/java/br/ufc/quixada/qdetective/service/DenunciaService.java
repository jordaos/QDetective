package br.ufc.quixada.qdetective.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.DenunciaServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jordao on 10/12/17.
 */

public class DenunciaService {
    public static final String BASE_URL = "http://192.168.1.15:8080/QDetective/rest/";

    private DenunciaRestInterface apiService;

    public DenunciaService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(DenunciaRestInterface.class);
    }

    public Call<List<DenunciaServer>> getAll() {
        Call<List<DenunciaServer>> call = apiService.denunciaList();
        return call;
    }

    public Call<DenunciaServer> getById(int id) {
        Call<DenunciaServer> call = apiService.getDenuncia(id);
        return call;
    }
}
