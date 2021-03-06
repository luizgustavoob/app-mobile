package br.com.paraondeirapp.firebase;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.paraondeirapp.AppParaOndeIr;
import br.com.paraondeirapp.model.Usuario;
import br.com.paraondeirapp.utils.SharedPreferencesUtils;

public class ParaOndeIrInstanceIDService extends FirebaseInstanceIdService {

    private static String TAG = ParaOndeIrInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferencesUtils.setTokenFirebase(token);
        Usuario user = AppParaOndeIr.getInstance().getUser();
        if (user != null && !user.getFcmid().equals(token)){
            user.setFcmid(token);
        }
        Log.i(TAG, "Token do app: " + token);
    }
}
