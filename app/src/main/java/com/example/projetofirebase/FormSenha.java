package com.example.projetofirebase;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class FormSenha extends AppCompatActivity {
    String[] mensagens = {"Por favor, insira um email cadastrado", "Enviamos o email de recuperação!"};
    private EditText edit_email2;

    private static final String TAG = "FormSenha"; // Defina sua tag de log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Encontrar a EditText no layout XML e atribuir a edit_email2
        edit_email2 = findViewById(R.id.edit_email2);

        // Configurar o botão 'btn_voltar' para voltar uma tela
        Button btnVoltar = findViewById(R.id.seta);
        btnVoltar.setOnClickListener(v -> finish());

        // Configurar o botão 'btn_reset' para enviar o email de redefinição de senha
        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> enviarEmailRedefinicaoSenha());
    }

    private void enviarEmailRedefinicaoSenha() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = edit_email2.getText().toString().trim();

        // Verificar se o campo de email está vazio
        if (email.isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        } else {
            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagens[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();

                        // Adicionar um atraso de 2 segundos antes de retornar
                        // Retornar após 2 segundos
                        new Handler().postDelayed(this::finish, 2000);
                    })
                    .addOnFailureListener(e -> {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Falha ao enviar email de recuperação: " + e.getMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                        Log.e(TAG, "Falha ao enviar email de recuperação", e);
                    });
        }
    }
}
