package com.example.robsonrcs.adote_um_animal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbarTop;
    private TextView mTitle;
    private CardView exit;
    private CardView cadastrar;
    private CardView excluir;
    private CardView pesquisar;
    private CardView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar3);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title3);
        exit = (CardView) findViewById(R.id.cardSair);
        cadastrar = (CardView) findViewById(R.id.cardCadastrar);
        excluir = (CardView) findViewById(R.id.cardExcluir);
        pesquisar = (CardView) findViewById(R.id.cardPesquisar);
        feedback = (CardView) findViewById(R.id.cardFeedback);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                goLoginScreen();
            }
        });

       /* cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, Cadastro_informacaoActivity.class));
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, Excluir_cadastroActivity.class));
            }
        });

        pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, Lista_animaisActivity.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, FeedbackActivity.class));
            }
        });
        */
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}