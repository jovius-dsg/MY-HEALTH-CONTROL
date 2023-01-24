package com.example.myhealthcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhealthcontrol.dao.PessoaDao;
import com.example.myhealthcontrol.modelo.Pessoa;

import java.util.ArrayList;

public class Perfil extends AppCompatActivity {
    PessoaDao pessoaDao;
    Pessoa pessoa;
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

            if (nome.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                Pessoa pessoa = new Pessoa();
                Log.e("NOME", nome);
                pessoa.setNome(nome);
                pessoaDao = new PessoaDao(Perfil.this);


                retornoDB = pessoaDao.salvarPessoa(pessoa);

                if (retornoDB == -1) {
                    resultado = "Erro no banco";
                } else {
                    resultado = "Usuário salvo com sucesso!";
                }

                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                arrayListPessoa = pessoaDao.selectAllPessoa();

                Intent i = new Intent(Perfil.this, Menu.class);
                Bundle parametros = new Bundle();
                parametros.putString("nome", arrayListPessoa.get(0).getNome());
                i.putExtras(parametros);
                startActivity(i);
            }

        } catch (Exception e) {
            Log.e("ERORRRRR", e.toString());
            Toast.makeText(this, "Ops, infelizmente algo de errado aconteceu!", Toast.LENGTH_SHORT).show();
        }
    }
}