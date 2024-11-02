package com.example.projetofirebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperReserva extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reservas.db";
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela
    private static final String TABLE_RESERVAS = "reservas";

    // Colunas da tabela
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATA_INICIO = "data_inicio";
    private static final String COLUMN_DATA_FIM = "data_fim";
    private static final String COLUMN_QUARTO = "quarto";

    // Comando SQL para criar a tabela
    private static final String CREATE_TABLE_RESERVAS =
            "CREATE TABLE " + TABLE_RESERVAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATA_INICIO + " TEXT, " +
                    COLUMN_DATA_FIM + " TEXT, " +
                    COLUMN_QUARTO + " TEXT)";

    public DatabaseHelperReserva(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESERVAS); // Cria a tabela ao inicializar o banco de dados
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVAS); // Remove a tabela antiga se existir
        onCreate(db); // Cria uma nova tabela
    }
}
