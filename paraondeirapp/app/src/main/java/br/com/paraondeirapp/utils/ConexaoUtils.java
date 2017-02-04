package br.com.paraondeirapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.paraondeirapp.interfaces.IConstantesServidorSinc;

public class ConexaoUtils {

    /**
     * Verifica se o dispositivo possui conexão com a rede.
     * @param ctx
     * @return tem conexão/não tem conexão.
     */
    public static boolean temConexao(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Formata a URL a ser usada na requisição, adicionando o IP do servidor ao contexto passado por parâmetro.
     * @param link - Contexto da requisição
     * @return - Link formatado
     */
    public static String formatarLinkConexao(String link){
        SharedPreferencesUtils shared = new SharedPreferencesUtils();
        return "http://" + shared.getIPServidor() + link;
    }

    /**
     * Realiza conexão com servidores externos através do método GET.
     * @param urlConexao - link do servidor externo.
     * @return - Stream obtida do servidor externo.
     * @throws Exception
     */
    public static InputStream get(String urlConexao) throws Exception {
        InputStream is = null;
        try {
            URL url = new URL(formatarLinkConexao(urlConexao));
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setReadTimeout(IConstantesServidorSinc.TIMEOUT);
            conexao.setConnectTimeout(IConstantesServidorSinc.TIMEOUT);
            conexao.setRequestProperty("Content-Type", "application/json;charset=utf8");
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setRequestProperty("Method", "GET");
            conexao.setDoInput(true);
            conexao.setDoOutput(false);
            conexao.connect();
            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conexao.getInputStream();
            }
        } catch (Exception ex){
            is = null;
            throw new Exception(ex.getMessage());
        }
        return is;
    }


    /**
     * Realiza conexão com servidores externos através do método POST.
     * @param urlConexao - link do servidor externo.
     * @param jsonEnvio - Corpo da requisição.
     * @return - Stream obtida do servidor externo.
     * @throws Exception
     */
    public static InputStream post(String urlConexao, String jsonEnvio) throws Exception{
        InputStream is = null;
        try {
            URL url = new URL(formatarLinkConexao(urlConexao));
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setReadTimeout(IConstantesServidorSinc.TIMEOUT);
            conexao.setConnectTimeout(IConstantesServidorSinc.TIMEOUT);
            conexao.setRequestProperty("Content-Type", "application/json;charset=utf8");
            conexao.setRequestProperty("Accept", "application/json");
            conexao.setRequestProperty("Method", "POST");
            conexao.setDoInput(true);
            conexao.setDoOutput(true);

            OutputStream os = conexao.getOutputStream();
            os.write(jsonEnvio.toString().getBytes("UTF-8"));
            os.close();

            conexao.connect();
            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conexao.getInputStream();
            }
        } catch (Exception ex){
            is = null;
            throw new Exception(ex.getMessage());
        }
        return is;
    }

}