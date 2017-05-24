package br.com.paraondeirapp.delegate;

import java.util.List;

import br.com.paraondeirapp.model.Estabelecimento;

public interface IDelegateTask {

    void executarQuandoSucesso();
    void executarQuandoSucesso(List<Estabelecimento> lista);
    void executarQuandoErro(String erro);
}
