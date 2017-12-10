package br.ufc.quixada.qdetective.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;

import at.markushi.ui.CircleButton;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.Denuncia;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DenunciaDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DenunciaDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DenunciaDetailsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idDenuncia";

    // TODO: Rename and change types of parameters
    private int idDenuncia;

    private DenunciaDAO denunciaDAO;

    private OnFragmentInteractionListener mListener;

    public DenunciaDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idDenuncia ID da denúncia.
     * @return A new instance of fragment DenunciaDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DenunciaDetailsFragment newInstance(int idDenuncia) {
        DenunciaDetailsFragment fragment = new DenunciaDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idDenuncia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            Toast.makeText(getActivity(), "Denúncia não encontrada", Toast.LENGTH_LONG).show();
            return;
        }
        idDenuncia = getArguments().getInt(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_denuncia_details, container, false);

        denunciaDAO = new DenunciaDAO(view.getContext());
        final Denuncia denuncia = denunciaDAO.buscarPorId(idDenuncia);
        String filename = denuncia.getUriMidia().substring(denuncia.getUriMidia().lastIndexOf("/") + 1);

        TextView detalhesTextView = (TextView) view.findViewById(R.id.descricaoDetailsDenuncia);
        TextView usuarioTextView = (TextView) view.findViewById(R.id.usuarioDetailsDenuncia);
        TextView dataTextView = (TextView) view.findViewById(R.id.dataDetailsDenuncia);
        TextView categoriaTextView = (TextView) view.findViewById(R.id.categoriaDetailsDenuncia);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageDetaisDenuncia);
        final VideoView videoView = (VideoView) view.findViewById(R.id.videoDetaisDenuncia);
        LinearLayout videoPlayer = (LinearLayout) view.findViewById(R.id.videoPlayer);

        detalhesTextView.setText(denuncia.getDescricao());
        usuarioTextView.setText(denuncia.getUsuario());
        String data = DateFormat.getDateInstance().format(denuncia.getData());
        dataTextView.setText(data);
        String categoria = "";
        switch (denuncia.getCategoria()){
            case EQUIPAMENTOS:
                categoria = "Equipamentos comunitários";
                break;
            case VIAS_PUBLICAS:
                categoria = "Vias públicas de acesso";
                break;
            case LIMPEZA_SANEAMENTO:
                categoria = "Limpeza urbana e saneamento";
                break;
        }
        categoriaTextView.setText(categoria);

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, filename);
        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
        if (ext.equalsIgnoreCase("jpg")) {
            imageView.setVisibility(View.VISIBLE);
            try {
                // Make sure the Pictures directory exists.
                path.mkdirs();

                file.createNewFile();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (ext.equalsIgnoreCase("mp4")) {
            CircleButton playVideo = (CircleButton) view.findViewById(R.id.play_video);
            playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoView.start();
                }
            });
            videoPlayer.setVisibility(View.VISIBLE);
            videoView.setVideoPath(file.getAbsolutePath());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(view.getContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();
        final MapView mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onEnterAmbient(null);
        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng coordinates = new LatLng(denuncia.getLatitude(), denuncia.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(coordinates));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                mapView.onResume();
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
