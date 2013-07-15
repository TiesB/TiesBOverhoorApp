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

/**
 * Created by bolding on 2-7-13.
 */

public class EnterWordsActivity extends Activity {

    private final int dInt = 0;
    private final String dString = "";
    private final boolean dBoolean = false;
    private final Set<String> dSet = new HashSet<String>();

    private SaveHandler sh = new SaveHandler();
    private WordHandler wh = new WordHandler();
    private FinishHandler fh = new FinishHandler();

    private String title;
    private String language1;
    private String language2;
    private int words;

    private EditText mLanguage1ET;
    private EditText mLanguage2ET;
    private Button mNextWordButton;
    private Button mFinishButton;

    private String[] tempWordsLanguage1 = new String[100];
    private String[] tempWordsLanguage2 = new String[100];
    
    private Bundle intentStartBundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_words);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        mLanguage1ET = (EditText)findViewById(R.id.words_language1_et);
        mLanguage2ET = (EditText)findViewById(R.id.words_language2_et);

        mNextWordButton = (Button)findViewById(R.id.words_next_word_button);
        mNextWordButton.setOnClickListener(wh.nextWordButtonHandler);
        mFinishButton = (Button)findViewById(R.id.words_finish_button);
        mFinishButton.setOnClickListener(fh.finishButtonHandler);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intentStartBundle = extras.getBundle("bundle");
            title = intentStartBundle.getString("title");
            language1 = intentStartBundle.getString("language1");
            language2 = intentStartBundle.getString("language2");
            words = intentStartBundle.getInt("words", 0);
            if (words > 1) {
                tempWordsLanguage1 = intentStartBundle.getStringArray("temp_words_language1");
                tempWordsLanguage2 = intentStartBundle.getStringArray("temp_words_language2");
            }
        }
        if (words == 100) return;
        if (words == 0) {
            mFinishButton.setClickable(false);
            mFinishButton.setEnabled(false);
        }
        Toast.makeText(getApplicationContext(), language2, Toast.LENGTH_LONG).show();

        mLanguage1ET.setHint(language1);
        mLanguage2ET.setHint(language2);
    }

    @Override
    public void onPause() {
        super.onPause();
        //TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Bundle saveBundle = getSaveBundle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if(mLanguage1ET.getText().toString().equals("") && mLanguage2ET.getText().toString().equals("")) {finish(); return;}
            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.quit_words))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                })
                .show();
    }

    public Bundle getSaveBundle() {
        Bundle saveBundle = intentStartBundle;
        if (!tempWordsLanguage1[1].isEmpty()) {
            saveBundle.remove("words");
            saveBundle.remove("temp_words_language1");
            saveBundle.remove("temp_words_language2");
        }
        saveBundle.putInt("words", words);
        saveBundle.putStringArray("temp_words_language1", tempWordsLanguage1);
        saveBundle.putStringArray("temp_words_language2", tempWordsLanguage2);
        return saveBundle;
    }

    public class SaveHandler {
        //Mode: 1 for saving, 2 for loading
        //Type: 1 for int, 2 for String, 3 for boolean, 4 for set

        private final String MAIN_SAVE = "tiesb_mainsave";
        private SharedPreferences sp;
        private SharedPreferences.Editor spe;

        public void put (int type, String save, String pref, int input_int, String input_string, boolean input_boolean, Set<String> input_set) {
            if (save.equals("main")) save = MAIN_SAVE;
            sp = getSharedPreferences(save, 0);
            spe = sp.edit();
            switch (type) {
                case 1: spe.putInt(pref, input_int); break;
                case 2: spe.putString(pref, input_string); break;
                case 3: spe.putBoolean(pref, input_boolean); break;
                case 4: spe.putStringSet(pref, input_set); break;
                default: Log.e("TiesB", "No legit type: " + Integer.toString(type));
            }
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

    public void reloadAcitivity () {
        Intent intent = getIntent();
        finish();

        Bundle saveBundle = getSaveBundle();
        intent.putExtra("bundle", saveBundle);

        /*if (words > 1) {
            intent.removeExtra("words");
            intent.removeExtra("temp_words_language1");
            intent.removeExtra("temp_words_language2");
        }
        intent.removeExtra("title");
        intent.removeExtra("language");
        intent.removeExtra("language2");
        intent.putExtra("title", title);
        intent.putExtra("language1", language1);
        intent.putExtra("language2", language2);
        intent.putExtra("words", words);
        intent.putExtra("temp_words_language1", tempWordsLanguage1);
        intent.putExtra("temp_words_language2", tempWordsLanguage2);*/
        startActivity(intent);
    }

    public class FinishHandler {
        private String save_prefix = "word_";

        View.OnClickListener finishButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), tempWordsLanguage2[2], Toast.LENGTH_LONG).show();
            }
        };
    }

    public class WordHandler {

        public View.OnClickListener nextWordButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {
                String wordLanguage1 = mLanguage1ET.getText().toString();
                String wordLanguage2 = mLanguage2ET.getText().toString();
                if (wordLanguage1.equals("") || wordLanguage2.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
                }
                else {
                    saveWordToTemp(wordLanguage1, wordLanguage2);
                    reloadAcitivity();
                }
            }
        };



        private void saveWordToTemp (String wordLanguage1, String wordLanguage2) {
            words = words + 1;
            tempWordsLanguage1[words] = wordLanguage1;
            tempWordsLanguage2[words] = wordLanguage2;
        }
    }
}