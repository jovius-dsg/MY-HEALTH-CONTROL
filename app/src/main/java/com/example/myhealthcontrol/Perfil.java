package com.example.myhealthcontrol;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhealthcontrol.dao.BancoDAO;
import com.example.myhealthcontrol.modelo.Pessoa;

import java.util.ArrayList;

public class Perfil extends AppCompatActivity {
    BancoDAO bancoDAO;
    long retornoDB;
    String resultado;
    ArrayList<Pessoa> arrayListPessoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().hide();
    }

    public void criarUsuario(View v) {
        try {
            EditText nomeET = findViewById(R.id.editNomePessoa);
            String nome = nomeET.getText().toString();

            NotificationChannel();

            if (nome.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(nome);
                bancoDAO = new BancoDAO(Perfil.this);


                retornoDB = bancoDAO.salvarPessoa(pessoa);

                if (retornoDB == -1) {
                    resultado = "Erro no banco";
                } else {
                    resultado = "Usuário salvo com sucesso!";
                }

                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                arrayListPessoa = bancoDAO.selectAllPessoa();

                Intent i = new Intent(Perfil.this, Menu.class);
                Bundle parametros = new Bundle();
                parametros.putString("nome", arrayListPessoa.get(0).getNome());
                i.putExtras(parametros);
                startActivity(i);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ops, infelizmente algo de errado aconteceu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void NotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification", "Remédio", importance);
            channel.setDescription("Notificações dos remédios");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}