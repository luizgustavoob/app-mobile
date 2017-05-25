package br.com.paraondeirapp.sincronizacao;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.json.JSONStringer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoAvaliacao;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoCidade;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoEndereco;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoEstabelecimento;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoRegistroDeletado;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoUF;
import br.com.paraondeirapp.sincronizacao.impl.SincronizacaoUsuario;

public class ListaSincronizacao {

    private ProgressDialog progressDialog;
    private List<SincronizacaoAbstract> listaSincronizacao;

    public ListaSincronizacao(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public void sincronizarTudo(Context context, List<Avaliacao> avaliacoes) throws Exception {
        try {
            listaSincronizacao = new ArrayList<>();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            JSONStringer builder = new JSONStringer();
            builder.object();
            builder.key("data").value(formato.format(Calendar.getInstance().getTime()));
            builder.endObject();
            listaSincronizacao.add(new SincronizacaoRegistroDeletado(context, progressDialog, builder.toString()));
            listaSincronizacao.add(new SincronizacaoUF(context, progressDialog));
            listaSincronizacao.add(new SincronizacaoCidade(context, progressDialog));
            listaSincronizacao.add(new SincronizacaoEndereco(context, progressDialog));
            listaSincronizacao.add(new SincronizacaoEstabelecimento(context, progressDialog));

            Gson gson = new Gson();
            JsonElement element = null;
            String body = null;

            element = gson.toJsonTree(AppParaOndeIr.getInstance().getUser(), new TypeToken<Usuario>() {}.getType());
            body = element.getAsJsonObject().toString();
            listaSincronizacao.add(new SincronizacaoUsuario(progressDialog, body));

            if (avaliacoes != null && avaliacoes.size() > 0) {
                element = gson.toJsonTree(avaliacoes, new TypeToken<List<Avaliacao>>() {}.getType());
                body = element.getAsJsonArray().toString();
                listaSincronizacao.add(new SincronizacaoAvaliacao(context, progressDialog, body));
            }

            for (SincronizacaoAbstract sinc : listaSincronizacao) {
                sinc.sincronizar();
            }
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}
