package baleroni.saveit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class ProjetosAdapter extends BaseAdapter {

    Context ctx;
    List<Projeto> projetos;
    DBAdapter dbAdapter;

    public ProjetosAdapter(Context ctx, List<Projeto> projetos) {
        this.ctx = ctx;
        dbAdapter = new DBAdapter(ctx);
        this.projetos = projetos;
    }

    private String moneyFomart(double number) {
        DecimalFormat df = new DecimalFormat("0.00");
        return "R$ " + df.format(number);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        projetos = dbAdapter.buscarProjeto(null);
    }

    @Override
    public int getCount() {
        return projetos.size();
    }

    @Override
    public Object getItem(int position) {
        return projetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Projeto projeto = projetos.get(position);

        View linha = LayoutInflater.from(ctx).inflate(R.layout.item_projeto, null);

        TextView txtNome = (TextView) linha.findViewById(R.id.txtNomeProjeto);
        TextView txtObjetivo = (TextView) linha.findViewById(R.id.txtObjetivo);
        TextView txtCorrente = (TextView)linha.findViewById(R.id.txtCorrente);

        txtNome.setText(projeto.get_nome());
        txtObjetivo.setText(moneyFomart(projeto.get_objetivo()));
        txtCorrente.setText(moneyFomart(projeto.get_corrente()));

        return linha;
    }
}
