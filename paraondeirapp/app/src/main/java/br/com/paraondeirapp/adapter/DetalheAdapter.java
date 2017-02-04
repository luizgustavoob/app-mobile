package br.com.paraondeirapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.paraondeirapp.R;
import br.com.paraondeirapp.entity.Estabelecimento;

public class DetalheAdapter extends BaseAdapter {

    private Context ctx;
    private Estabelecimento estabelecimento;

    public DetalheAdapter(Context ctx, Estabelecimento estab) {
        this.ctx = ctx;
        this.estabelecimento = estab;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Estabelecimento getItem(int position) {
        return this.estabelecimento;
    }

    @Override
    public long getItemId(int position) {
        return this.estabelecimento.getIdEstabelecimento();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.elemento_detalhe, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //Seta atributos do estabelecimento na tela.
        holder.associar(getItem(position));
        return view;
    }

    private static class ViewHolder {

        private TextView tvNome, tvEndereco, tvCidade, tvTelefone;

        public ViewHolder(View view){
            tvNome = (TextView) view.findViewById(R.id.tv_detalhe_nome);
            tvEndereco = (TextView) view.findViewById(R.id.tv_detalhe_endereco);
            tvCidade = (TextView) view.findViewById(R.id.tv_detalhe_cidade);
            tvTelefone = (TextView) view.findViewById(R.id.tv_detalhe_telefone);
        }

        public void associar(Estabelecimento estabelecimento){
            tvNome.setText(estabelecimento.getNome());
            tvEndereco.setText(estabelecimento.getEndereco().toString());
            tvCidade.setText(estabelecimento.getEndereco().getCidade().toString());
            tvTelefone.setText(estabelecimento.getTelefone());
        }
    }
}
