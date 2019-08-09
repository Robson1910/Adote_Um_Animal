package com.example.robsonrcs.adote_um_animal.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.example.robsonrcs.adote_um_animal.Clases.Animais_Lista;
import com.example.robsonrcs.adote_um_animal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class CadastroActivity extends AppCompatActivity {

    private Toolbar toolbarTop, toolbarTop2;
    private TextView mTitle, mTitle2;
    private ImageView back, close, mImageView1;
    private EditText nome, raca, tipo, cidade, telefone, email, descricao;
    private Button avanca, mButtonUpload1, mButtonChooseImage1;
    private FrameLayout frameLayout;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri1;
    private StorageReference mStorageRef1;
    private DatabaseReference mDatabaseRef1;
    private StorageTask mUploadTask1;
    private ProgressBar mProgressBar1;
    private FirebaseAuth firebaseAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar7);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title7);
        toolbarTop2 = (Toolbar) findViewById(R.id.toolbar8);
        mTitle2 = (TextView) toolbarTop.findViewById(R.id.toolbar_title8);
        frameLayout = (FrameLayout) findViewById(R.id.frame4);
        back = (ImageView) findViewById(R.id.arrow_back_cadastrar);
        close = (ImageView) findViewById(R.id.arrow_close_cadastrar);
        nome = (EditText) findViewById(R.id.Edit_text_Nome);
        raca = (EditText) findViewById(R.id.Edit_text_raca);
        tipo = (EditText) findViewById(R.id.Edit_text_tipo);
        cidade = (EditText) findViewById(R.id.Edit_text_city);
        telefone = (EditText) findViewById(R.id.Edit_text_telefone);
        email = (EditText) findViewById(R.id.Edit_text_email2);
        descricao = (EditText) findViewById(R.id.edit_text_description);
        avanca = (Button) findViewById(R.id.btnAvanca);

        mProgressBar1 = findViewById(R.id.progress_bar);
        mImageView1 = findViewById(R.id.image_view_1);
        mButtonUpload1 = findViewById(R.id.btnCadastraFirebase);
        mButtonChooseImage1 = findViewById(R.id.button_choose_image);
        mStorageRef1 = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("uploads");
        firebaseAuth1 = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        avanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nome.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Nome vazio", Toast.LENGTH_LONG).show();
                } else if (raca.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Raça vazio", Toast.LENGTH_LONG).show();
                } else if (tipo.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Gênero vazio", Toast.LENGTH_LONG).show();
                } else if (cidade.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Cidade vazio", Toast.LENGTH_LONG).show();
                } else if (telefone.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo WhatsApp vazio", Toast.LENGTH_LONG).show();
                } else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Email vazio", Toast.LENGTH_LONG).show();
                } else if (descricao.getText().toString().isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Campo Descrição vazio", Toast.LENGTH_LONG).show();
                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
            }
        });

        mButtonChooseImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask1 != null && mUploadTask1.isInProgress()) {
                    Toast.makeText(CadastroActivity.this, "Em Andamento...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    } //end

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri1 = data.getData();

            Glide.with(CadastroActivity.this).load(mImageUri1).centerCrop().into(mImageView1);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri1 != null) {
            StorageReference fileReference = mStorageRef1.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri1));

            mUploadTask1 = fileReference.putFile(mImageUri1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar1.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(CadastroActivity.this, "Cadastro Concluido", Toast.LENGTH_LONG).show();

                            Animais_Lista animais = new Animais_Lista();
                            animais.setName(nome.getText().toString());
                            animais.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                            animais.setmRaca(raca.getText().toString());
                            animais.setmTipo(tipo.getText().toString());
                            animais.setmCity(cidade.getText().toString());
                            animais.setmTelefone(telefone.getText().toString());
                            animais.setmEmail(email.getText().toString());
                            animais.setmDescription(descricao.getText().toString());
                            animais.setUserId(firebaseAuth1.getCurrentUser().getUid());

                            String uploadId = mDatabaseRef1.push().getKey();
                            mDatabaseRef1.child(uploadId).setValue(animais);
                            startActivity(new Intent(CadastroActivity.this, HomeActivity.class));
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar1.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Foto nao selecionado", Toast.LENGTH_SHORT).show();
        }
    }
}
