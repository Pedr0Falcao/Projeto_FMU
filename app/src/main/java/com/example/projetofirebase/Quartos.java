package com.example.projetofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Quartos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quartos);


        IniciarComponentes();

        Button bt_voltar = findViewById(R.id.seta3);
        bt_voltar.setOnClickListener(v -> finish());

        // Encontre os ImageButtons
        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        ImageButton imageButton3 = findViewById(R.id.imageButton3);

        // Defina os métodos de clique para cada ImageButton
        imageButton1.setOnClickListener(v -> {
            // Faça algo quando o primeiro ImageButton for clicado
            Intent intent = new Intent(Quartos.this, Quarto3.class);
            startActivity(intent);
        });

        imageButton2.setOnClickListener(v -> {
            // Faça algo quando o segundo ImageButton for clicado
            Intent intent = new Intent(Quartos.this, Quarto1.class);
            startActivity(intent);
        });

        imageButton3.setOnClickListener(v -> {
            // Faça algo quando o terceiro ImageButton for clicado
            Intent intent = new Intent(Quartos.this, Quarto2.class);
            startActivity(intent);
        });
    }

    private void IniciarComponentes() {
    }

    public void onClickImageButton(View view) {
    }
}