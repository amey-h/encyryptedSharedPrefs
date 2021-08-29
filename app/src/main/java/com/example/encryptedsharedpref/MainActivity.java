package com.example.encryptedsharedpref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         sharedPrefWithoutEncryption();

        sharedPrefsWithEncryption();
    }

    private void sharedPrefWithoutEncryption() {
        Log.i(TAG, "sharedPrefWithoutEncryption");
        SharedPreferences prefs = getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", "John");
        editor.putString("lastname", "Smith");
        editor.putString("email", "john.s@gmail.com");
        editor.apply();
    }

    private void sharedPrefsWithEncryption() {
        Log.i(TAG, "sharedPrefsWithEncryption");
        String masterKeyAlias = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            } else {
                masterKeyAlias = new MasterKey.Builder(MainActivity.this)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build().toString();
            }

            SharedPreferences encryptedSharedPrefs = EncryptedSharedPreferences.create(
                    "EncryptedSharedPrefs",
                    masterKeyAlias,
                    MainActivity.this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = encryptedSharedPrefs.edit();
            editor.putString("name", "John");
            editor.putString("lastname", "Smith");
            editor.putString("email", "john.s@gmail.com");
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
        }

    }
}