package br.com.paraondeirapp.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import br.com.paraondeirapp.R;
import me.drakeet.materialdialog.MaterialDialog;

public class DeviceUtils {

    public static String getContaDispositivo(Context ctx) {
        AccountManager manager = AccountManager.get(ctx);
        Account[] contas = manager.getAccountsByType("com.google");
        Account conta;
        if (contas.length > 0){
            conta = contas[0];
            return conta.name;
        }
        return "";
    }

    public static void esconderTeclado(Context ctx, View view){
        InputMethodManager imm;
        try {
            imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } finally {
            imm = null;
        }
    }

    public static boolean temPermissao(Activity ctx, String permissao){
        return ContextCompat.checkSelfPermission(ctx, permissao) == PackageManager.PERMISSION_GRANTED;
    }

    public static void solicitarPermissao(Activity ctx, String mensagem, String permissao){
        if (ActivityCompat.shouldShowRequestPermissionRationale(ctx, permissao)) { //já teve uma solicitação e foi negada?
            exibirDialogDePermissao(ctx, mensagem, permissao); //explicação do porquê precisa da permissão.
        } else {
            ActivityCompat.requestPermissions(ctx, new String[]{permissao}, 1);
        }
    }

    private static void exibirDialogDePermissao(final Activity ctx, String mensagem, final String permissao) {
        final MaterialDialog dialog = new MaterialDialog(ctx);
        dialog.setTitle(ctx.getString(R.string.permissao))
                .setMessage(mensagem)
                .setPositiveButton(ctx.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(ctx, new String[]{permissao}, 1);
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
