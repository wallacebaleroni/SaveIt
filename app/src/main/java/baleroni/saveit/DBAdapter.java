package baleroni.saveit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    private DBHelper helper;

    public DBAdapter(Context ctx) {
        helper = new DBHelper(ctx);
    }

    private long inserir(Projeto projeto) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_NOME, projeto.get_nome());
        cv.put(DBHelper.COLUNA_CORRENTE, projeto.get_corrente());

        long id = db.insert(DBHelper.TABLE_NAME, null, cv);
        if (id != -1) {
            projeto.id = id;
        }

        db.close();
        return id;
    }

    private int atualizar(Projeto projeto) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUNA_NOME, projeto.get_nome());
        cv.put(DBHelper.COLUNA_CORRENTE, projeto.get_corrente());

        int linhasAfetadas = db.update(
            DBHelper.TABLE_NAME,
            cv,
            DBHelper.COLUNA_ID + " = ?",
            new String[] {String.valueOf(projeto.id)});
        db.close();
        return linhasAfetadas;
    }

    public void salvar(Projeto projeto) {
        if(projeto.id == 0) {
            inserir(projeto);
        } else {
            atualizar(projeto);
        }
    }

    public int excluir(Projeto projeto) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int linhasAfetadas = db.delete(
                DBHelper.TABLE_NAME,
                DBHelper.COLUNA_ID + " = ?",
                new String[]{ String.valueOf(projeto.id)});
        db.close();
        return linhasAfetadas;
    }

    public List<Projeto> buscarProjeto(String filtro) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT * FROM " + DBHelper.TABLE_NAME;
        String[] argumentos = null;
        if (filtro != null) {
            sql += " WHERE " + DBHelper.COLUNA_NOME + " LIKE ?";
            argumentos = new String[]{ filtro };
        }
        sql += " ORDER BY " + DBHelper.COLUNA_NOME;

        Cursor cursor = db.rawQuery(sql, argumentos);

        List<Projeto> projetos = new ArrayList<Projeto>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(
                    cursor.getColumnIndex(
                            DBHelper.COLUNA_ID));
            String nome = cursor.getString(
                    cursor.getColumnIndex(
                            DBHelper.COLUNA_NOME));
            float corrente = cursor.getFloat(
                    cursor.getColumnIndex(
                            DBHelper.COLUNA_CORRENTE));
            Projeto projeto = new Projeto(id, nome);
            projeto.add_corrente(corrente);
            projetos.add(projeto);
        }
        cursor.close();
        db.close();

        return projetos;
    }
}
