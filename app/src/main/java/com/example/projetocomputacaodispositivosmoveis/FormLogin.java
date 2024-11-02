package com.example.projetocomputacaodispositivosmoveis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class FormLogin extends AppCompatActivity {
    private TextView text_tela_cadastro, text_tela_senha;
    private EditText edit_email, edit_senha;
    private Button bt_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos", "Login Efetuado com sucesso"};

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        IniciarComponents();
        databaseHelper = new DatabaseHelper(this);

        text_tela_cadastro.setOnClickListener(v -> {
            Intent intent = new Intent(FormLogin.this, FormCadastro.class);
            startActivity(intent);
        });

        text_tela_senha.setOnClickListener(v -> {
            Intent intent = new Intent(FormLogin.this, FormSenha.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                AutenticarUsuario();
            }
        });
    }

    private void AutenticarUsuario() {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        if (databaseHelper.authenticateUser(email, senha)) {
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> TelaPrincipal(), 3000);
        } else {
            Snackbar snackbar = Snackbar.make(bt_entrar, "Usuário ou senha incorretos", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    private void TelaPrincipal() {
        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponents() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
        text_tela_senha = findViewById(R.id.text_tela_senha);
    }

    // Classe DatabaseHelper para gerenciamento do banco de dados SQLite
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "appDatabase.db";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_USERS = "usuarios";
        private static final String COLUMN_EMAIL = "email";
        private static final String COLUMN_PASSWORD = "senha";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                    COLUMN_PASSWORD + " TEXT)";
            db.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }

        public boolean authenticateUser(String email, String password) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_EMAIL, COLUMN_PASSWORD},
                    COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, password}, null, null, null);

            boolean authenticated = cursor.moveToFirst();
            cursor.close();
            db.close();
            return authenticated;
        }
    }
}
