package com.example.myhealthcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhealthcontrol.dao.AlarmeDao;
import com.example.myhealthcontrol.modelo.Alarme;

public class CadastrarRemedio extends AppCompatActivity {
    AlarmeDao alarmeDao;
    Alarme alarme, altAlarme;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        getSupportActionBar().hide();

        Intent i = getIntent();
        altAlarme = (Alarme) i.getSerializableExtra("alarme");

        Button btnVariavel = findViewById(R.id.btnVariavel);

        if (altAlarme != null) {
            EditText nomeET = findViewById(R.id.editNome);
            EditText dosagemET = findViewById(R.id.editDosagem);
            EditText frequenciaET = findViewById(R.id.eTVezesAoDia);
            EditText horarioET = findViewById(R.id.editHorario);

            TextView txtCadastro = findViewById(R.id.textCadastro);

            txtCadastro.setText("Editar");
            btnVariavel.setText("Editar");

            nomeET.setText(altAlarme.getNome());
            dosagemET.setText(altAlarme.getDosagem());
            frequenciaET.setText(altAlarme.getFrequencia());
            horarioET.setText(altAlarme.getHorario());

            alarme.setId(altAlarme.getId());
        } else {
            btnVariavel.setText("Cadastrar");
        }
    }

    public void voltar(View v) {
        startActivity(new Intent(CadastrarRemedio.this, Menu.class));
    }

    public void adicionarRemedio(View v) {
        try {
            EditText nomeET = findViewById(R.id.editNome);
            EditText dosagemET = findViewById(R.id.editDosagem);
            EditText frequenciaET = findViewById(R.id.eTVezesAoDia);
            EditText horarioET = findViewById(R.id.editHorario);

            String nome = nomeET.getText().toString();
            String dosagem = dosagemET.getText().toString();
            String frequencia = frequenciaET.getText().toString();
            String horario = horarioET.getText().toString();


            if (nome.isEmpty() || dosagem.isEmpty() || frequencia.isEmpty() || horario.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                Button btnVariavel = findViewById(R.id.btnVariavel);
                if (btnVariavel.getText().toString().equals("Cadastrar")) {
                    alarme.setNome(nome);
                    alarme.setDosagem(dosagem);
                    alarme.setFrequencia(frequencia);
                    alarme.setHorario(horario);
                    alarme.setDias_semana("todos");

                    retornoDB = alarmeDao.salvarAlarme(alarme);

                    if (retornoDB == -1) {
                        alert("Erro no banco!");
                    } else {
                        alert("Cadastro realizado com sucesso!");
                    }
                } else {
                    retornoDB = alarmeDao.alterarAlarme(alarme);
                    alarmeDao.close();

                    if(retornoDB == -1) {
                        alert("Erro ao alterar!");
                    } else {
                        alert("Atualização realizada com sucesso!");
                    }
                }
                Intent i = new Intent(CadastrarRemedio.this, Menu.class);
                startActivity(i);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ops, infelizmente algo de errado aconteceu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}