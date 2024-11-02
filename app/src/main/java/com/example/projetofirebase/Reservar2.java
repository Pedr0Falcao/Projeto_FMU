package com.example.projetofirebase;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;

public class Reservar2 extends AppCompatActivity {
    private EditText editTextDate;
    private EditText editTextDate4;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    String[] mensagens = {"Reserva feita com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar3);

        IniciarComponentes();

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        editTextDate = findViewById(R.id.editTextDate);
        editTextDate4 = findViewById(R.id.editTextDate4);
        calendar = Calendar.getInstance();
        databaseHelper = new DatabaseHelper(this);

        editTextDate.setOnClickListener(this::onClick);
        editTextDate4.setOnClickListener(this::onClick);

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(this::saveReservation);
    }

    private void confirmReservation() {
        // Implement confirmation logic here if needed
    }

    private void IniciarComponentes() {
        // Initialize any other components here
    }

    private void onClick(View v) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Reservar2.this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;

                    if (v == editTextDate) {
                        editTextDate.setText(selectedDate);
                    } else if (v == editTextDate4) {
                        editTextDate4.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        if (v == editTextDate || v == editTextDate4) {
            datePickerDialog.show();
        }
    }

    private void saveReservation(View v) {
        String date1 = editTextDate.getText().toString();
        String date2 = editTextDate4.getText().toString();
        String roomType = "Suíte Simples";

        ContentValues values = new ContentValues();
        values.put("data_inicio", date1);
        values.put("data_fim", date2);
        values.put("quarto", roomType);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.insert("reservas", null, values);
        db.close();

        if (result != -1) {
            mostrarSnackbar(v, mensagens[0]);
        } else {
            mostrarSnackbar(v, "Erro ao realizar reserva");
        }
    }

    private void mostrarSnackbar(View v, String mensagem) {
        Snackbar snackbar = Snackbar.make(v, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                startActivity(new Intent(Reservar2.this, TelaPrincipal.class));
                finish();
            }
        });
        snackbar.show();
    }
}
