package baleroni.saveit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBAdapter dbAdapter;
    List<Projeto> projetos;
    ListView listView;
    FloatingActionButton fab;
    ProjetosAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(this);
        projetos = dbAdapter.buscarProjeto(null);

        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new ProjetosAdapter(this, projetos);
        listView.setAdapter(adapter);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Alterar depois para a chamada da próxima activity
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Projeto projeto = (Projeto) adapterView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Nome: " + projeto.nome + "\n" +
                                                  "Objetivo: " + projeto.objetivo + "\n" +
                                                  "Corrente: " + projeto.corrente,
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        registerForContextMenu(listView);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateNovoProjetoDialog();
            }
        });
    }

    public Dialog onCreateNovoProjetoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog_view = inflater.inflate(R.layout.dialog_novo_projeto, null);

        builder.setView(dialog_view)
            .setTitle(R.string.novo_projeto)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText editText_nome = (EditText) dialog_view.findViewById(R.id.editText_nome);
                    String nome = editText_nome.getText().toString();
                    if (nome.equals("")) {
                        Toast.makeText(MainActivity.this, R.string.nome_vazio, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Projeto novo_projeto = new Projeto(nome);

                    dbAdapter.salvar(novo_projeto);
                    atualizar_projetos();
                }
            })
            .setNegativeButton(R.string.cancelit, null);
        builder.show();
        return builder.create();
    }

    public Dialog onCreateAlterarNomeDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog_view = inflater.inflate(R.layout.dialog_alterar_nome, null);
        final int i = index;

        builder.setView(dialog_view)
                .setTitle(R.string.alterar_nome)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText_nome = (EditText) dialog_view.findViewById(R.id.editText_novo_nome);
                        String novo_nome = editText_nome.getText().toString();
                        if (novo_nome.equals("")) {
                            Toast.makeText(MainActivity.this, R.string.nome_vazio, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dbAdapter.salvar(projetos.get(i).mudar_nome(novo_nome));
                        atualizar_projetos();
                    }
                })
                .setNegativeButton(R.string.cancelit, null);
        builder.show();
        return builder.create();
    }

    public Dialog onCreateExcluirDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog_view = inflater.inflate(R.layout.dialog_excluir, null);
        final int i = index;

        builder.setView(dialog_view)
                .setTitle(R.string.excluir)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dbAdapter.excluir(projetos.get(i));
                        atualizar_projetos();
                    }
                })
                .setNegativeButton(R.string.cancelit, null);
        builder.show();
        return builder.create();
    }

    private void atualizar_projetos() {
        projetos = dbAdapter.buscarProjeto(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        if (view.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(projetos.get(info.position).get_nome());
            String[] menuItems = getResources().getStringArray(R.array.context_menu_options);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        if(item.getItemId() == 0) {
            onCreateAlterarNomeDialog(index);

        }
        else if(item.getItemId() == 1) {
            onCreateExcluirDialog(index);
        }
        else {
            return false;
        }

        return true;
    }
}
