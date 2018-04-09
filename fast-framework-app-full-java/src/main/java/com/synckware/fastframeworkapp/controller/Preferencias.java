package com.synckware.fastframeworkapp.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.synckware.fastframeworkapp.util.Constantes;

public class Preferencias {

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;

    public Preferencias(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Constantes.NAME_APP, Constantes.PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void setValue(String value) {
        mEditor.putString(Constantes.SHARED_VALUE, value);
        mEditor.commit();
    }

    public String getValue() {
        return mSharedPreferences.getString(Constantes.SHARED_VALUE, "default_value");
    }

}
