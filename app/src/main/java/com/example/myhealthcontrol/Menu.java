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

import com.example.myhealthcontrol.dao.BancoDAO;
import com.example.myhealthcontrol.modelo.Alarme;
import com.example.myhealthcontrol.modelo.Pessoa;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
    ArrayList<Pessoa> arrayListPessoa;
    ArrayList<Alarme> arrayListAlarme;

    ArrayAdapter<Alarme> arrayAdapterAlarme;

    BancoDAO bancoDAO;

    Alarme alarme;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        bancoDAO = new BancoDAO(Menu.this);

        arrayListPessoa = bancoDAO.selectAllPessoa();

        if (arrayListPessoa.size() <= 0) {
            Intent i = new Intent(Menu.this, Perfil.class);
            Bundle parametros = new Bundle();
            parametros.putString("metodo", "cadastro");
            i.putExtras(parametros);
            startActivity(i);
        } else {
            TextView nomeTV = findViewById(R.id.textNomePessoa);
            nomeTV.setText(arrayListPessoa.get(0).getNome());

            lista = findViewById(R.id.listRemedios);
            registerForContextMenu(lista);


            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Alarme alarmeEnviado = arrayAdapterAlarme.getItem(position);
                    Intent i = new Intent(Menu.this, CadastrarRemedio.class);
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
        bancoDAO = new BancoDAO(Menu.this);
        arrayListAlarme = bancoDAO.selectAllAlarme();
        bancoDAO.close();

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
                bancoDAO = new BancoDAO(Menu.this);
                retornoDB = bancoDAO.excluirAlarme(alarme);
                bancoDAO.close();

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