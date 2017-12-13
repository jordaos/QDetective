package br.ufc.quixada.qdetective.fragments;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.rengwuxian.materialedittext.Colors;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import at.markushi.ui.CircleButton;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.controller.DenunciaController;
import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.CategoriaDenuncia;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.ListViewType;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewDenunciaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewDenunciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDenunciaFragment extends Fragment {
    private static final String ARG_PARAM1 = "idDenuncia";

    private OnFragmentInteractionListener mListener;

    private DenunciaController controller;
    private Denuncia denuncia;

    private LocationManager locationManager;

    private final static int CAMERA_RQ = 6969;

    private double latitude;
    private double longitude;
    private String mediaFileSrc;

    CircleButton takePhoto;
    CircleButton recVideo;
    Button newDenuncia;
    MaterialEditText nomeEditText;
    RadioGroup group;
    MaterialEditText descricaoEditText;

    public NewDenunciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewDenunciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDenunciaFragment newInstance() {
        NewDenunciaFragment fragment = new NewDenunciaFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idDenuncia ID da denúncia, quando for editar
     * @return A new instance of fragment NewDenunciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewDenunciaFragment newInstance(int idDenuncia) {
        NewDenunciaFragment fragment = new NewDenunciaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idDenuncia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new DenunciaController(getActivity());
        if (getArguments() == null) {
            return;
        }

        int id = getArguments().getInt(ARG_PARAM1);
        denuncia = controller.getById(id);
    }

    public void initEdition(View view) {
       nomeEditText.setText(denuncia.getUsuario());
       descricaoEditText.setText(denuncia.getDescricao());
       mediaFileSrc = denuncia.getUriMidia();
       latitude = denuncia.getLatitude();
       longitude = denuncia.getLongitude();

       switch (denuncia.getCategoria()){
           case LIMPEZA_SANEAMENTO:
               group.check(R.id.radio_limpeza_saneamento);
               break;
           case VIAS_PUBLICAS:
               group.check(R.id.radio_vias_publicas);
               break;
           case EQUIPAMENTOS:
               group.check(R.id.radio_equipamentos);
               break;
       }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_denuncia, container, false);

        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        takePhoto = (CircleButton) view.findViewById(R.id.take_photo);
        recVideo = (CircleButton) view.findViewById(R.id.rec_video);
        newDenuncia = (Button) view.findViewById(R.id.newDenunciaButton);
        nomeEditText = (MaterialEditText) view.findViewById(R.id.nomeEditText);
        group = (RadioGroup) view.findViewById(R.id.group_categoria);
        descricaoEditText = (MaterialEditText) view.findViewById(R.id.descricaoEditText);

        if(denuncia != null){
            initEdition(view);
        }else {
            getLocationManager();
        }

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialCamera(NewDenunciaFragment.this)
                        .saveDir(path)
                        .stillShot() // launches the Camera in stillshot mode
                        .start(CAMERA_RQ);
            }
        });

        recVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialCamera(NewDenunciaFragment.this)
                        .saveDir(path)
                        .showPortraitWarning(false)
                        .countdownMinutes(.1f)
                        .countdownImmediately(false)
                        .start(CAMERA_RQ);
            }
        });

        final CategoriaDenuncia[] categoria = {CategoriaDenuncia.EQUIPAMENTOS};
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_equipamentos:
                        categoria[0] = CategoriaDenuncia.EQUIPAMENTOS;
                        break;
                    case R.id.radio_limpeza_saneamento:
                        categoria[0] = CategoriaDenuncia.LIMPEZA_SANEAMENTO;
                        break;
                    case R.id.radio_vias_publicas:
                        categoria[0] = CategoriaDenuncia.VIAS_PUBLICAS;
                        break;
                }
            }
        });

        newDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getText().toString();
                String descricao = descricaoEditText.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();

                if (mediaFileSrc == null) {
                    Toast.makeText(getActivity(), "Registre uma foto ou vídeo.", Toast.LENGTH_LONG).show();
                    return;
                }

                Denuncia newDenuncia = new Denuncia(descricao, currentTime, longitude, latitude, mediaFileSrc, nome, categoria[0]);

                if(denuncia == null) {
                    controller.addDenuncia(newDenuncia);
                }else {
                    newDenuncia.setId(denuncia.getId());
                    controller.update(newDenuncia);
                }
                Toast.makeText(getActivity(), "Denúncia cadastrada com sucesso", Toast.LENGTH_LONG).show();

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, DenunciaListFragment.newInstance(1)).commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                mediaFileSrc = data.getDataString();
            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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

    private void getLocationManager() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    1);
            return;
        }

        Listener listener = new Listener();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        long tempoAtualizacao = 0;
        float distancia = 0;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, tempoAtualizacao, distancia, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tempoAtualizacao, distancia, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    getLocationManager();
                } else {
                    Toast.makeText(getActivity(), "Sem permissão para uso de gps ou rede.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {    }
        @Override
        public void onProviderEnabled(String provider) {  }
        @Override
        public void onProviderDisabled(String provider) { }
    }
}
