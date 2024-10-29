package com.example.projetofirebase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormCadastro extends AppCompatActivity {
    private EditText edit_nome, edit_email, edit_senha, edit_senha2;
    private Button bt_cadastrar;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Senha diferente em um dos campos"};
    String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        IniciarComponentes();

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        bt_cadastrar.setOnClickListener(v -> {

            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            String senha2 = edit_senha2.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                if ( senha.equals(senha2)) {
                    CadastrarUsuario(v);
                } else{
                    Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private boolean isEmailValido(String email) {
        // Lista de domínios válidos
        String[] dominiosValidos = {"gmail.com", "hotmail.com", "yahoo.com", "bol.com", "Outlook.com"};

        // Obter o domínio do email
        String dominio = email.substring(email.indexOf("@") + 1);

        // Verificar se o domínio está na lista de domínios válidos
        for (String dominioValido : dominiosValidos) {
            if (dominioValido.equals(dominio)) {
                return true;
            }
        }
        return false;
    }

    private void CadastrarUsuario(View v) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        // Verificar se o email é válido
        if (!isEmailValido(email)) {
            Snackbar snackbar = Snackbar.make(v, "Email inválido", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SalvarDadosUsuario();
                Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite uma senha com no mínimo 6 números";
                } catch (FirebaseAuthUserCollisionException e) {
                    erro = "Esta conta já foi cadastrada";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "Email inválido";
                } catch (Exception e) {
                    erro = "Erro ao cadastrar usuário";
                }
                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }
    private void SalvarDadosUsuario(){
        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(unused -> Log.d("db", "Sucesso ao salvar os dados"))
                .addOnFailureListener(e -> Log.d("db_error","Erro ao salvar os dados"));
    }

    private void IniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        edit_senha2 = findViewById(R.id.edit_senha2);
    };
}