package com.example.myhealthcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myhealthcontrol.dao.AlarmeDao;
import com.example.myhealthcontrol.dao.PessoaDao;
import com.example.myhealthcontrol.modelo.Alarme;
import com.example.myhealthcontrol.modelo.Pessoa;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
    ArrayList<Pessoa> arrayListPessoa;
    ArrayList<Alarme> arrayListAlarme;

    ArrayAdapter<Alarme> arrayAdapterAlarme;

    PessoaDao pessoaDao;
    AlarmeDao alarmeDao;

    Alarme alarme;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        pessoaDao = new PessoaDao(Menu.this);

        arrayListPessoa = pessoaDao.selectAllPessoa();

        if (arrayListPessoa.size() <= 0) {
            Intent i = new Intent(Menu.this, Perfil.class);
            Bundle parametros = new Bundle();
            parametros.putString("metodo", "cadastro");
            i.putExtras(parametros);
            startActivity(i);
        } else {
            TextView nomeTV = findViewById(R.id.textNomePessoa);
            nomeTV.setText(arrayListPessoa.get(0).getNome());

            lista = (ListView) findViewById(R.id.listRemedios);
            registerForContextMenu(lista);


            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Alarme alarmeEnviado = (Alarme) arrayAdapterAlarme.getItem(position);
                    Intent i = new Intent(Menu.this, Perfil.class);
                    i.putExtra("alarme", alarmeEnviado);
                    startActivity(i);
                }
            });

            lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    alarme = arrayAdapterAlarme.getItem(position);
                    return false;
                }
            });
        }
    }

    public void populaLista() {
        alarmeDao = new AlarmeDao(Menu.this);
        arrayListAlarme = alarmeDao.selectAllAlarme();
        pessoaDao.close();

        if (lista != null) {
            arrayAdapterAlarme = new ArrayAdapter<Alarme>(Menu.this, android.R.layout.simple_list_item_1, arrayListAlarme);
            lista.setAdapter(arrayAdapterAlarme);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem mDelete = menu.add("Excluir Registro");
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                long retornoDB;
                alarmeDao = new AlarmeDao(Menu.this);
                retornoDB = alarmeDao.excluirAlarme(alarme);
                alarmeDao.close();

                if (retornoDB == -1) {
                    alert("Erro ao excluir!");
                } else {
                    alert("Registro excluído com sucesso!");
                }
                populaLista();
                return false;
            }
        });
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void adicionarRemedio(View v) {
        startActivity(new Intent(Menu.this, CadastrarRemedio.class));
    }

    private void alert(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}