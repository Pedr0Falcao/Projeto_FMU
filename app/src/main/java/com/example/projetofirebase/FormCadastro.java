package com.example.projetofirebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.snackbar.Snackbar;

public class FormCadastro extends AppCompatActivity {
    private EditText edit_nome, edit_email, edit_senha, edit_senha2;
    private Button bt_cadastrar;
    private DatabaseHelper databaseHelper;
    String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Senha diferente em um dos campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);

        // Configuração de margens da interface
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        IniciarComponentes();
        databaseHelper = new DatabaseHelper(this);

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        bt_cadastrar.setOnClickListener(v -> {
            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            String senha2 = edit_senha2.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty()) {
                mostrarSnackbar(v, mensagens[0]);
            } else {
                if (senha.equals(senha2)) {
                    CadastrarUsuario(v);
                } else {
                    mostrarSnackbar(v, mensagens[2]);
                }
            }
        });
    }

    private boolean isEmailValido(String email) {
        String[] dominiosValidos = {"gmail.com", "hotmail.com", "yahoo.com", "bol.com", "outlook.com"};
        String dominio = email.substring(email.indexOf("@") + 1);
        for (String dominioValido : dominiosValidos) {
            if (dominioValido.equalsIgnoreCase(dominio)) {
                return true;
            }
        }
        return false;
    }

    private void CadastrarUsuario(View v) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        String nome = edit_nome.getText().toString();

        if (!isEmailValido(email)) {
            mostrarSnackbar(v, "Email inválido");
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Verificar se o email já está registrado
        if (databaseHelper.isEmailRegistered(email)) {
            mostrarSnackbar(v, "Este email já está registrado");
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NOME, nome);
            values.put(DatabaseHelper.COLUMN_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_SENHA, senha);

            long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
            if (result != -1) {
                mostrarSnackbar(v, mensagens[1]);
            } else {
                mostrarSnackbar(v, "Erro ao cadastrar usuário");
            }
        }
        db.close();
    }

    private void mostrarSnackbar(View v, String mensagem) {
        Snackbar snackbar = Snackbar.make(v, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    private void IniciarComponentes(){
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
        edit_senha2 = findViewById(R.id.edit_senha2);
    }

    // Classe DatabaseHelper para gerenciar o banco de dados SQLite
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "appDatabase.db";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_USERS = "usuarios";
        private static final String COLUMN_NOME = "nome";
        private static final String COLUMN_EMAIL = "email";
        private static final String COLUMN_SENHA = "senha";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                    COLUMN_NOME + " TEXT, " +
                    COLUMN_SENHA + " TEXT)";
            db.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }

        public boolean isEmailRegistered(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_EMAIL},
                    COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
            boolean exists = cursor.moveToFirst();
            cursor.close();
            return exists;
        }
    }
}
