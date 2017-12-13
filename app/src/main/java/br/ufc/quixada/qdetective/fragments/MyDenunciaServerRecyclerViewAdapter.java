package br.ufc.quixada.qdetective.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.controller.DenunciaController;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.entity.ListViewType;
import br.ufc.quixada.qdetective.fragments.DenunciaServerListFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyDenunciaServerRecyclerViewAdapter extends RecyclerView.Adapter<MyDenunciaServerRecyclerViewAdapter.ViewHolder> {

    private final List<Denuncia> mValues;
    private final OnListFragmentInteractionListener mListener;

    ImageButton buttonDelete;
    ImageButton buttonEdit;

    private DenunciaController controller;

    public MyDenunciaServerRecyclerViewAdapter(View view, List<Denuncia> items, DenunciaServerListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        controller = new DenunciaController(view.getContext());

        buttonDelete = (ImageButton) view.findViewById(R.id.delete);
        buttonEdit = (ImageButton) view.findViewById(R.id.edit);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_denuncia_server_list, parent, false);
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
                fm.beginTransaction().replace(R.id.content_frame, DenunciaDetailsFragment.newInstance(denuncia.getId(), ListViewType.SERVER))
                        .addToBackStack(null)
                        .commit();
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValues.remove(position);
                //denunciaDAO.excluirPorId(denuncia.getId());
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
        public Denuncia mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemContent = (LinearLayout) view.findViewById(R.id.itemContent);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDeleteButton = (ImageButton) view.findViewById(R.id.delete);
            mEditButton = (ImageButton) view.findViewById(R.id.edit);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
