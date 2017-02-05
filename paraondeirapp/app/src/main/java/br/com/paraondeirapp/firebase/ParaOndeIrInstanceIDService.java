package br.com.paraondeirapp.firebase;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.com.paraondeirapp.utils.SharedPreferencesUtils;

public class ParaOndeIrInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferencesUtils shared = new SharedPreferencesUtils();
        shared.setTokenFirebase(token);
        Log.i("Token do app", token);
    }
}
