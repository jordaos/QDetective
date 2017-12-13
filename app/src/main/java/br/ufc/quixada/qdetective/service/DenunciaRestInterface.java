package br.ufc.quixada.qdetective.service;

import java.util.List;

import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.DenunciaServer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jordao on 10/12/17.
 */

public interface DenunciaRestInterface {
    @GET("denuncias/{id}")
    Call<DenunciaServer> getDenuncia(@Path("id") int id);

    @GET("denuncias/")
    Call<List<DenunciaServer>> denunciaList();

    @GET("arquivos/{id}")
    Call<String> getFile(@Path("id") int id);

    @POST("denuncias/")
    Call<DenunciaServer> createDenuncia(@Body DenunciaServer denuncia);
}
