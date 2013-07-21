package com.tiesb.overhoor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by bolding on 17-7-13.
 */
public class FinishActivity extends Activity {
    final String WORD_PREFIX = "word_";
    final String SAVE_PREFIX = "save_";

    final int dInt = 0;
    final String dString = "";
    final boolean dBoolean = false;
    final Set<String> dSet = new HashSet<String>();

    SaveHandler sh = new SaveHandler();

    String title;
    String language1;
    String language2;
    int words;

    String[] tempWordsLanguage1 = new String[100];
    String[] tempWordsLanguage2 = new String[100];

    Bundle intentStartBundle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intentStartBundle = extras.getBundle("bundle");
            title = intentStartBundle.getString("title");
            language1 = intentStartBundle.getString("language1");
            language2 = intentStartBundle.getString("language2");
            words = intentStartBundle.getInt("words", 0);
            tempWordsLanguage1 = intentStartBundle.getStringArray("temp_words_language1");
            tempWordsLanguage2 = intentStartBundle.getStringArray("temp_words_language2");
        }

        //TODO: Buttons & onClicks

        saveToSave();
    }

    private void saveToSave () {
        String saveName = SAVE_PREFIX + title;
        Toast.makeText(this, Integer.toString(words), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, saveName, Toast.LENGTH_LONG).show();

        //Handle words on their own
        for (int i = 1; i <= words; i++) {
            Toast.makeText(this, tempWordsLanguage2[i], Toast.LENGTH_SHORT).show();
            sh.putString(saveName, tempWordsLanguage1[i], tempWordsLanguage2[i]);
        }

        //Save to main save
        int numberOfSaves = sh.loadInt("main", "number_of_saves");
        sh.putInt("main", "number_of_saves", numberOfSaves + 1);
        Set<String> oldSet = dSet;
        try {
            oldSet = sh.loadSet("main", "saves");
        } catch (Exception e) {
            Log.i("TiesBOverhoorApp", "No saves found when loading to save new save");
        }
        oldSet.add(title);
        sh.putSet("main", "saves", oldSet);
    }

    public class SaveHandler {
        //Mode: 1 for saving, 2 for loading
        //Type: 1 for int, 2 for String, 3 for boolean, 4 for set

        private final String MAIN_SAVE = "tiesb_mainsave";
        private SharedPreferences sp;
        private SharedPreferences.Editor spe;

        public void putInt (String save, String pref, int input) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            spe = sp.edit();
            spe.putInt(pref, input);
            spe.commit();
        }

        public void putString (String save, String pref, String input) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            spe = sp.edit();
            spe.putString(pref, input);
            spe.commit();
        }

        public void putBoolean (String save, String pref, boolean input) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            spe = sp.edit();
            spe.putBoolean(pref, input);
            spe.commit();
        }

        public void putSet (String save, String pref, Set<String> input) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            spe = sp.edit();
            spe.putStringSet(pref, input);
            spe.commit();
        }
        
        public int loadInt (String save, String pref) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            int ret = sp.getInt(pref, dInt);
            if (ret == 0) Log.w("TiesB", "Integer " + pref + " has no value or is zero. Probably bad!");
            return ret;
        }

        public String loadString (String save, String pref) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            String ret = sp.getString(pref, dString);
            if (ret.equals("")) Log.e("TiesB", "String " + pref + " has no value!");
            return ret;
        }

        public Boolean loadBoolean (String save, String pref) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            Boolean ret = sp.getBoolean(pref, dBoolean);
            if (!ret) Log.w("TiesB", "Boolean " + pref + " has no value or is false!");
            return ret;
        }

        public Set<String> loadSet (String save, String pref) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            Set<String> ret = sp.getStringSet(pref, dSet);
            return ret;
        }
    }

}
