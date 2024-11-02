package com.example.projetofirebase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Reservas extends AppCompatActivity {

    private static final String TAG = "ShowReservation";
    private DatabaseHelperReserva databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        final TextView textViewDate1 = findViewById(R.id.textViewDate1);
        final TextView textViewDate2 = findViewById(R.id.textViewDate2);
        final TextView textViewDate3 = findViewById(R.id.textViewDate3);
        final TextView textViewQuarto = findViewById(R.id.textViewQuarto);
        ImageView imageView = findViewById(R.id.quarto1);

        databaseHelper = new DatabaseHelperReserva(this);

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        Button bt_apagar = findViewById(R.id.seta10);
        bt_apagar.setOnClickListener(v -> {
            deleteReservation(textViewDate1, textViewDate2, textViewDate3, textViewQuarto, imageView);
            finish();
        });

        carregarReserva(textViewDate1, textViewDate2, textViewDate3, textViewQuarto, imageView);
    }

    private void carregarReserva(TextView textViewDate1, TextView textViewDate2, TextView textViewDate3, TextView textViewQuarto, ImageView imageView) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("reservas", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            String date2 = cursor.getString(cursor.getColumnIndexOrThrow("data_inicio"));
            String date1 = cursor.getString(cursor.getColumnIndexOrThrow("data_fim"));
            String quarto = cursor.getString(cursor.getColumnIndexOrThrow("quarto"));

            textViewDate1.setText(date1);
            textViewDate2.setText(date2);
            textViewDate3.setText("ATÉ");
            textViewQuarto.setText(quarto);

            setImageResource(imageView, quarto);
        } else {
            textViewDate1.setText("Não há reserva");
            textViewDate2.setText("Não há reserva");
            textViewDate3.setText("");
            textViewQuarto.setText("");
            imageView.setImageResource(0);
        }
        cursor.close();
        db.close();
    }

    private void setImageResource(ImageView imageView, String quarto) {
        if ("Suíte Simples".equals(quarto)) {
            imageView.setImageResource(R.mipmap.quarto4grande);
        } else if ("Suíte Business".equals(quarto)) {
            imageView.setImageResource(R.mipmap.quarto5grande);
        } else if ("Suíte Master".equals(quarto)) {
            imageView.setImageResource(R.mipmap.quarto3yagograndao);
        } else {
            imageView.setImageResource(0);
        }
    }

    private void deleteReservation(TextView textViewDate1, TextView textViewDate2, TextView textViewDate3, TextView textViewQuarto, ImageView imageView) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsDeleted = db.delete("reservas", null, null);
        db.close();

        if (rowsDeleted > 0) {
            textViewDate1.setText("Reserva apagada");
            textViewDate2.setText("Reserva apagada");
            textViewDate3.setText("");
            textViewQuarto.setText("");
            imageView.setImageResource(0);
            Log.d(TAG, "Reserva apagada com sucesso.");
        } else {
            Log.d(TAG, "Nenhuma reserva para apagar.");
        }
    }
}
