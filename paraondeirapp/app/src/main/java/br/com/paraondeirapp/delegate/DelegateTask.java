package br.com.paraondeirapp.delegate;

import java.util.List;

import br.com.paraondeirapp.model.Estabelecimento;

public interface DelegateTask {

    void executarQuandoSucessoNaSincronizacao();
    void executarQuandoSucessoNaIndicacao(List<Estabelecimento> estabelecimentos);
    void executarQuandoErro(String erro);
}
