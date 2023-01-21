package com.example.myhealthcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Teste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);
        getSupportActionBar().hide();


        Button teste1 = (Button) findViewById(R.id.btnTelaCadastrar);
        teste1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teste.this, CadastrarRemedio.class);
                startActivity(intent);
            }
        });

        Button teste2 = (Button) findViewById(R.id.btnTelaMenu);
        teste2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teste.this, Menu.class);
                startActivity(intent);
            }
        });

        Button teste3 = (Button) findViewById(R.id.btnTelaPerfil);
        teste3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teste.this, Perfil.class);
                startActivity(intent);
            }
        });


    }
}