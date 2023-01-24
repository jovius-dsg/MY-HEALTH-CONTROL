package com.example.myhealthcontrol;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhealthcontrol.dao.BancoDAO;
import com.example.myhealthcontrol.modelo.Alarme;

import java.util.Calendar;

public class CadastrarRemedio extends AppCompatActivity {
    BancoDAO alarmeDao;
    Alarme alarme, altAlarme;
    long retornoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        getSupportActionBar().hide();

        Intent i = getIntent();
        altAlarme = (Alarme) i.getSerializableExtra("alarme");

        alarme = new Alarme();
        alarmeDao = new BancoDAO(CadastrarRemedio.this);

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

        btnVariavel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        alert("Preencha todos os campos!");
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
                                NotificationChannel();

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, 19);
                                calendar.set(Calendar.MINUTE, 45);
                                calendar.set(Calendar.SECOND, 00);

//                                if (Calendar.getInstance().after(calendar)) {
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
//                                }

                                Intent intent = new Intent(CadastrarRemedio.this, MemoBroadcast.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
                                        intent, PendingIntent.FLAG_IMMUTABLE);

                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        Integer.parseInt(alarme.getFrequencia()), pendingIntent);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            pendingIntent);
                                }
                            }
                        } else {
                            alarme.setNome(nome);
                            alarme.setDosagem(dosagem);
                            alarme.setFrequencia(frequencia);
                            alarme.setHorario(horario);
                            alarme.setDias_semana("todos");

                            retornoDB = alarmeDao.alterarAlarme(alarme);
                            alarmeDao.close();

                            if (retornoDB == -1) {
                                alert("Erro ao alterar!");
                            } else {
                                alert("Atualização realizada com sucesso!");
                            }
                        }
                        Intent i = new Intent(CadastrarRemedio.this, Menu.class);
                        startActivity(i);
                    }

                } catch (Exception e) {
                    Log.e("ERROR AO SALVAR ALARME", e.getMessage());
                    alert("Ops, infelizmente algo de errado aconteceu!");
                }
            }
        });
    }

    private void NotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Frases";
            String description = "FRASES DO DIA";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void voltar(View v) {
        startActivity(new Intent(CadastrarRemedio.this, Menu.class));
    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}