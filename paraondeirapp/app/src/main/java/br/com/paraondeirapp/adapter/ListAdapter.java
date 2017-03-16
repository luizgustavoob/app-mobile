package br.com.paraondeirapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.model.Estabelecimento;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<Estabelecimento> estabelecimentos;

    public ListAdapter(Context context, List<Estabelecimento> lista){
        this.context = context;
        this.estabelecimentos = lista;
    }

    public List<Estabelecimento> getEstabelecimentos() {
        return estabelecimentos;
    }

    public void setEstabelecimentos(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return estabelecimentos.size();
    }

    @Override
    public Estabelecimento getItem(int position) {
        return estabelecimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return estabelecimentos.get(position).getIdEstabelecimento();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.elemento_lista, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.associar((Estabelecimento) getItem(position));
        return view;
    }

    private static class ViewHolder {

        private TextView tvNome, tvEndereco, tvCidade;

        public ViewHolder(View view){
            tvNome = (TextView) view.findViewById(R.id.tv_nome_estab);
            tvEndereco = (TextView) view.findViewById(R.id.tv_endereco_estab);
            tvCidade = (TextView) view.findViewById(R.id.tv_cidade_estab);
        }

        public void associar(Estabelecimento estabelecimento){
            tvNome.setText(estabelecimento.getNome());
            tvEndereco.setText(estabelecimento.getEndereco().toString());
            tvCidade.setText(estabelecimento.getEndereco().getCidade().toString());
        }
    }
}
