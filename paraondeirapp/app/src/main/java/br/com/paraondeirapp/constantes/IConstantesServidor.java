package br.com.paraondeirapp.constantes;

public interface IConstantesServidor {

    static final int TIMEOUT = 30000;
    static final String URL_SINCRONIZA_REGISTROS_DELETADOS = "/web-service/deletado/sincronizaDeletado";
    static final String URL_SINCRONIZA_ESTADOS = "/web-service/estado/sincronizaEstado";
    static final String URL_SINCRONIZA_CIDADES = "/web-service/cidade/sincronizaCidade";
    static final String URL_SINCRONIZA_ENDERECOS = "/web-service/endereco/sincronizaEndereco";
    static final String URL_SINCRONIZA_ESTABELECIMENTOS = "/web-service/estab/sincronizaEstab";
    static final String URL_SINCRONIZA_AVALIACOES = "/web-service/avaliacao/sincronizaAvaliacao";
    static final String URL_SINCRONIZA_USUARIO = "/web-service/usuario/";
    static final String URL_SOLICITA_INDICACAO = "/web-service/indicacao/";
}