package br.com.paraondeirapp.delegate;

import java.util.List;

import br.com.paraondeirapp.model.Estabelecimento;

public interface IDelegateIndicacao {

    void processarRetornoIndicacao(List<Estabelecimento> lista);
    void processarErroIndicacao(String erro);
}
