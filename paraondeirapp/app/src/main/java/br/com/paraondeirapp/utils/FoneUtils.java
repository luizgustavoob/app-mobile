package br.com.paraondeirapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import br.com.paraondeirapp.R;

public class FoneUtils {

    /**
     * Realiza ligação para o telefone passado por parâmetro.
     * @param - Telefone para ligação
     * @param - Contexto de aplicação.
     */
    public static void discar(String telefone, Context ctx){
        if(!TextUtils.isEmpty(telefone) &&
                PhoneNumberUtils.isGlobalPhoneNumber(StringUtils.apenasNumeros(telefone))) {
            ligar(StringUtils.apenasNumeros(telefone), ctx);
        } else {
            MensagemUtils.gerarEExibirToast(ctx, ctx.getString(R.string.msg_erro_ligacao));
        }
    }

    /**
     * Realiza ligação para o telefone passado por parâmetro.
     * @param - Telefone para ligação
     * @param - Contexto de aplicação.
     */
    private static void ligar(String telefone, Context ctx) {
        Intent it;
        try {
            String ligacao = "tel:".concat(telefone);
            it = new Intent(Intent.ACTION_DIAL, Uri.parse(ligacao));
            ctx.startActivity(it);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            MensagemUtils.gerarEExibirToast(ctx, ctx.getString(R.string.msg_erro_ligacao));
        } finally {
            it = null;
        }
    }
}
