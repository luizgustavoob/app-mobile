package br.com.paraondeirapp.view;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.adapter.ListAdapter;
import br.com.paraondeirapp.entity.Avaliacao;
import br.com.paraondeirapp.entity.Estabelecimento;
import br.com.paraondeirapp.enumeration.TipoConsultaEstabelecimento;
import br.com.paraondeirapp.interfaces.IConstantesDatabase;
import br.com.paraondeirapp.interfaces.IConstantesNotificacao;
import br.com.paraondeirapp.interfaces.IDelegateIndicacao;
import br.com.paraondeirapp.servidor.indicacao.SolicitaIndicacao;
import br.com.paraondeirapp.utils.ConexaoUtils;
import br.com.paraondeirapp.utils.DeviceUtils;
import br.com.paraondeirapp.utils.MensagemUtils;
import br.com.paraondeirapp.utils.SharedPreferencesUtils;
import br.com.paraondeirapp.interfaces.IDelegateSinc;
import br.com.paraondeirapp.persistence.dao.AvaliacaoDAO;
import br.com.paraondeirapp.persistence.dao.EstabelecimentoDAO;
import br.com.paraondeirapp.servidor.sincronizacao.RealizaSincronizacao;
import br.com.paraondeirapp.view.interfaces.IActivity;

