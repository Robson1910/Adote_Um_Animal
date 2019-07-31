package com.example.robsonrcs.adote_um_animal;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbarTop;
    private TextView mTitle;
    private EditText createEmail;
    private EditText createPassword;
    private Button BtnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar1);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title1);

        createEmail = (EditText) findViewById(R.id.Edit_create_email);
        createPassword = (EditText) findViewById(R.id.Edit_create_password);
        BtnRegister = (Button) findViewById(R.id.btnCadastro);
        auth = FirebaseAuth.getInstance();

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean verificador = createEmail.getText().toString().equals("");
                boolean verificador2 = createPassword.getText().toString().equals("");

                if (!verificador && !verificador2) {

                    final String email = createEmail.getText().toString().trim();
                    final String password = createPassword.getText().toString().trim();

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), getString(R.string.senha_curta), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, getString(R.string.conta_existe), Toast.LENGTH_SHORT).show();
                                    } else {
                                        SendEmailVerification();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, getString(R.string.Vazio), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SendEmailVerification() {
        final FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                findViewById(R.id.Edit_create_email).setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.SignUpAutetication) + user.getEmail(), Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, getString(R.string.SignUpError) + user.getEmail(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

