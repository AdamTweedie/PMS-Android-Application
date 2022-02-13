package com.deitel.pms;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPrefUtils {

    public android.content.SharedPreferences getEncryptedPreferences(MasterKey masterKey, Context context) {
        try {
            return EncryptedSharedPreferences.create(context
                    ,"secret_credentials"
                    ,masterKey
                    ,EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
                    ,EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MasterKey getMasterKey(Context context) {
        assert context != null;
        MasterKey masterKey;
        try {
            masterKey = new
                    MasterKey.Builder(context,MasterKey.DEFAULT_MASTER_KEY_ALIAS).
                    setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            return masterKey;
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