public class ListaActivity extends AppCompatActivity implements
        IActivity,
        AdapterView.OnItemClickListener,
        Toolbar.OnMenuItemClickListener,
        IDelegateSinc,
        IDelegateIndicacao {

    private ListView lvEstabelecimentos;
    private Toolbar toolbar, toolbarPesquisa;
    private EditText etPesquisa;
    private ListAdapter adapter;
    private AppParaOndeIr app;
    private SharedPreferencesUtils shared;
    private Dialog customDialog;
    private ListaActivity listaActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        inicializarComponentes();

        if (shared.isPrimeiroAcesso()){
            if (shared.getIPServidor().isEmpty()){
                MensagemUtils mu = new MensagemUtils(){
                    @Override
                    protected void clicouSim() {
                        EditText temp = (EditText) customDialog.findViewById(R.id.et_valordigitado);
                        String ip = temp.getText().toString();
                        shared.setIPServidor(ip);
                        executar(TipoConsultaEstabelecimento.SINCRONIZACAO);
                        shared.setPrimeiroAcesso(false);
                        customDialog.dismiss();
                    }

                    @Override
                    protected void clicouNao() {
                        customDialog.dismiss();
                    }
                };

                customDialog = mu.gerarCustomDialog(this, getString(R.string.app_name), getString(R.string.informa_ip));
                customDialog.show();
            } else {
                executar(TipoConsultaEstabelecimento.SINCRONIZACAO);
                shared.setPrimeiroAcesso(false);
            }
        }
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
                executar(TipoConsultaEstabelecimento.SINCRONIZACAO);
                break;
            case R.id.mn_indicacao:
                executar(TipoConsultaEstabelecimento.SOLICITACAO);
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
                executar(TipoConsultaEstabelecimento.BDLOCAL);
                break;
            case R.id.mn_lista_indicacao:
                executar(TipoConsultaEstabelecimento.ULTIMA_INDICACAO);
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
        shared = new SharedPreferencesUtils();
        listaActivity = this;

        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.cancel(IConstantesNotificacao.NOTIFICA_SINCRONIZACAO);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetalheActivity.class);
        i.putExtra("estabelecimento", adapter.getItem(position));
        startActivity(i);
    }

    // IDelegateSinc
    @Override
    public void processarRetornoSinc() {
        MensagemUtils mu = new MensagemUtils() {
            @Override
            protected void clicouSim() {
                executar(TipoConsultaEstabelecimento.BDLOCAL);
            }
        };

        mu.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_confirmacao),
                getString(R.string.sincronizacao_sucesso), getString(R.string.ok));
    }

    @Override
    public void processarErroSinc(String erro) {
        MensagemUtils mu = new MensagemUtils(){
            @Override
            protected void clicouSim() {
                executar(TipoConsultaEstabelecimento.BDLOCAL);
            }
        };

        mu.gerarEExibirAlertDialogOK(this, getString(R.string.ops), erro, getString(R.string.ok));
    }

    // IDelegateIndicacao
    @Override
    public void processarRetornoIndicacao(List<Estabelecimento> lista) {
        List<Estabelecimento> listaFiltrada = new ArrayList<>();
        try {
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(this);
            for (Estabelecimento estab : lista) {
                if (estabelecimentoDAO.findByID(estab.getIdEstabelecimento()) != null){
                    listaFiltrada.add(estab);
                }
            }
            estabelecimentoDAO.close();
        } catch (Exception ex){
            MensagemUtils.gerarEExibirToast(this, "Erro na geração de indicações. Detalhe: " + ex.getMessage());
        }

        lvEstabelecimentos.setAdapter(null);
        app.setEstabelecimentos(listaFiltrada);
        adapter.setContext(this);
        adapter.setEstabelecimentos(listaFiltrada);
        lvEstabelecimentos.setAdapter(adapter);
        lvEstabelecimentos.requestFocus();

        app.setUltimaVisualizacao(1);

        DeviceUtils.esconderTeclado(this, etPesquisa);
    }

    @Override
    public void processarErroIndicacao(String erro) {
        MensagemUtils mu = new MensagemUtils(){
            @Override
            protected void clicouSim(){
                executar(TipoConsultaEstabelecimento.BDLOCAL);
            }
        };

        mu.gerarEExibirAlertDialogOK(this, getString(R.string.ops), erro, getString(R.string.ok));
    }

    public void executar(TipoConsultaEstabelecimento tipoConsulta){
        app.setTipoConsulta(tipoConsulta);
        app.getTipoConsulta().executar(this);
    }

    public void consultarNoServidor(){
        if (ConexaoUtils.temConexao(this)) {
            if (shared.getIPServidor().isEmpty()){
                MensagemUtils mu = new MensagemUtils(){
                    @Override
                    protected void clicouSim() {
                        List<Avaliacao> avaliacoes;
                        try {
                            AvaliacaoDAO dao = new AvaliacaoDAO(listaActivity);
                            avaliacoes = dao.findAll();
                            dao.close();
                        } catch (Exception ex) {
                            avaliacoes = null;
                        }
                        new RealizaSincronizacao(listaActivity, listaActivity, avaliacoes);
                        customDialog.dismiss();
                    }

                    @Override
                    protected void clicouNao() {
                        customDialog.dismiss();
                    }
                };

                customDialog = mu.gerarCustomDialog(this, getString(R.string.app_name), getString(R.string.informa_ip));
                customDialog.show();
            } else {
                List<Avaliacao> avaliacoes;
                try {
                    AvaliacaoDAO dao = new AvaliacaoDAO(this);
                    avaliacoes = dao.findAll();
                    dao.close();
                } catch (Exception ex) {
                    avaliacoes = null;
                }
                new RealizaSincronizacao(this, this, avaliacoes);
            }
        } else {
            MensagemUtils mu = new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    executar(TipoConsultaEstabelecimento.BDLOCAL);
                }
            };

            mu.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.erro_sincronizacao), getString(R.string.ok));
        }
        lvEstabelecimentos.requestFocus();
        DeviceUtils.esconderTeclado(this, etPesquisa);
    }

    public void consultarNoBancoLocal(){
        List<Estabelecimento> estabelecimentos = new ArrayList<>();
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

    private void testarConexao(){
        if (ConexaoUtils.temConexao(this)) {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.conexao_ok));
        } else {
            MensagemUtils.gerarEExibirToast(this, getString(R.string.conexao_erro));
        }
    }

    private void pesquisarEstabelecimentos() throws SQLException {
        List<Estabelecimento> lista = null;
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

    private void exibirTelaInicial(){
        if (app.getUltimaVisualizacao() == 1){
            executar(TipoConsultaEstabelecimento.ULTIMA_INDICACAO);
        } else {
            executar(TipoConsultaEstabelecimento.BDLOCAL);
        }
        this.etPesquisa.setText("");
        this.lvEstabelecimentos.requestFocus();
    }

    public void solicitarIndicacoes() {
        if (ConexaoUtils.temConexao(this)) {
            if (shared.getIPServidor().isEmpty()) {
                MensagemUtils mu = new MensagemUtils(){
                    @Override
                    protected void clicouSim() {
                        new SolicitaIndicacao(listaActivity, listaActivity, app.getUser());
                        customDialog.dismiss();
                    }

                    @Override
                    protected void clicouNao() {
                        customDialog.dismiss();
                    }
                };

                customDialog = mu.gerarCustomDialog(this, getString(R.string.app_name), getString(R.string.informa_ip));
                customDialog.show();
            } else {
                new SolicitaIndicacao(listaActivity, listaActivity, app.getUser());
            }
        } else {
            MensagemUtils mu = new MensagemUtils(){
                @Override
                protected void clicouSim() {
                    executar(TipoConsultaEstabelecimento.BDLOCAL);
                }
            };

            mu.gerarEExibirAlertDialogOK(this, getString(R.string.titulo_erro_conexao),
                    getString(R.string.erro_conexao), getString(R.string.ok));
        }
    }

    public void consultarUltimasIndicacoes(){
        lvEstabelecimentos.setAdapter(null);
        adapter.setContext(this);
        adapter.setEstabelecimentos(app.getEstabelecimentos());
        lvEstabelecimentos.setAdapter(adapter);
        lvEstabelecimentos.requestFocus();

        app.setUltimaVisualizacao(1);

        DeviceUtils.esconderTeclado(this, etPesquisa);
    }
}