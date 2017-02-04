package br.com.paraondeirapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.paraondeirapp.R;

public class MensagemUtils {

    public static Toast gerarToast(Context ctx, String mensagem){
        return Toast.makeText(ctx, mensagem, Toast.LENGTH_SHORT);
    }

    public static void gerarEExibirToast(Context ctx, String mensagem) {
        gerarToast(ctx, mensagem).show();
    }

    public AlertDialog gerarAlertDialog(Context ctx, String titulo, String mensagem,
                                        String textoSim, String textoNao, String textoNeutro){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);
        builder.setCancelable(false);

        if (!textoSim.isEmpty()){
            builder.setPositiveButton(textoSim, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clicouSim();
                }
            });
        }

        if (!textoNao.isEmpty()){
            builder.setNegativeButton(textoNao, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clicouNao();
                }
            });
        }

        if (!textoNeutro.isEmpty()){
            builder.setNeutralButton(textoNeutro, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clicouNeutro();
                }
            });
        }

        return builder.create();
    }

    public void gerarEExibirAlertDialog(Context ctx, String titulo, String mensagem,
                                        String textoSim, String textoNao, String textoNeutro){

        gerarAlertDialog(ctx, titulo, mensagem, textoSim, textoNao, textoNeutro).show();
    }

    public void gerarEExibirAlertDialogOK(Context ctx, String titulo, String mensagem, String textoSim){
        gerarEExibirAlertDialog(ctx, titulo, mensagem, textoSim, "", "");
    }

    public Dialog gerarCustomDialog(Context ctx, String titulo, String mensagem){
        Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView textViewTitulo = (TextView) dialog.findViewById(R.id.tv_titulo);
        textViewTitulo.setText(titulo);
        TextView textViewMensagem = (TextView) dialog.findViewById(R.id.tv_mensagem);
        textViewMensagem.setText(mensagem);

        Button btnOK = (Button) dialog.findViewById(R.id.bt_ok);
        Button btnCancelar = (Button) dialog.findViewById(R.id.bt_cancelar);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicouSim();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicouNao();
            }
        });

        return dialog;
    }

    protected void clicouNeutro() {
        // Sobrescrever nas classes que utilizam os métodos gerarAlertDialog, gerarEExibirAlertDialog
        // e gerarCustomDialog
    }

    protected void clicouNao() {
        // Sobrescrever nas classes que utilizam os métodos gerarAlertDialog, gerarEExibirAlertDialog
        // e gerarCustomDialog
    }

    protected void clicouSim() {
        // Sobrescrever nas classes que utilizam os métodos gerarAlertDialog, gerarEExibirAlertDialog
        // e gerarCustomDialog
    }
}
