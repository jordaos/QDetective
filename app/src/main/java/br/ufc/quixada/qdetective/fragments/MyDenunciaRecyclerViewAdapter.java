package br.ufc.quixada.qdetective.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.controller.DenunciaController;
import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.DenunciaServer;
import br.ufc.quixada.qdetective.entity.ListViewType;
import br.ufc.quixada.qdetective.fragments.DenunciaListFragment.OnListFragmentInteractionListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Denuncia} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDenunciaRecyclerViewAdapter extends RecyclerView.Adapter<MyDenunciaRecyclerViewAdapter.ViewHolder> {

    private final List<Denuncia> mValues;
    private final OnListFragmentInteractionListener mListener;

    ImageButton buttonDelete;
    ImageButton buttonEdit;
    ImageButton buttonSend;

    private DenunciaDAO denunciaDAO;
    private DenunciaController controller;

    public MyDenunciaRecyclerViewAdapter(View view, List<Denuncia> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        denunciaDAO = new DenunciaDAO(view.getContext());
        controller = new DenunciaController(view.getContext());

        buttonDelete = (ImageButton) view.findViewById(R.id.delete);
        buttonEdit = (ImageButton) view.findViewById(R.id.edit);
        buttonSend = (ImageButton) view.findViewById(R.id.send);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_denuncia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId().toString());
        holder.mContentView.setText(mValues.get(position).getDescricao());

        final Denuncia denuncia = mValues.get(position);

        holder.mItemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((Activity) view.getContext()).getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, DenunciaDetailsFragment.newInstance(denuncia.getId(), ListViewType.LOCAL))
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValues.remove(position);
                denunciaDAO.excluirPorId(denuncia.getId());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mValues.size());
                Toast.makeText(view.getContext(), "Deleted " + denuncia.getDescricao() + "!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((Activity) view.getContext()).getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, NewDenunciaFragment.newInstance(denuncia.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DenunciaServer denunciaServer = new DenunciaServer(denuncia.getId(), denuncia.getDescricao(), denuncia.getData().getTime(), denuncia.getLongitude(), denuncia.getLatitude(), denuncia.getUriMidia(), denuncia.getUsuario(), "EQUIPAMENTOS_COMUNICATARIOS");
                controller.postDenuncia(denunciaServer).enqueue(new Callback<DenunciaServer>() {
                    @Override
                    public void onResponse(Call<DenunciaServer> call, Response<DenunciaServer> response) {
                        Log.d("RESPONSE", response.toString());
                        Toast.makeText(view.getContext(), "OK", Toast.LENGTH_LONG).show();
                        mValues.remove(position);
                        denunciaDAO.excluirPorId(denuncia.getId());
                    }

                    @Override
                    public void onFailure(Call<DenunciaServer> call, Throwable t) {
                        Toast.makeText(view.getContext(), "ERRO", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mItemContent;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageButton mDeleteButton;
        public final ImageButton mEditButton;
        public final ImageButton mSendButton;
        public Denuncia mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemContent = (LinearLayout) view.findViewById(R.id.itemContent);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDeleteButton = (ImageButton) view.findViewById(R.id.delete);
            mEditButton = (ImageButton) view.findViewById(R.id.edit);
            mSendButton = (ImageButton) view.findViewById(R.id.send);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
