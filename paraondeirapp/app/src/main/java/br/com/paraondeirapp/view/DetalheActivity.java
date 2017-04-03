package br.com.paraondeirapp.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.adapter.DetalheAdapter;
import br.com.paraondeirapp.model.Avaliacao;
import br.com.paraondeirapp.model.Estabelecimento;
import br.com.paraondeirapp.enumeration.YesNo;
import br.com.paraondeirapp.repository.dao.AvaliacaoDAO;
import br.com.paraondeirapp.utils.DeviceUtils;
import br.com.paraondeirapp.utils.FoneUtils;
import br.com.paraondeirapp.utils.MensagemUtils;
import br.com.paraondeirapp.view.interfaces.IActivity;

public class DetalheActivity extends AppCompatActivity implements
        IActivity,
        View.OnClickListener {

    private RelativeLayout layoutPrinc;
    private Toolbar tbDetalhe;
    private ImageView ivFoto;
    private ListView lvDetalhe;
    private Button btGostei, btNaoGostei;
    private Estabelecimento estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        inicializarComponentes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_detalhe, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context_detalhe, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            /*case R.id.mn_ver_no_mapa:
                MensagemUtils.gerarEExibirToast(this, "Vai abrir o mapa");*/
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_ligacao:
                ligarParaEstabelecimento();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void inicializarComponentes() {
        Intent i = getIntent();
        this.estabelecimento = (Estabelecimento) i.getSerializableExtra("estabelecimento");
        this.tbDetalhe = (Toolbar) findViewById(R.id.tb_detalhe);
        setSupportActionBar(tbDetalhe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.layoutPrinc = (RelativeLayout) findViewById(R.id.layoutPrinc);
        this.ivFoto = (ImageView) findViewById(R.id.iv_foto);
        this.lvDetalhe = (ListView) findViewById(R.id.lv_detalhe_estab);
        this.lvDetalhe.setAdapter(new DetalheAdapter(this, estabelecimento));
        this.btGostei = (Button) findViewById(R.id.bt_gostei);
        this.btNaoGostei = (Button) findViewById(R.id.bt_nao_gostei);

        this.setImagemEstabelecimento();

        this.btGostei.setOnClickListener(this);
        this.btNaoGostei.setOnClickListener(this);
        registerForContextMenu(layoutPrinc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_gostei:
                try {
                    avaliar(YesNo.S);
                } catch (SQLException ex) {
                    MensagemUtils.gerarEExibirToast(this, ex.getMessage());
                }
                break;
            case R.id.bt_nao_gostei:
                try {
                    avaliar(YesNo.N);
                } catch (SQLException ex) {
                    MensagemUtils.gerarEExibirToast(this, ex.getMessage());
                }
                break;
        }
    }

    private void setImagemEstabelecimento(){
        getSupportActionBar().setTitle(this.estabelecimento.getNome());
        if (estabelecimento.getImagem() != null) {
            Bitmap imagem = BitmapFactory.decodeByteArray(estabelecimento.getImagem(), 0,
                    estabelecimento.getImagem().length);
            this.ivFoto.setImageBitmap(imagem);
        }
    }

    private void avaliar(YesNo like) throws SQLException {
        try {
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setEstabelecimento(estabelecimento);
            avaliacao.setGostou(like);
            avaliacao.setUsuario(AppParaOndeIr.getInstance().getUser().getUsuario());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            avaliacao.setData(formato.format(new Date()));

            AvaliacaoDAO dao = new AvaliacaoDAO(this);
            dao.save(avaliacao);
            MensagemUtils.gerarEExibirToast(this, getString(R.string.msg_sucesso_avaliacao));
            dao.close();
            finish();
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
    }

    private void ligarParaEstabelecimento(){
        if (!DeviceUtils.temPermissao(this, Manifest.permission.CALL_PHONE)) {
            DeviceUtils.solicitarPermissao(this, getString(R.string.msg_permissao_ligar),
                    Manifest.permission.CALL_PHONE);
        } else {
            FoneUtils.discar(estabelecimento.getTelefone(), this);
        }
    }
}
