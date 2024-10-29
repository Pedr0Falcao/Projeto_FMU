package com.example.projetofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private Button bt_deslogar, btContato, btQuartos, btReservas;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        IniciarComponentes();

        bt_deslogar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
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

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                nomeUsuario.setText(documentSnapshot.getString("nome"));
                emailUsuario.setText(email);
            }
        });
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
