package com.example.projetocomputacaodispositivosmoveis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Definições do banco de dados
    private static final String DATABASE_NAME = "appDatabase";
    private static final int DATABASE_VERSION = 2;

    // Definições da tabela de usuários
    public static final String TABLE_USERS = "Usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";

    // Definições da tabela de reservas
    public static final String TABLE_RESERVAS = "Reservas";
    public static final String COLUMN_DATA_INICIO = "data_inicio";
    public static final String COLUMN_DATA_FIM = "data_fim";
    public static final String COLUMN_QUARTO = "quarto";
    public static final String COLUMN_USER_ID = "userID";

    // Criação da tabela de usuários
    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_SENHA + " TEXT NOT NULL);";

    // Criação da tabela de reservas
    private static final String TABLE_RESERVAS_CREATE =
            "CREATE TABLE " + TABLE_RESERVAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATA_INICIO + " TEXT NOT NULL, " +
                    COLUMN_DATA_FIM + " TEXT NOT NULL, " +
                    COLUMN_QUARTO + " TEXT NOT NULL, " +
                    COLUMN_USER_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_CREATE); // Cria a tabela de usuários
        db.execSQL(TABLE_RESERVAS_CREATE); // Cria a tabela de reservas
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVAS); // Remove a tabela de reservas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS); // Remove a tabela de usuários
        onCreate(db); // Cria as tabelas novamente
    }

    // Método para inserir um novo usuário
    public boolean addUser(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SENHA, senha);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;  // retorna true se a inserção for bem-sucedida
    }

    // Método para autenticar o usuário
    public boolean authenticateUser(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_SENHA + "=?",
                new String[]{email, senha},
                null, null, null
        );

        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return authenticated;
    }

    // Método para obter o ID do usuário pelo email
    public String getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );

        String userId = null;
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
        }
        db.close();
        return userId;
    }

    // Método para obter o nome do usuário pelo ID
    public String getUserNameById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_NOME},
                COLUMN_ID + "=?",
                new String[]{userId},
                null, null, null
        );

        String userName = null;
        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
            cursor.close();
        }
        db.close();
        return userName;
    }

    // Método para inserir uma nova reserva
    public boolean addReservation(String dataInicio, String dataFim, String quarto, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA_INICIO, dataInicio);
        values.put(COLUMN_DATA_FIM, dataFim);
        values.put(COLUMN_QUARTO, quarto);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_RESERVAS, null, values);
        db.close();
        return result != -1;  // retorna true se a inserção for bem-sucedida
    }

    // Método para obter reservas por ID de usuário
    public Cursor getReservationsByUserId(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESERVAS,
                null, // Todas as colunas
                COLUMN_USER_ID + "=?",
                new String[]{userId},
                null, null, null);
    }
}
