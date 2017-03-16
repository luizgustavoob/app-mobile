package br.com.paraondeirapp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.R;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.utils.DeviceUtils;
import br.com.paraondeirapp.utils.MensagemUtils;
import br.com.paraondeirapp.utils.SharedPreferencesUtils;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!DeviceUtils.temPermissao(this, Manifest.permission.GET_ACCOUNTS)) {
            DeviceUtils.solicitarPermissao(this, getString(R.string.msg_permissao_conta),
                    Manifest.permission.GET_ACCOUNTS);
        } else {
            inicializar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                for (int i = 0; i < permissions.length; i++){
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.GET_ACCOUNTS)){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            inicializar();
                        } else {
                            MensagemUtils mu = new MensagemUtils(){
                                @Override
                                protected void clicouSim() {
                                    finish();
                                }
                            };
                            mu.gerarEExibirAlertDialogOK(this, getString(R.string.app_name),
                                    getString(R.string.msg_encerra_app), getString(R.string.ok));
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void inicializar(){
        setUsuarioAplicacao();
        SharedPreferencesUtils shared = new SharedPreferencesUtils();
        try {
            Intent intent = new Intent(this, shared.isPrimeiroAcesso()
                    ? SplashActivity.class : ListaActivity.class);
            startActivity(intent);
        } finally {
            shared = null;
        }
        finish();
    }

    private void setUsuarioAplicacao() {
        AppParaOndeIr app = AppParaOndeIr.getInstance();
        if (app.getUser() == null ){
            String conta = DeviceUtils.getContaDispositivo(this);
            if (conta != null){
                app.setUser(new Usuario(conta));
            }
        }
    }
}
