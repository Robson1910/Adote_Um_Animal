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
import com.google.firebase.auth.FirebaseAuth;

public class AccountRecoveryActivity extends AppCompatActivity {

    private Toolbar toolbarTop;
    private TextView mTitle;
    private EditText recoverEmail;
    private Button BtnRecover;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recovery);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbarTop = (Toolbar) findViewById(R.id.toolbar2);
        mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title2);
        recoverEmail = (EditText) findViewById(R.id.Edit_recover_email);
        BtnRecover = (Button) findViewById(R.id.btnRecover);

        BtnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean verificador = recoverEmail.getText().toString().equals("");

                if (!verificador) {
                    auth = FirebaseAuth.getInstance();
                    String emailAddress = recoverEmail.getText().toString().trim();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountRecoveryActivity.this, getString(R.string.AccountRecovery), Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(AccountRecoveryActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(AccountRecoveryActivity.this, getString(R.string.Vazio), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
