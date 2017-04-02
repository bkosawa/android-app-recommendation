package br.com.kosawalabs.apprecommendation.data.disk;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;

public class TokenDiskRepository implements TokenDataRepository {

    private static final String SESSION_TOKEN = "SESSION_TOKEN";
    private Context context;

    public TokenDiskRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getToken() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(SESSION_TOKEN, "");
    }

    @Override
    public void putToken(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SESSION_TOKEN, token);
        editor.apply();
    }

    @Override
    public void removeToken() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(SESSION_TOKEN);
        editor.apply();
    }
}
