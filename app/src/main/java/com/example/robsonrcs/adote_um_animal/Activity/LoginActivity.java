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
import android.widget.TextView;
import android.widget.Toast;

import com.example.robsonrcs.adote_um_animal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbarTop;
    private TextView mTitle, recuperar;
    private Button login, cadastrar;
    private FirebaseAuth auth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        login = (Button) findViewById(R.id.btnLogin);
        cadastrar = (Button) findViewById(R.id.btnSignUp);
        recuperar = (TextView) findViewById(R.id.Recover_Password);
        email = (EditText) findViewById(R.id.Edit_text_email);
        password = (EditText) findViewById(R.id.Edit_text_password);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AccountRecoveryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean verificador = email.getText().toString().equals("");
                boolean verificador2 = password.getText().toString().equals("");

                if (!verificador && !verificador2) {
                    String edit_email = email.getText().toString().trim();
                    final String edit_password = password.getText().toString().trim();

                    auth.signInWithEmailAndPassword(edit_email, edit_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            password.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.Vazio), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
