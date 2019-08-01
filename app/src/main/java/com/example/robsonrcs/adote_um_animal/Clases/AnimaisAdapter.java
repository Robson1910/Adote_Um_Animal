package com.example.robsonrcs.adote_um_animal.Clases;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.robsonrcs.adote_um_animal.R;

import java.util.List;

public class AnimaisAdapter extends RecyclerView.Adapter<AnimaisAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Animais_Lista> mUploads;
    private OnItemClickListener mListener;

    public AnimaisAdapter(Context context, List<Animais_Lista> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.animais_adapter, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Animais_Lista uploadCurrent = mUploads.get(position);
        holder.textViewName12.setText("Nome: " + uploadCurrent.getName());
        holder.textViewCidade12.setText("Cidade: " + uploadCurrent.getmCity());
        holder.textViewRaca12.setText("Raça: " + uploadCurrent.getmRaca());
        Glide.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.logos)
                .centerCrop()
                .into(holder.imageView12);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName12;
        public TextView textViewCidade12;
        public TextView textViewRaca12;
        public ImageView imageView12;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName12 = itemView.findViewById(R.id.text_name_view1);
            imageView12 = itemView.findViewById(R.id.image_upload);
            textViewCidade12 = itemView.findViewById(R.id.text_cidade_view_1);
            textViewRaca12 = itemView.findViewById(R.id.text_raca_view_1);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}