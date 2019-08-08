package com.example.robsonrcs.adote_um_animal.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robsonrcs.adote_um_animal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar toolbarTop;
    private TextView mTitle;
    private ImageView back;
    EditText nome;
    EditText email;
    EditText descricao;
    Button enviar;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar7);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title7);
        back = (ImageView) findViewById(R.id.arrow_back_feedback);
        nome = (EditText) findViewById(R.id.Edit_nome_text);
        email = (EditText) findViewById(R.id.Edit_email_text);
        descricao = (EditText) findViewById(R.id.Edit_comentario_text);
        enviar = (Button) findViewById(R.id.btnFeddback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean verificador = nome.getText().toString().equals("");
                boolean verificador2 = email.getText().toString().equals("");
                boolean verificador3 = descricao.getText().toString().equals("");

                if(!verificador && !verificador2 &&! verificador3 ) {
                    String nome1 = nome.getText().toString();
                    String email1 = email.getText().toString();
                    String descricao1 = descricao.getText().toString();
                    Random randomico = new Random();
                    int rand = randomico.nextInt(10000);

                    referencia.child("feedback").child(String.valueOf(rand)).setValue("Nome:"+nome1 +" - Email:"+ email1 +" - Descricao:"+descricao1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("sucesso no banco de dados");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("falha no banco de dados");
                                }
                            });

                    Toast.makeText(FeedbackActivity.this, "Mensagem enviado com sucesso", Toast.LENGTH_LONG).show();
                    nome.setText("");
                    email.setText("");
                    descricao.setText("");
                }
                else {
                    Toast.makeText(FeedbackActivity.this, "Campo vazio", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
