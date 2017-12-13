package br.ufc.quixada.qdetective.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.controller.DenunciaController;
import br.ufc.quixada.qdetective.entity.CategoriaDenuncia;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.DenunciaServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DenunciaServerListFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    DenunciaController controller;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DenunciaServerListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DenunciaServerListFragment newInstance() {
        DenunciaServerListFragment fragment = new DenunciaServerListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_denuncia_server, container, false);

        controller = new DenunciaController(view.getContext());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            controller.getAllFromServer().enqueue(new Callback<List<DenunciaServer>>() {
                @Override
                public void onResponse(Call<List<DenunciaServer>> call, Response<List<DenunciaServer>> response) {
                    List<DenunciaServer> listDen = response.body();

                    List<Denuncia> list = new ArrayList<Denuncia>();
                    for (DenunciaServer ds : response.body()) {
                        CategoriaDenuncia categoria = CategoriaDenuncia.EQUIPAMENTOS_COMUNICATARIOS;
                        switch (ds.getCategoria()){
                            case "VIAS_PUBLICAS":
                                categoria = CategoriaDenuncia.VIAS_PUBLICAS;
                                break;
                            case "LIMPEZA_URBANA":
                                categoria = CategoriaDenuncia.LIMPEZA_URBANA;
                                break;
                        }
                        Denuncia denuncia = new Denuncia(ds.getId(), ds.getDescricao(), new Date(ds.getData()), ds.getLongitude(), ds.getLatitude(), ds.getUriMidia(), ds.getUsuario(), categoria);
                        list.add(denuncia);
                    }

                    recyclerView.setAdapter(new MyDenunciaServerRecyclerViewAdapter(view, list, mListener));
                }

                @Override
                public void onFailure(Call<List<DenunciaServer>> call, Throwable t) {

                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Denuncia item);
    }
}
