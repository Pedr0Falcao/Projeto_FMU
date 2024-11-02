package com.example.projetocomputacaodispositivosmoveis;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Contato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

            IniciarComponentes();

            Button bt_voltar = findViewById(R.id.seta);
            bt_voltar.setOnClickListener(v -> finish());
        }
    private void IniciarComponentes() {
    }
}