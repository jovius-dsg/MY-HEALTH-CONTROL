package com.example.myhealthcontrol.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myhealthcontrol.modelo.Alarme;
import com.example.myhealthcontrol.modelo.Pessoa;

import java.util.ArrayList;

public class BancoDAO extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "health.db";
    private static final int VERSION = 1;
    private static final String TABELA = "pessoa";
    private static final String ID = "id";
    private static final String NOME = "nome";


    private static final String TABELA_ALARME = "alarme";
    private static final String ID_ALARME = "id";
    private static final String NOME_ALARME = "nome";
    private static final String HORARIO = "horario";
    private static final String DOSAGEM = "dosagem";
    private static final String FREQUENCIA = "frequencia";
    private static final String DIAS_SEMANAS = "dias_semanas";

    public BancoDAO(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+" ("+
                " "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                " "+NOME+" TEXT );";


        String sql_alarme = "CREATE TABLE "+TABELA_ALARME+" ( "+
                " "+ID_ALARME+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                " "+NOME_ALARME+" TEXT, "+
                " "+HORARIO+" INTEGER, "+
                " "+DOSAGEM+" TEXT, "+
                " "+FREQUENCIA+" TEXT, "+
                " "+DIAS_SEMANAS+" TEXT );";

        Log.e("pessos", sql);
        Log.e("alarme", sql_alarme);
        db.execSQL(sql_alarme);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE " + TABELA;
        String sql_alarme = "DROP TABLE " + TABELA_ALARME;
        db.execSQL(sql);
        db.execSQL(sql_alarme);
        onCreate(db);
    }

    public long salvarPessoa(Pessoa p) {
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, p.getNome());
        retornoDB = getWritableDatabase().insert(TABELA, null, values);

        return retornoDB;
    }

    public long alterarPessoa(Pessoa p) {
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, p.getNome());

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().update(TABELA, values, "id=?", args);

        return retornoDB;
    }

    public long excluirPessoa(Pessoa p) {
        long retornoDB;

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().delete(TABELA, ID + "=?", args);

        return retornoDB;
    }

    public ArrayList<Pessoa> selectAllPessoa() {
        String[] coluns = {ID, NOME};
        Cursor cursor = getWritableDatabase().query(TABELA, coluns, null, null, null, null, "upper(nome)", null);

        ArrayList<Pessoa> listPessoa = new ArrayList<Pessoa>();
        while (cursor.moveToNext()) {
            Pessoa p = new Pessoa();

            p.setId(cursor.getInt(0));
            p.setNome(cursor.getString(1));

            listPessoa.add(p);
        }

        return listPessoa;
    }

    public long salvarAlarme(Alarme a) {
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, a.getNome());
        values.put(HORARIO, a.getHorario());
        values.put(DOSAGEM, a.getDosagem());
        values.put(FREQUENCIA, a.getFrequencia());
        values.put(DIAS_SEMANAS, a.getDias_semana());

        retornoDB = getWritableDatabase().insert(TABELA_ALARME, null, values);

        return retornoDB;
    }

    public long alterarAlarme(Alarme a) {
        ContentValues values = new ContentValues();
        long retornoDB;

        values.put(NOME, a.getNome());
        values.put(HORARIO, a.getHorario());
        values.put(DOSAGEM, a.getDosagem());
        values.put(FREQUENCIA, a.getFrequencia());
        values.put(DIAS_SEMANAS, a.getDias_semana());

        String[] args = {String.valueOf(a.getId())};
        retornoDB = getWritableDatabase().update(TABELA_ALARME, values, "id=?", args);

        return retornoDB;
    }

    public long excluirAlarme(Alarme p) {
        long retornoDB;

        String[] args = {String.valueOf(p.getId())};
        retornoDB = getWritableDatabase().delete(TABELA_ALARME, ID + "=?", args);

        return retornoDB;
    }

    public ArrayList<Alarme> selectAllAlarme() {
        String[] coluns = {ID, NOME, HORARIO, DOSAGEM, FREQUENCIA, DIAS_SEMANAS};
        Cursor cursor = getWritableDatabase().query(TABELA_ALARME, coluns, null, null, null, null, "upper(nome)", null);

        ArrayList<Alarme> listAlarme = new ArrayList<Alarme>();

        while (cursor.moveToNext()) {
            Alarme p = new Alarme();

            p.setId(cursor.getInt(0));
            p.setNome("Nome: " + cursor.getString(1) + "\nHorário: " + cursor.getString(2));
            p.setHorario(cursor.getString(2));
            p.setDosagem(cursor.getString(3));
            p.setFrequencia(cursor.getString(4));
            p.setDias_semana(cursor.getString(5));

            listAlarme.add(p);
        }

        return listAlarme;
    }
}
