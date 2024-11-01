package com.example.projetofirebase;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private Button bt_deslogar, btContato, btQuartos, btReservas;
    private DatabaseHelper databaseHelper;
    private String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        databaseHelper = new DatabaseHelper(this);
        IniciarComponentes();

        bt_deslogar.setOnClickListener(v -> {
            deslogarUsuario();
            Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
            startActivity(intent);
            finish();
        });

        btContato.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, Contato.class);
            startActivity(intent);
        });

        btQuartos.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, Quartos.class);
            startActivity(intent);
        });

        btReservas.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, Reservas.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarDadosUsuario();
    }

    private void carregarDadosUsuario() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Supondo que o `usuarioID` foi armazenado em uma sessão ou passado como `Extra`
        usuarioID = obterUsuarioLogadoID();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_NOME, DatabaseHelper.COLUMN_EMAIL},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{usuarioID},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            nomeUsuario.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME)));
            emailUsuario.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)));
            cursor.close();
        }
        db.close();
    }

    private void deslogarUsuario() {
        // Aqui você pode limpar qualquer sessão de login salva, por exemplo, usando `SharedPreferences`
        getSharedPreferences("appPreferences", MODE_PRIVATE).edit().clear().apply();
    }

    private String obterUsuarioLogadoID() {
        // Recupera o ID do usuário atualmente logado armazenado nas `SharedPreferences`
        return getSharedPreferences("appPreferences", MODE_PRIVATE).getString("usuarioID", "");
    }

    private void IniciarComponentes() {
        nomeUsuario = findViewById(R.id.textNomeUsuario);
        emailUsuario = findViewById(R.id.textEmailUsuario);
        bt_deslogar = findViewById(R.id.bt_deslogar);
        btContato = findViewById(R.id.bt_contato);
        btQuartos = findViewById(R.id.bt_quartos);
        btReservas = findViewById(R.id.bt_reservas);
    }
}
