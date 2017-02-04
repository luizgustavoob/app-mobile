package br.com.paraondeirapp.interfaces;

import java.util.List;

import br.com.paraondeirapp.entity.Estabelecimento;

public interface IDelegateIndicacao {

    void processarRetornoIndicacao(List<Estabelecimento> lista);
    void processarErroIndicacao(String erro);
}
