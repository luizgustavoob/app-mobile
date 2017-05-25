package br.com.paraondeirapp.constantes;

public interface IConstantesDatabase {

    static final String NOME_DATABASE = "paraondeir_db";
    static final int VERSAO_DATABASE = 1;

    static final String TABELA_AVALIACAO = "AVALIACAO",
            AVALIACAO_ID = "IDAVALIACAO",
            AVALIACAO_ESTABELECIMENTO = "IDESTABELECIMENTO",
            AVALIACAO_LIKE = "GOSTOU",
            AVALIACAO_USER = "USUARIO",
            AVALIACAO_DTAVALIACAO = "DATA";

    static final String TABELA_ESTADO = "UF",
            ESTADO_ID = "IDUF",
            ESTADO_NOME = "NOME",
            ESTADO_SIGLA = "SIGLA";

    static final String TABELA_CIDADE = "CIDADE",
            CIDADE_ID = "IDCIDADE",
            CIDADE_NOME = "NOME",
            CIDADE_IDESTADO = "IDUF";

    static final String TABELA_ENDERECO = "ENDERECO",
            ENDERECO_ID = "IDEND",
            ENDERECO_LOGRADOURO = "LOGRADOURO",
            ENDERECO_BAIRRO = "BAIRRO",
            ENDERECO_NUMERO = "NUMERO",
            ENDERECO_CEP = "CEP",
            ENDERECO_IDCIDADE = "IDCIDADE";

    static final String TABELA_ESTABELECIMENTO = "ESTABELECIMENTO",
            ESTABELECIMENTO_ID = "IDESTABELECIMENTO",
            ESTABELECIMENTO_NOME = "NOME",
            ESTABELECIMENTO_IDEND = "IDEND",
            ESTABELECIMENTO_TELEFONE = "TELEFONE",
            ESTABELECIMENTO_IMAGEM = "IMAGEM";
}
