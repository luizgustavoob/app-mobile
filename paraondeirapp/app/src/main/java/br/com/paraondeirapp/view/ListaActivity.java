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
import br.com.paraondeirapp.delegate.IDelegateTask;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.constantes.IConstantesNotificacao;
import br.com.paraondeirapp.task.IndicacaoTask;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.utils.DeviceUtils;
import br.com.paraondeirapp.utils.MensagemUtils;
import br.com.paraondeirapp.utils.SharedPreferencesUtils;
import br.com.paraondeirapp.dao.impl.AvaliacaoDAO;
import br.com.paraondeirapp.dao.impl.EstabelecimentoDAO;
import br.com.paraondeirapp.task.SincronizacaoTask;
import br.com.paraondeirapp.view.interfaces.IActivity;

public class ListaActivity extends AppCompatActivity implements
        IActivity,
        AdapterView.OnItemClickListener,
        Toolbar.OnMenuItemClickListener,
        IDelegateTask {

    private ListView lvEstabelecimentos;
    private Toolbar toolbar, toolbarPesquisa;
    private EditText etPesquisa;
    private ListAdapter adapter;
    private AppParaOndeIr app;
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
                try {
                    pesquisarEstabelecimentos();
                } catch (SQLException ex){
                    ex.printStackTrace();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_banco_local:
                consultarNoBancoLocal();
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
    public void inicializarComponentes() {
        toolbar = (Toolbar) findViewById(R.id.tb_lista);
        setSupportActionBar(toolbar);

        toolbarPesquisa = (Toolbar) findViewById(R.id.tb_pesquisa);
        toolbarPesquisa.setOnMenuItemClickListener(this);
        toolbarPesquisa.inflateMenu(R.menu.menu_toolbar_pesquisa);

        etPesquisa = (EditText) findViewById(R.id.et_pesquisa);
        adapter = new ListAdapter(this, null);
        lvEstabelecimentos = (ListView) findViewById(R.id.lv_estabelecimentos);
        lvEstabelecimentos.setOnItemClickListener(this);
        registerForContextMenu(lvEstabelecimentos);
        app = AppParaOndeIr.getInstance();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra("estabelecimento", adapter.getItem(position));
        startActivity(i);
    }

    @Override
    public void executarQuandoSucesso() {
        new MensagemUtils() {
            @Override
            protected void clicouSim() {
                consultarNoBancoLocal();
            }
        }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_confirmacao),
                getString(R.string.msg_sucesso_sincronizacao), getString(R.string.ok));
    }

    @Override
    public void executarQuandoSucesso(List<Estabelecimento> lista) {
        if (lista.size() > 0) {
            List<Estabelecimento> listaFiltrada = new ArrayList<>();
            try {
                EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(this);
                for (Estabelecimento estab : lista) {
                    if (estabelecimentoDAO.findByID(estab.getIdEstabelecimento()) != null) {
                        listaFiltrada.add(estab);
                    }
                }
                estabelecimentoDAO.close();
            } catch (Exception ex) {
                MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_erro_indicacao).replace("$e", ex.getMessage()));
            }

            lvEstabelecimentos.setAdapter(null);
            app.setEstabelecimentos(listaFiltrada);
            adapter.setContext(this);
            adapter.setEstabelecimentos(listaFiltrada);
            lvEstabelecimentos.setAdapter(adapter);
            lvEstabelecimentos.requestFocus();
            app.setUltimaVisualizacao(1);
            DeviceUtils.esconderTeclado(this, etPesquisa);
        } else {
            new MensagemUtils(){
                @Override
                protected void clicouSim(){
                    consultarNoBancoLocal();
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.app_name),
                    getString(R.string.msg_indicacao_vazia), getString(R.string.ok));
        }
    }

    @Override
    public void executarQuandoErro(String erro) {
        new MensagemUtils(){
            @Override
            protected void clicouSim() {
                consultarNoBancoLocal();
            }
        }.gerarEExibirAlertDialogOK(this, getString(R.string.ops), erro, getString(R.string.ok));
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
        if (ConexaoUtils.temConexao(this)) {
            if (SharedPreferencesUtils.getIPServidor().isEmpty()){
                configurarIPServidor();
                if (!SharedPreferencesUtils.getIPServidor().isEmpty()){
                    List<Avaliacao> avaliacoes;
                    try {
                        AvaliacaoDAO dao = new AvaliacaoDAO(this);
                        avaliacoes = dao.findAll();
                        dao.close();
                    } catch (Exception ex) {
                        avaliacoes = null;
                    }
                    new SincronizacaoTask(this, this, avaliacoes).execute();
                } else {
                    new MensagemUtils().gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                            getString(R.string.msg_necessario_ip), getString(R.string.ok));
                }
            } else {
                List<Avaliacao> avaliacoes;
                try {
                    AvaliacaoDAO dao = new AvaliacaoDAO(this);
                    avaliacoes = dao.findAll();
                    dao.close();
                } catch (Exception ex) {
                    avaliacoes = null;
                }
                new SincronizacaoTask(this, this, avaliacoes).execute();
            }
        } else {
            new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    consultarNoBancoLocal();
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.msg_erro_sincronizacao), getString(R.string.ok));
        }
        lvEstabelecimentos.requestFocus();
        DeviceUtils.esconderTeclado(this, etPesquisa);
    }

    private void consultarNoBancoLocal(){
        List<Estabelecimento> estabelecimentos;
        try {
            EstabelecimentoDAO dao = new EstabelecimentoDAO(this);
            estabelecimentos = dao.findAll();
            dao.close();
        } catch (Exception ex) {
            estabelecimentos = null;
        }
        lvEstabelecimentos.setAdapter(null);
        adapter.setContext(this);
        adapter.setEstabelecimentos(estabelecimentos);
        lvEstabelecimentos.setAdapter(adapter);
        lvEstabelecimentos.requestFocus();
        app.setUltimaVisualizacao(0);
        DeviceUtils.esconderTeclado(this, etPesquisa);
    }

    private void consultarUltimasIndicacoes(){
        if (app.getEstabelecimentos().size() > 0) {
            lvEstabelecimentos.setAdapter(null);
            adapter.setContext(this);
            adapter.setEstabelecimentos(app.getEstabelecimentos());
            lvEstabelecimentos.setAdapter(adapter);
            lvEstabelecimentos.requestFocus();
            app.setUltimaVisualizacao(1);
            DeviceUtils.esconderTeclado(this, etPesquisa);
        } else {
            MensagemUtils.gerarEExibirToast(this, "Não existem estabelecimentos para exibir. Solicite novas indicações!");
        }
    }

    private void solicitarIndicacoes() {
        if (ConexaoUtils.temConexao(this)) {
            if (SharedPreferencesUtils.getIPServidor().isEmpty()) {
                this.configurarIPServidor();
                if (!SharedPreferencesUtils.getIPServidor().isEmpty()){
                    new IndicacaoTask(this, this).execute();
                } else {
                    new MensagemUtils().gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                            getString(R.string.msg_necessario_ip), getString(R.string.ok));
                }
            } else {
                new IndicacaoTask(this, this).execute();
            }
        } else {
            new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    consultarNoBancoLocal();
                }
            }.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.msg_erro_conexao), getString(R.string.ok));
        }
    }

    private void exibirTelaInicial(){
        if (app.getUltimaVisualizacao() == 1){
            consultarUltimasIndicacoes();
        } else {
            consultarNoBancoLocal();
        }
        this.etPesquisa.setText("");
        this.lvEstabelecimentos.requestFocus();
        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = manager.getActiveNotifications();
        for (int i = 0; i < notifications.length; i++) {
            if (notifications[i].getId() == IConstantesNotificacao.NOTIFICA_SINCRONIZACAO) {
                manager.cancel(notifications[i].getId());
                break;
            }
        }
    }

    private void pesquisarEstabelecimentos() throws SQLException {
        List<Estabelecimento> lista;
        try {
            EstabelecimentoDAO dao = new EstabelecimentoDAO(this);
            lista = dao.getEstabelecimentosByNomeOrEndereco(etPesquisa.getText().toString().trim());
            dao.close();
        } catch (SQLException ex) {
            lista = null;
        }

        if (lista != null){
            List<Estabelecimento> listaFinal = new ArrayList<>();

            if (app.getUltimaVisualizacao() == 1){
                for (Estabelecimento estab : lista) {
                    for (int i = 0; i < app.getEstabelecimentos().size(); i++){
                        Estabelecimento e = app.getEstabelecimentos().get(i);
                        if (estab.equals(e)){
                            listaFinal.add(estab);
                        }
                    }
                }
            } else {
                listaFinal = lista;
            }

            lvEstabelecimentos.setAdapter(null);
            adapter.setContext(this);
            adapter.setEstabelecimentos(listaFinal);
            lvEstabelecimentos.setAdapter(adapter);
        }
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
                            EditText etInner = (EditText) customDialog.findViewById(R.id.et_valordigitado);
                            String ip = etInner.getText().toString();
                            SharedPreferencesUtils.setIPServidor(ip);
                            customDialog.dismiss();
                        }

                        @Override
                        protected void clicouNao() {
                            customDialog.dismiss();
                        }
        }.gerarCustomDialog(this, getString(R.string.app_name), getString(R.string.msg_informa_ip));

        EditText et = (EditText) customDialog.findViewById(R.id.et_valordigitado);
        et.setText(SharedPreferencesUtils.getIPServidor());
        customDialog.show();
    }
}