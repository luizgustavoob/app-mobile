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

    public static String getContaDispositivo(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] contas = accountManager.getAccountsByType("com.google");
        Account conta;
        if (contas.length > 0){
            conta = contas[0];
            return conta.name;
        }
        return "";
    }

    public static void esconderTeclado(Context context, View view){
        InputMethodManager inputMethodManager;
        try {
            inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } finally {
            inputMethodManager = null;
        }
    }

    public static boolean temPermissao(Activity activity, String permissao){
        return ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
    }

    public static void solicitarPermissao(Activity activity, String mensagem, String permissao){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissao)) { //já teve uma solicitação e foi negada?
            exibirDialogDePermissao(activity, mensagem, permissao); //explicação do porquê precisa da permissão.
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permissao}, 1);
        }
    }

    private static void exibirDialogDePermissao(final Activity activity, String mensagem, final String permissao) {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.setTitle(activity.getString(R.string.permissao))
                .setMessage(mensagem)
                .setPositiveButton(activity.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(activity, new String[]{permissao}, 1);
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
