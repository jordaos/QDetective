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
import br.ufc.quixada.qdetective.dao.DenunciaDAO;
import br.ufc.quixada.qdetective.entity.Denuncia;
import br.ufc.quixada.qdetective.fragments.DenunciaListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Denuncia} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDenunciaRecyclerViewAdapter extends RecyclerView.Adapter<MyDenunciaRecyclerViewAdapter.ViewHolder> {

    private final List<Denuncia> mValues;
    private final OnListFragmentInteractionListener mListener;

    Button buttonDelete;
    Button buttonEdit;

    private DenunciaDAO denunciaDAO;

    public MyDenunciaRecyclerViewAdapter(View view, List<Denuncia> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        denunciaDAO = new DenunciaDAO(view.getContext());

        buttonDelete = (Button) view.findViewById(R.id.delete);
        buttonEdit = (Button) view.findViewById(R.id.edit);
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
                fm.beginTransaction().replace(R.id.content_frame, DenunciaDetailsFragment.newInstance(denuncia.getId()))
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
