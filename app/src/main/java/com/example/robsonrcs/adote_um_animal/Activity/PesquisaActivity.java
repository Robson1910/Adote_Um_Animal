package com.example.robsonrcs.adote_um_animal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.robsonrcs.adote_um_animal.Clases.AnimaisAdapter;
import com.example.robsonrcs.adote_um_animal.Clases.Animais_Lista;
import com.example.robsonrcs.adote_um_animal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class PesquisaActivity extends AppCompatActivity implements AnimaisAdapter.OnItemClickListener {

    private Toolbar toolbarTop, toolbarTop2;
    private TextView mTitle, mTitle2;
    private RecyclerView mRecyclerView;
    private AnimaisAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Animais_Lista> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar4);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title4);
        toolbarTop2 = (Toolbar) findViewById(R.id.toolbar5);
        mTitle2 = (TextView) toolbarTop.findViewById(R.id.toolbar_title5);

        mRecyclerView = findViewById(R.id.recycler_v);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_c);

        mUploads = new ArrayList<>();

        mAdapter = new AnimaisAdapter(PesquisaActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(PesquisaActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Animais_Lista upload = postSnapshot.getValue(Animais_Lista.class);
                    upload.setKey(postSnapshot.getKey());

                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PesquisaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        onWhatEverClick(position);
    }

    @Override
    public void onWhatEverClick(int position) {

        TextView telefone = (TextView) findViewById(R.id.text_view_telefone_1);
        TextView raca = (TextView) findViewById(R.id.text_view_raca_1);
        TextView city = (TextView) findViewById(R.id.text_view_cidade_1);
        TextView tipo = (TextView) findViewById(R.id.text_view_tipo_1);
        TextView email = (TextView) findViewById(R.id.text_view_email_1);
        TextView description = (TextView) findViewById(R.id.text_view_descricao_1);
        TextView name = (TextView) findViewById(R.id.text_nome_info_1);
        ImageView img = (ImageView) findViewById(R.id.image_info1);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame2);
        frameLayout.setVisibility(View.VISIBLE);

        Animais_Lista selectedItem = mUploads.get(position);

        String selectedTelefone = selectedItem.getmTelefone();
        String selectedRaca = selectedItem.getmRaca();
        String selectedCity = selectedItem.getmCity();
        String selectedTipo = selectedItem.getmTipo();
        String selectedEmail = selectedItem.getmEmail();
        String selectedDescription = selectedItem.getmDescription();
        String selectedName = selectedItem.getName();
        String selectedImagem = selectedItem.getImageUrl();

        Glide.with(PesquisaActivity.this)
                .load(selectedImagem)
                .centerCrop()
                .into(img);
        telefone.setText(selectedTelefone);
        raca.setText(selectedRaca);
        city.setText(selectedCity);
        tipo.setText(selectedTipo);
        email.setText(selectedEmail);
        description.setText(selectedDescription);
        name.setText(selectedName);
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
