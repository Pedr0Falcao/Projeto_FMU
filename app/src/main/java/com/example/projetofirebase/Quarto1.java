package com.example.projetofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Quarto1 extends AppCompatActivity {

    private Button bt_reservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarto1);

        IniciarComponentes();

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        bt_reservar.setOnClickListener(v -> {
            Intent intent = new Intent(Quarto1.this, Reservar3.class);
            startActivity(intent);
        });

    }
    private void IniciarComponentes() {
        bt_reservar = findViewById(R.id.bt_reservar);

    }
}
