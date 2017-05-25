package br.com.paraondeirapp.view;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.adapter.ListAdapter;
import br.com.paraondeirapp.delegate.DelegateTask;
import br.com.paraondeirapp.enumeration.UltimaVisualizacao;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.constantes.IConstantesNotificacao;
import br.com.paraondeirapp.async.task.IndicacaoTask;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.utils.DeviceUtils;
import br.com.paraondeirapp.utils.MensagemUtils;
import br.com.paraondeirapp.utils.SharedPreferencesUtils;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.dao.impl.EstabelecimentoDAO;
import br.com.paraondeirapp.async.task.SincronizacaoTask;

public class ListaActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        Toolbar.OnMenuItemClickListener,
        DelegateTask {

    private ListView listViewEstabelecimentos;
    private Toolbar toolbarTopo, toolbarPesquisa;
    private EditText editTextPesquisa;
    private ListAdapter listAdapter;
    private AppParaOndeIr myApp;
    private Dialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        inicializarComponentes();
        configuracoesIniciais();
    }

    @Override
    protected void onResume()  {
        super.onResume();
        exibirTelaInicial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_lista, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_lista, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_sincronizar:
                consultarNoServidor();
                break;
            case R.id.mn_indicacao:
                solicitarIndicacoes();
                break;
            case R.id.mn_setipservidor:
                configurarIPServidor();
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_limpar_pesquisa:
                exibirTelaInicial();
                break;
            case R.id.mn_pesquisar:
                pesquisarEstabelecimentos();
                break;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_banco_local:
                consultarNoBancoLocal(true);
                break;
            case R.id.mn_lista_indicacao:
                consultarUltimasIndicacoes();
                break;
            case R.id.mn_testar_conexao:
                testarConexao();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra("estabelecimento", listAdapter.getItem(position));
        startActivity(i);
    }

    @Override
    public void executarQuandoSucessoNaSincronizacao() {
        new MensagemUtils() {
            @Override
            protected void clicouSim() {
                consultarNoBancoLocal(true);
            }
        }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_confirmacao),
                getString(R.string.msg_sucesso_sincronizacao), getString(R.string.ok));
    }

    @Override
    public void executarQuandoErro(String erro) {
        new MensagemUtils(){
            @Override
            protected void clicouSim() {
                consultarNoBancoLocal(false);
            }
        }.gerarEExibirAlertDialogOK(this, getString(R.string.ops), erro, getString(R.string.ok));
    }

    @Override
    public void executarQuandoSucessoNaIndicacao(List<Estabelecimento> estabelecimentos) {
        if (estabelecimentos.size() <= 0) {
            new MensagemUtils(){
                @Override
                protected void clicouSim(){
                    consultarNoBancoLocal(false);
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.app_name),
                    getString(R.string.msg_indicacao_vazia), getString(R.string.ok));
            return;
        }

        List<Estabelecimento> estabelecimentosFiltrados = new ArrayList<>();
        try {
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(this);
            for (Estabelecimento estabelecimento : estabelecimentos) {
                if (estabelecimentoDAO.findByID(estabelecimento.getIdEstabelecimento()) != null) {
                    estabelecimentosFiltrados.add(estabelecimento);
                }
            }
            estabelecimentoDAO.close();
        } catch (SQLException ex) {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_erro_indicacao)
                    .replace("$e", ex.getMessage()));
            return;
        }

        listViewEstabelecimentos.setAdapter(null);
        myApp.setUltimosEstabsIndicacao(estabelecimentosFiltrados);
        listAdapter.setContext(this);
        listAdapter.setEstabelecimentos(estabelecimentosFiltrados);
        listViewEstabelecimentos.setAdapter(listAdapter);
        listViewEstabelecimentos.requestFocus();
        myApp.setUltimaVisualizacao(UltimaVisualizacao.ULTIMAS_INDICACOES);
        DeviceUtils.esconderTeclado(this, editTextPesquisa);
    }

    public void inicializarComponentes() {
        toolbarTopo = (Toolbar) findViewById(R.id.tb_lista);
        setSupportActionBar(toolbarTopo);

        toolbarPesquisa = (Toolbar) findViewById(R.id.tb_pesquisa);
        toolbarPesquisa.setOnMenuItemClickListener(this);
        toolbarPesquisa.inflateMenu(R.menu.menu_toolbar_pesquisa);

        editTextPesquisa = (EditText) findViewById(R.id.et_pesquisa);
        listAdapter = new ListAdapter(this, null);
        listViewEstabelecimentos = (ListView) findViewById(R.id.lv_estabelecimentos);
        listViewEstabelecimentos.setOnItemClickListener(this);
        registerForContextMenu(listViewEstabelecimentos);
        myApp = AppParaOndeIr.getInstance();
    }

    private void configuracoesIniciais() {
        if (SharedPreferencesUtils.isPrimeiroAcesso()){
            if (SharedPreferencesUtils.getIPServidor().isEmpty()){
                configurarIPServidor();
                if (!SharedPreferencesUtils.getIPServidor().isEmpty()){
                    consultarNoServidor();
                }
                SharedPreferencesUtils.setPrimeiroAcesso(false);
            } else {
                consultarNoServidor();
                SharedPreferencesUtils.setPrimeiroAcesso(false);
            }
        } else if (SharedPreferencesUtils.getIPServidor().isEmpty()){
            configurarIPServidor();
            if (!SharedPreferencesUtils.getIPServidor().isEmpty()){
                consultarNoServidor();
            }
        }
    }

    private void consultarNoServidor(){
        if (!ConexaoUtils.temConexao(this)) {
            new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    consultarNoBancoLocal(false);
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.msg_erro_sincronizacao), getString(R.string.ok));
            return;
        }

        if (SharedPreferencesUtils.getIPServidor().isEmpty()) {
            configurarIPServidor();
            if (SharedPreferencesUtils.getIPServidor().isEmpty()) {
                new MensagemUtils().gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                        getString(R.string.msg_necessario_ip), getString(R.string.ok));
                return;
            }
        }

        List<Avaliacao> avaliacoes;
        try {
            AvaliacaoDAO dao = new AvaliacaoDAO(this);
            avaliacoes = dao.findAll();
            dao.close();
        } catch (Exception ex) {
            avaliacoes = null;
        }

        new SincronizacaoTask(this, this).execute(avaliacoes);

        listViewEstabelecimentos.requestFocus();
        DeviceUtils.esconderTeclado(this, editTextPesquisa);
    }

    private void consultarNoBancoLocal(boolean forcarConsultaNoBD){
        if (myApp.getEstabsEmCache().size() <= 0 || forcarConsultaNoBD) {
            try {
                EstabelecimentoDAO dao = new EstabelecimentoDAO(this);
                List<Estabelecimento> estabelecimentos = dao.findAll();
                dao.close();
                myApp.setEstabsEmCache(estabelecimentos);
            } catch (Exception ex) {
                return;
            }
        }

        listViewEstabelecimentos.setAdapter(null);
        listAdapter.setContext(this);
        listAdapter.setEstabelecimentos(myApp.getEstabsEmCache());
        listViewEstabelecimentos.setAdapter(listAdapter);
        listViewEstabelecimentos.requestFocus();
        myApp.setUltimaVisualizacao(UltimaVisualizacao.BD_LOCAL);
        DeviceUtils.esconderTeclado(this, editTextPesquisa);
    }

    private void consultarUltimasIndicacoes(){
        if (myApp.getUltimosEstabsIndicacao().size() <= 0){
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_sem_estabs));
            return;
        }

        listViewEstabelecimentos.setAdapter(null);
        listAdapter.setContext(this);
        listAdapter.setEstabelecimentos(myApp.getUltimosEstabsIndicacao());
        listViewEstabelecimentos.setAdapter(listAdapter);
        listViewEstabelecimentos.requestFocus();
        myApp.setUltimaVisualizacao(UltimaVisualizacao.ULTIMAS_INDICACOES);
        DeviceUtils.esconderTeclado(this, editTextPesquisa);
    }

    private void solicitarIndicacoes() {
        if (!ConexaoUtils.temConexao(this)) {
            new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    consultarNoBancoLocal(false);
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.msg_erro_conexao), getString(R.string.ok));
            return;
        }

        if (SharedPreferencesUtils.getIPServidor().isEmpty()) {
            configurarIPServidor();
            if (SharedPreferencesUtils.getIPServidor().isEmpty()) {
                new MensagemUtils().gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                        getString(R.string.msg_necessario_ip), getString(R.string.ok));
                return;
            }
        }

        new IndicacaoTask(this, this).execute(myApp.getUser());
    }

    private void exibirTelaInicial(){
        if (myApp.getUltimaVisualizacao() == UltimaVisualizacao.ULTIMAS_INDICACOES){
            consultarUltimasIndicacoes();
        } else {
            consultarNoBancoLocal(false);
        }

        this.editTextPesquisa.setText("");
        this.listViewEstabelecimentos.requestFocus();
        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = manager.getActiveNotifications();
        for (int i = 0; i < notifications.length; i++) {
            if (notifications[i].getId() == IConstantesNotificacao.NOTIFICA_SINCRONIZACAO) {
                manager.cancel(notifications[i].getId());
                break;
            }
        }
    }

    private void pesquisarEstabelecimentos() {
        List<Estabelecimento> estabelecimentos;
        try {
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(this);
            estabelecimentos = estabelecimentoDAO.findEstabelecimentosByNomeOrEndereco(editTextPesquisa.getText().toString().trim());
            estabelecimentoDAO.close();
        } catch (SQLException ex) {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_erro_pesquisa)
                    .replace("$e", ex.getMessage()));
            return;
        }

        List<Estabelecimento> estabelecimentosFiltrados = new ArrayList<>();
        if (myApp.getUltimaVisualizacao() == UltimaVisualizacao.ULTIMAS_INDICACOES){
            for (Estabelecimento estabelecimento : estabelecimentos) {
                for (int i = 0; i < myApp.getUltimosEstabsIndicacao().size(); i++){
                    Estabelecimento local = myApp.getUltimosEstabsIndicacao().get(i);
                    if (estabelecimento.equals(local)){
                        estabelecimentosFiltrados.add(estabelecimento);
                    }
                }
            }
        } else {
            estabelecimentosFiltrados = estabelecimentos;
        }

        listViewEstabelecimentos.setAdapter(null);
        listAdapter.setContext(this);
        listAdapter.setEstabelecimentos(estabelecimentosFiltrados);
        listViewEstabelecimentos.setAdapter(listAdapter);
    }

    private void testarConexao(){
        if (ConexaoUtils.temConexao(this)) {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_sucesso_conexao));
        } else {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_erro_conexao));
        }
    }

    private void configurarIPServidor() {
        customDialog = new MensagemUtils(){
                        @Override
                        protected void clicouSim() {
                            EditText editTextDepoisDoOK = (EditText) customDialog.findViewById(R.id.et_valordigitado);
                            String ipServidor = editTextDepoisDoOK.getText().toString();
                            SharedPreferencesUtils.setIPServidor(ipServidor);
                            customDialog.dismiss();
                        }

                        @Override
                        protected void clicouNao() {
                            customDialog.dismiss();
                        }
        }.gerarCustomDialog(this, getString(R.string.app_name), getString(R.string.msg_informa_ip));

        EditText editTextAtual = (EditText) customDialog.findViewById(R.id.et_valordigitado);
        editTextAtual.setText(SharedPreferencesUtils.getIPServidor());
        customDialog.show();
    }
}