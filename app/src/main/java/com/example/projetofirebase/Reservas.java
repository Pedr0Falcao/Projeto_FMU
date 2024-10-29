package com.example.projetofirebase;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Reservas extends AppCompatActivity {

    private static final String TAG = "ShowReservation";

    // Initialize Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        final TextView textViewDate1 = findViewById(R.id.textViewDate1);
        final TextView textViewDate2 = findViewById(R.id.textViewDate2);
        final TextView textViewDate3 = findViewById(R.id.textViewDate3);
        final TextView textViewQuarto = findViewById(R.id.textViewQuarto);

        Button bt_voltar = findViewById(R.id.seta);
        bt_voltar.setOnClickListener(v -> finish());

        Button bt_apagar = findViewById(R.id.seta10);
        bt_apagar.setOnClickListener(v -> {
            deleteReservation(textViewDate1, textViewDate2, textViewDate3, textViewQuarto);
            finish();
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            String userID = currentUser.getUid();

            db.collection("reservations")
                    .whereEqualTo("userID", userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ImageView imageView = findViewById(R.id.quarto1);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.contains("date1")) {
                                        String date1 = document.getString("date1");
                                        textViewDate1.setText(date1);
                                        textViewDate3.setText("ATÉ");
                                        String Quarto = document.getString("quarto");
                                        textViewQuarto.setText(Quarto);
                                        setImageResource(imageView, Quarto);
                                    } else {
                                        textViewDate1.setText("Não há reserva");
                                        textViewDate3.setText("Não há reserva");
                                        imageView.setImageResource(0);
                                        textViewQuarto.setText("");
                                    }

                                    if (document.contains("date2")) {
                                        String date2 = document.getString("date2");
                                        textViewDate2.setText(date2);
                                        textViewDate3.setText("ATÉ");
                                        String Quarto = document.getString("quarto");
                                        textViewQuarto.setText(Quarto);
                                        setImageResource(imageView, Quarto);
                                    } else {
                                        textViewDate2.setText("Não há reserva");
                                        textViewDate3.setText("Não há reserva");
                                        imageView.setImageResource(0);
                                        textViewQuarto.setText("");
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            Log.d(TAG, "User not authenticated");
        }
    }

    // Method to set image resource based on room type
    private void setImageResource(ImageView imageView, String Quarto) {
        if ("Suíte Simples".equals(Quarto)) {
            imageView.setImageResource(R.mipmap.quarto4grande);
        } else if ("Suíte Business".equals(Quarto)) {
            imageView.setImageResource(R.mipmap.quarto5grande);
        } else if ("Suíte Master".equals(Quarto)) {
            imageView.setImageResource(R.mipmap.quarto3yagograndao);
        } else {
            imageView.setImageResource(0);
        }
    }

    private void deleteReservation(TextView textViewDate1, TextView textViewDate2, TextView textViewDate3, TextView textViewQuarto) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            // Realiza uma consulta para obter o documento da reserva do usuário atual
            db.collection("reservations")
                    .whereEqualTo("userID", userID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Delete os campos date1 e date2 do documento
                                db.collection("reservations").document(document.getId())
                                        .update(
                                                "date1", FieldValue.delete(),
                                                "quarto", FieldValue.delete(),
                                                "date2", FieldValue.delete()
                                        )
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d(TAG, "Reservation dates deleted successfully");
                                            } else {
                                                Log.d(TAG, "Error deleting reservation dates", task1.getException());
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            Log.d(TAG, "User not authenticated");
        }
    }
}