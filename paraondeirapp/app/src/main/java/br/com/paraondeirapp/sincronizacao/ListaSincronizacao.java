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
    private List<Sincronizacao> listaSincronizacao;

    public ListaSincronizacao(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public void sincronizarTudo(Context ctx, List<Avaliacao> listaAvaliacao) throws Exception {
        try {
            listaSincronizacao = new ArrayList<>();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            JSONStringer builder = new JSONStringer();
            builder.object();
            builder.key("data").value(formato.format(Calendar.getInstance().getTime()));
            builder.endObject();
            listaSincronizacao.add(new SincronizacaoRegistroDeletado(ctx, progressDialog, builder.toString()));
            listaSincronizacao.add(new SincronizacaoUF(ctx, progressDialog));
            listaSincronizacao.add(new SincronizacaoCidade(ctx, progressDialog));
            listaSincronizacao.add(new SincronizacaoEndereco(ctx, progressDialog));
            listaSincronizacao.add(new SincronizacaoEstabelecimento(ctx, progressDialog));

            Gson gson = new Gson();
            JsonElement element = null;
            String jsonPost = null;

            element = gson.toJsonTree(AppParaOndeIr.getInstance().getUser(), new TypeToken<Usuario>() {}.getType());
            jsonPost = element.getAsJsonObject().toString();
            listaSincronizacao.add(new SincronizacaoUsuario(ctx, progressDialog, jsonPost));

            if (listaAvaliacao != null && listaAvaliacao.size() > 0) {
                element = gson.toJsonTree(listaAvaliacao, new TypeToken<List<Avaliacao>>() {}.getType());
                jsonPost = element.getAsJsonArray().toString();
                listaSincronizacao.add(new SincronizacaoAvaliacao(ctx, progressDialog, jsonPost));
            }

            for (Sincronizacao sinc : listaSincronizacao) {
                sinc.sincronizar();
            }
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
}
