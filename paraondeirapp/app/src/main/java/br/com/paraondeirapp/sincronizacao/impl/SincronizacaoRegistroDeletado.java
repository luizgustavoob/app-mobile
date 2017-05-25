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
import br.com.paraondeirapp.sincronizacao.SincronizacaoAbstract;

public class SincronizacaoRegistroDeletado extends SincronizacaoAbstract<RegistroDeletado> {

    private Context context;

    public SincronizacaoRegistroDeletado(Context context, ProgressDialog progressDialog, String body) {
        super(progressDialog, 1, IConstantesServidor.URL_SINCRONIZA_REGISTROS_DELETADOS, body);
        this.context = context;
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
        List<Integer> avaliacoes = new ArrayList<>();
        List<Integer> estabelecimentos = new ArrayList<>();
        List<Integer> enderecos = new ArrayList<>();
        List<Integer> cidades = new ArrayList<>();
        List<Integer> estados = new ArrayList<>();

        for (RegistroDeletado registro : lista) {
            switch (registro.getNome_tabela().toUpperCase().trim()){
                case IConstantesDatabase.TABELA_AVALIACAO:
                    avaliacoes.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ESTABELECIMENTO:
                    estabelecimentos.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ENDERECO:
                    enderecos.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_CIDADE:
                    cidades.add(registro.getChave_tabela());
                    break;
                case IConstantesDatabase.TABELA_ESTADO:
                    estados.add(registro.getChave_tabela());
                    break;
                default:
                    break;
            }
        }

        if (lista.size() > 0) {
            try {
                deletarAvaliacoes(avaliacoes);
                deletarEstabelecimentos(estabelecimentos);
                deletarEnderecos(enderecos);
                deletarCidades(cidades);
                deletarEstados(estados);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deletarAvaliacoes(List<Integer> idsEstabelecimentos) {
        try {
            AvaliacaoDAO avaliacaoDAO = new AvaliacaoDAO(context);
            avaliacaoDAO.deleteByListIdEstabelecimento(idsEstabelecimentos);
            avaliacaoDAO.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deletarEstabelecimentos(List<Integer> idsEstabelecimentos) {
        try {
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(context);
            estabelecimentoDAO.deleteByListIdEstabelecimento(idsEstabelecimentos);
            estabelecimentoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarEnderecos(List<Integer> idsEnderecos) {
        try {
            EnderecoDAO enderecoDAO = new EnderecoDAO(context);
            enderecoDAO.deleteByListIdEndereco(idsEnderecos);
            enderecoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarCidades(List<Integer> idsCidades) {
        try {
            CidadeDAO cidadeDAO = new CidadeDAO(context);
            cidadeDAO.deleteByListIdCidade(idsCidades);
            cidadeDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void deletarEstados(List<Integer> idsEstados) {
        try {
            EstadoDAO estadoDAO = new EstadoDAO(context);
            estadoDAO.deleteByListIdEstado(idsEstados);
            estadoDAO.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
