package com.example.projetocomputacaodispositivosmoveis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Quarto2 extends AppCompatActivity {

    private Button bt_reservar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarto2);

        IniciarComponentes();

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());


        bt_reservar.setOnClickListener(v -> {
            Intent intent = new Intent(Quarto2.this, Reservar.class);
            startActivity(intent);
        });

    }
    private void IniciarComponentes() {
        bt_reservar = findViewById(R.id.bt_reservar);

    }
}
