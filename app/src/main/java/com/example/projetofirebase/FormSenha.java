package com.example.projetofirebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class FormSenha extends AppCompatActivity {
    private EditText edit_email2;
    private static final String TAG = "FormSenha";
    private DatabaseHelper databaseHelper;
    String[] mensagens = {"Por favor, insira um email cadastrado", "Email de recuperação simulado com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Inicializar componentes e DatabaseHelper
        edit_email2 = findViewById(R.id.edit_email2);
        databaseHelper = new DatabaseHelper(this);

        Button btnVoltar = findViewById(R.id.seta);
        btnVoltar.setOnClickListener(v -> finish());

        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> enviarEmailRedefinicaoSenha());
    }

    private void enviarEmailRedefinicaoSenha() {
        String email = edit_email2.getText().toString().trim();

        if (email.isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        } else {
            if (databaseHelper.isEmailRegistered(email)) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), mensagens[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                // Adicionar um atraso de 2 segundos antes de retornar
                new Handler().postDelayed(this::finish, 2000);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Email não encontrado", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
                Log.e(TAG, "Email não registrado: " + email);
            }
        }
    }

    // Classe DatabaseHelper para gerenciamento do banco de dados SQLite
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "appDatabase.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_USERS = "usuarios";
        private static final String COLUMN_EMAIL = "email";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_EMAIL + " TEXT PRIMARY KEY)";
            db.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }

        public boolean isEmailRegistered(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_EMAIL},
                    COLUMN_EMAIL + "=?",
                    new String[]{email}, null, null, null);

            boolean isRegistered = cursor.moveToFirst();
            cursor.close();
            db.close();
            return isRegistered;
        }
    }
}
