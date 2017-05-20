package br.com.paraondeirapp.sincronizacao.impl;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import br.com.paraondeirapp.model.RegistroDeletado;
import br.com.paraondeirapp.constantes.IConstantesDatabase;
import br.com.paraondeirapp.constantes.IConstantesServidor;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.dao.impl.CidadeDAO;
import br.com.paraondeirapp.dao.impl.EnderecoDAO;
import br.com.paraondeirapp.dao.impl.EstabelecimentoDAO;
import br.com.paraondeirapp.dao.impl.EstadoDAO;
import br.com.paraondeirapp.sincronizacao.Sincronizacao;

public class SincronizacaoRegistroDeletado extends Sincronizacao<RegistroDeletado> {

    private Context ctx;

    public SincronizacaoRegistroDeletado(Context ctx, ProgressDialog progressDialog, String jsonPost) {
        super(progressDialog, 1, IConstantesServidor.LINK_SINCRONIZACAO_REGISTROS_DELETADOS, jsonPost);
        this.ctx = ctx;
    }

    @Override
    protected boolean isPost() {
        return true;
    }

    @Override
    protected Type getCollectionType() {
        return new TypeToken<List<RegistroDeletado>>() {}.getType();
    }

    @Override
    protected void salvarSincronizacao(List<RegistroDeletado> lista) {
        List<Integer> listaAvaliacao = new ArrayList<>();
        List<Integer> listaEstab = new ArrayList<>();
        List<Integer> listaEndereco = new ArrayList<>();
        List<Integer> listaCidade = new ArrayList<>();
        List<Integer> listaEstado = new ArrayList<>();

        for (RegistroDeletado registro : lista) {
            switch (registro.getNome_tabela().toUpperCase().trim()){
                case IConstantesDatabase.TABELA_AVALIACAO:
                    listaAvaliacao.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ESTABELECIMENTO:
                    listaEstab.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ENDERECO:
                    listaEndereco.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_CIDADE:
                    listaCidade.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ESTADO:
                    listaEstado.add(registro.getChave_tabela());
                    break;
                default:
                    break;
            }
        }

        if (lista.size() > 0) {
            try {
                deletarAvaliacoes(listaAvaliacao);
                deletarEstabelecimentos(listaEstab);
                deletarEnderecos(listaEndereco);
                deletarCidades(listaCidade);
                deletarEstados(listaEstado);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deletarAvaliacoes(List<Integer> chaves) {
        try {
            AvaliacaoDAO dao = new AvaliacaoDAO(ctx);
            dao.deleteByListIdEstabelecimento(chaves);
            dao.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deletarEstabelecimentos(List<Integer> chaves) {
        try {
            EstabelecimentoDAO dao = new EstabelecimentoDAO(ctx);
            dao.deleteByListIdEstabelecimento(chaves);
            dao.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarEnderecos(List<Integer> chaves) {
        try {
            EnderecoDAO dao = new EnderecoDAO(ctx);
            dao.deleteByListIdEndereco(chaves);
            dao.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarCidades(List<Integer> chaves) {
        try {
            CidadeDAO dao = new CidadeDAO(ctx);
            dao.deleteByListIdCidade(chaves);
            dao.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarEstados(List<Integer> chaves) {
        try {
            EstadoDAO dao = new EstadoDAO(ctx);
            dao.deleteByListIdEstado(chaves);
            dao.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
