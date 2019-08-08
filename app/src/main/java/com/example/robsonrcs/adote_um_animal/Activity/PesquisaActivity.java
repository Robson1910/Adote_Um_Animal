package com.example.robsonrcs.adote_um_animal.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class PesquisaActivity extends AppCompatActivity implements AnimaisAdapter.OnItemClickListener {

    private Toolbar toolbarTop, toolbarTop2;
    private TextView mTitle, mTitle2;
    private RecyclerView mRecyclerView;
    private AnimaisAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Animais_Lista> mUploads;
    private MaterialSearchView searchView;
    private ImageView back;
    private String information = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbarTop);
        getSupportActionBar().setTitle("");
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title4);
        toolbarTop2 = (Toolbar) findViewById(R.id.toolbar5);
        mTitle2 = (TextView) toolbarTop.findViewById(R.id.toolbar_title5);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        mProgressCircle = findViewById(R.id.progress_c);
        back = (ImageView) findViewById(R.id.arrow_back_information);
        mRecyclerView = findViewById(R.id.recycler_v);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new AnimaisAdapter(PesquisaActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(PesquisaActivity.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesquisaActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        searchView(information);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView(newText);
                return true;
            }
        });
    }

    private void searchView(final String string) {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Animais_Lista upload = postSnapshot.getValue(Animais_Lista.class);
                    upload.setKey(postSnapshot.getKey());

                    String animaisName = upload.getName();
                    String animaisName2 = upload.getName().toLowerCase();
                    String animaisRaca = upload.getmRaca();
                    String animaisRaca2 = upload.getmRaca().toLowerCase();
                    String animaisCity = upload.getmCity();
                    String animaisCity2 = upload.getmCity().toLowerCase();

                    if (string.isEmpty()) {
                        mUploads.add(upload);
                    } else if (animaisName.contains(string) || animaisName2.contains(string)) {
                        mUploads.add(upload);
                    } else if (animaisRaca.contains(string) || animaisRaca2.contains(string)) {
                        mUploads.add(upload);
                    } else if (animaisCity.contains(string) || animaisCity2.contains(string)) {
                        mUploads.add(upload);
                    }
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
        ImageView close = (ImageView) findViewById(R.id.close_information);

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame2);
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

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }
}
