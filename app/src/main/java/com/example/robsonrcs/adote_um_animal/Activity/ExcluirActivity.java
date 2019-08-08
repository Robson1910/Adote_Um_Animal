package com.example.robsonrcs.adote_um_animal.Activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robsonrcs.adote_um_animal.Clases.AnimaisAdapter;
import com.example.robsonrcs.adote_um_animal.Clases.Animais_Lista;
import com.example.robsonrcs.adote_um_animal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ExcluirActivity extends AppCompatActivity implements AnimaisAdapter.OnItemClickListener {

    private Toolbar toolbarTop;
    private TextView mTitle;
    private RecyclerView mRecyclerView;
    private AnimaisAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private AlertDialog alerta;
    private List<Animais_Lista> mUploads;
    private FirebaseAuth firebaseAuth;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excluir);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar6);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title6);
        back = (ImageView) findViewById(R.id.arrow_back_excluir);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new AnimaisAdapter(ExcluirActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ExcluirActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExcluirActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Animais_Lista upload = postSnapshot.getValue(Animais_Lista.class);
                    upload.setKey(postSnapshot.getKey());
                    firebaseAuth = FirebaseAuth.getInstance();

                    if (firebaseAuth.getCurrentUser().getUid().equals(upload.getUserId())) {

                        mUploads.add(upload);
                    }
                }

                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ExcluirActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExcluirActivity.this);
        builder.setTitle("Excluir Cadastro");
        builder.setMessage("Você deseja Excluir ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                onDeleteClick(position);

                Toast.makeText(ExcluirActivity.this, "Cadastro excluido com sucesso", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ExcluirActivity.this, "Cadastro não excluido", Toast.LENGTH_LONG).show();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    @Override
    public void onWhatEverClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

        final Animais_Lista selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        final StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ExcluirActivity.this, "Item deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}
