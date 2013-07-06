package com.tiesb.overhoor;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private final int dInt = 0;
    private final String dString = "";
    private final boolean dBoolean = false;
    private Set<String> dSet = new HashSet<String>();;

    private SaveHandler sh = new SaveHandler();

    private EditText mTitleET;
    private EditText mLanguage1ET;
    private EditText mLanguage2ET;
    private Button mSaveButton;

    private Button mLoadButton;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onBackPressed () {
        if (!mTitleET.getText().toString().isEmpty() || !mLanguage1ET.getText().toString().isEmpty() || !mLanguage2ET.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.close_app))
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
        } else finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment;
            Bundle args;
            switch (position) {
                case 0:
                    fragment = new LoadFragment();
                    args = new Bundle();
                    args.putInt(LoadFragment.ARG_SECTION_NUMBER, position + 1);
                    fragment.setArguments(args);
                    break;
                case 1:
                    fragment = new SaveFragment();
                    args = new Bundle();
                    args.putInt(SaveFragment.ARG_SECTION_NUMBER, position + 1);
                    fragment.setArguments(args);
                    break;
                default:
                    fragment = new DummySectionFragment();
                    args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class LoadFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public LoadFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_load, container, false);
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));


            mLoadButton = (Button) rootView.findViewById(R.id.load_button);
            mLoadButton.setOnClickListener(loadButtonHandler);

            mSpinner = (Spinner) rootView.findViewById(R.id.load_spinner);

            List<String> list = new ArrayList<String>();

            //////////////////////////////////////////////////////////////TODO: REMOVE!!!!!!!!
            sh.put(1, "main", "number_of_saves", 4, dString, dBoolean, dSet);
            //////////////////////////////////////////////////////////////TODO: REMOVE!!!!!!!!

            if (sh.loadInt("main", "number_of_saves") == 0) {
                list.add(getString(R.string.no_saves_found));
                mLoadButton.setEnabled(false);
                mLoadButton.setClickable(false);
            } else {
                list.addAll(sh.loadSet("main", "ties"));
                mLoadButton.setEnabled(true);
                mLoadButton.setClickable(true);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list);
            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinner.setAdapter(dataAdapter);

            return rootView;
        }

        View.OnClickListener loadButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {
                String sSaveName = String.valueOf(mSpinner.getSelectedItem());
                String sLanguage1 = sh.loadString(sSaveName, "language1");
                String sLanguage2 = sh.loadString(sSaveName, "language2");
                int iNumberOfWords = sh.loadInt(sSaveName, "number_of_words");
                Set<String> words = sh.loadSet(sSaveName, "words");
            }
        };
    }

    public class SaveFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public SaveFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_save, container, false);
            //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

            mTitleET = (EditText) rootView.findViewById(R.id.save_title_et);
            mLanguage1ET = (EditText) rootView.findViewById(R.id.save_language1_et);
            mLanguage2ET = (EditText) rootView.findViewById(R.id.save_language2_et);

            mSaveButton = (Button) rootView.findViewById(R.id.save_button);
            mSaveButton.setOnClickListener(saveButtonHandler);

            return rootView;
        }

        View.OnClickListener saveButtonHandler = new View.OnClickListener() {
            public void onClick(View v) {
                String title = mTitleET.getText().toString();
                String language1 = mLanguage1ET.getText().toString();
                String language2 = mLanguage2ET.getText().toString();

                if (title.equals("") || language1.equals("") || language2.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
                    Log.v("TiesB", "No correct input from user when pressing Enter Words button.");
                } else {
                    Intent intent = new Intent(getApplicationContext(), EnterWordsActivity.class);
                    Bundle saveBundle = new Bundle();
                    saveBundle.putString("title", title);
                    saveBundle.putString("language1", language1);
                    saveBundle.putString("language2", language2);
                    intent.putExtra("bundle", saveBundle);
                    startActivity(intent);
                }
            }
        };
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
            //Want this to process as fast as possible
            return sp.getStringSet(pref, dSet);
        }
    }
}
