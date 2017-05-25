package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import br.com.paraondeirapp.model.Estado;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.EstadoDAO;
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoUF extends SincronizacaoAbstract<Estado> {

    private Context context;

    public SincronizacaoUF(Context context, ProgressDialog progressDialog) {
        super(progressDialog, 2, IConstantesServidor.URL_SINCRONIZA_ESTADOS, "");
        this.context = context;
    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<Estado>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<Estado> lista) {
        try {
            EstadoDAO estadoDAO = new EstadoDAO(context);
            for (int i = 0; i < lista.size(); i++){
                Estado estado = lista.get(i);
                estadoDAO.save(estado);
            }
            estadoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
