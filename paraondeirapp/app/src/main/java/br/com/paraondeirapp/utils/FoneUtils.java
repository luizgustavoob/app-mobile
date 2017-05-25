package br.com.paraondeirapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import br.com.paraondeirapp.R;

public class FoneUtils {

    public static void discar(Context context, String telefone){
        if(!TextUtils.isEmpty(telefone) &&
                PhoneNumberUtils.isGlobalPhoneNumber(StringUtils.apenasNumeros(telefone))) {
            ligar(context, StringUtils.apenasNumeros(telefone));
        } else {
            MensagemUtils.gerarEExibirToast(context, context.getString(R.string.msg_erro_ligacao));
        }
    }

    private static void ligar(Context context, String telefone) {
        Intent intent;
        try {
            String ligacao = "tel:".concat(telefone);
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse(ligacao));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            MensagemUtils.gerarEExibirToast(context, context.getString(R.string.msg_erro_ligacao));
        } finally {
            intent = null;
        }
    }
}
