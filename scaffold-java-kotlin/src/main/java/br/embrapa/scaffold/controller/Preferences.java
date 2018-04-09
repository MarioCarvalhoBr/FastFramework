package br.embrapa.scaffold.controller;

import android.content.Context;
import android.content.SharedPreferences;

import br.embrapa.scaffold.util.Constantes;

public class Preferences {

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;

    public Preferences(Context context) {
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
