package br.com.paraondeirapp.constantes;

public interface IConstantesServidor {

    static final int TIMEOUT = 15000;
    static final String LINK_SINCRONIZACAO_REGISTROS_DELETADOS = "/web-service/deletado/sincronizaDeletado";
    static final String LINK_SINCRONIZACAO_ESTADOS = "/web-service/estado/sincronizaEstado";
    static final String LINK_SINCRONIZACAO_CIDADES = "/web-service/cidade/sincronizaCidade";
    static final String LINK_SINCRONIZACAO_ENDERECOS = "/web-service/endereco/sincronizaEndereco";
    static final String LINK_SINCRONIZACAO_ESTABELECIMENTOS = "/web-service/estab/sincronizaEstab";
    static final String LINK_SINCRONIZACAO_AVALIACOES = "/web-service/avaliacao/sincronizaAvaliacao";
    static final String LINK_SINCRONIZACAO_INDICACAO = "/web-service/indicacao/";
    static final String LINK_SINCRONIZACAO_FIREBASE = "/web-service/firebase/";

    static final String LINK_MAPA = "http://maps.googleapis.com/maps/api/geocode/json?";
}