package gdscnits.easyexchange.app.SharedPrefManager;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {
    private String PREF_NAME="local_storage";
    private Context context;

    public SharedPrefManager(Context context){
        this.context=context;
    }

    public void storeKeyValuePair(String key,String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getValue(String key){
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key,null);
    }

    public void clearAll(){
        context.getSharedPreferences(PREF_NAME,0).edit().clear().apply();
    }
}