package com.jailbird.scorch;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jailbird.scorch.dummy.DummyContent;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nineoldandroids.animation.Animator;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PickerActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener, ItemFragment.OnListFragmentInteractionListener {
    public static List<DummyContent.DummyItem> items;
    public static List<Interest> interests;
    public static List<Interest> myInterests;

    private MaterialSearchBar searchBar;
    private List<String> lastSearches;
    List<String> list = new ArrayList<>();
    private DatabaseReference mDatabase;
    public static List<String> interestList;
    TextView countText;
    public static String prompt;
    FloatingActionButton doneButton;
    ItemFragment f1;
    boolean isEdit=false;
    String myID="UID";
boolean isEvent=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search Interests");
        searchBar.setSpeechMode(false);
        //enable searchbar callbacks
        prompt = "";
        interests = new ArrayList<>();
        searchBar.setOnSearchActionListener(this);
        searchBar.setNavButtonEnabled(false);
if(FirebaseAuth.getInstance().getCurrentUser()!=null){
    String id=FirebaseAuth.getInstance().getCurrentUser().getEmail();
    String com=".com";
    String rep="";
    if (id != null) {
        String sub=id.replace(com,rep);

        myID=sub.replaceAll("[-+.^:,]","");
    }


}
        myInterests = new ArrayList<>();
        lastSearches = searchBar.getLastSuggestions();
        searchBar.setLastSuggestions(list);
        interestList = new ArrayList<>();
        countText = (TextView) findViewById(R.id.count_text);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(getIntent().hasExtra("isEdit")){
            myInterests=MainActivity.myInterests;
            isEdit=true;
        }else{
            isEdit=false;
        }
        if(getIntent().hasExtra("isEvent")){
            isEvent=true;
        }
        //restore last queries from disk
        doneButton = (FloatingActionButton) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEvent) {

                    mDatabase.child("Interests").child(myID).setValue(myInterests).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!isEdit) {
                                Log.d("LoadingActivity", "Messing everything up");

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                finish();
                            }

                        }
                    });
                }else{

new AsyncTask<Void, Void, Void>(){

    @Override
    protected Void doInBackground(Void... voids) {
        for(Interest interest:myInterests){
            CreateEventActivity.selectedInterests.add(interest);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        finish();
        super.onPostExecute(aVoid);
    }
}.execute();
                }
            }
        });

        items = new ArrayList<>();

        //getArray();

    }

    @Override
    protected void onResume() {
        getInterests();

        super.onResume();


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("interests.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getInterests() {
        Log.d("Interests", "getting interests ");

      AsyncTask<Void,Void,Void>myTask=  new AsyncTask<Void, Void, Void>() {



            @Override
            protected Void doInBackground(Void... params) {
                //Looper.prepare();

                Field[] interfaceFields = Interests.class.getFields();
                Log.d("Fields", " field " + interfaceFields.length);

                for (Field f : interfaceFields) {
                    //do something
                    try {
                        Interest interest = (Interest) f.get(this);
                        interests.add(interest);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }


                }

                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                f1 = new ItemFragment();
                FragmentManager fM = getFragmentManager();
                fM.beginTransaction().replace(R.id.container, f1)
                        .commit();
                AVLoadingIndicatorView view = (AVLoadingIndicatorView) findViewById(R.id.avi);
                view.smoothToHide();
            }


        };
myTask.execute();
    }

    private void getArray() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("array");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                String itemString = m_jArry.getString(i);
                //JSONObject jo_inside = m_jArry.getJSONObject(i);
                //Log.d("Details-->", itemString);
                String formula_value = itemString;
                String url_value = "" + i;

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("formule", formula_value);
                m_li.put("url", url_value);
                items.add(new DummyContent.DummyItem(formula_value, R.drawable.nightsml, url_value));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onSearchStateChanged(boolean b) {
        Log.d("Search", " state " + b);
        if (!b) {
            //getArray();
            f1.adapter.swap(interests);
            Log.d("Search", " items " + items.size());

        }

    }

    @Override
    public void onSearchConfirmed(final CharSequence charSequence) {
        Log.d("Search", "Searching for " + charSequence);
        if (charSequence != "") {
            final List<Interest> searchItems = new ArrayList<>();
            //final ProgressDialog mProgressDialog = ProgressDialog.show(this, "", "Searching...", true);


            for (Interest item : interests) {


                if (item.interestName.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    searchItems.add(item);

                    Log.d("Search", " " + searchItems.size());
                }
            }
            try {

                // code runs in a thread

                f1.adapter.swap(searchItems);

                //mProgressDialog.dismiss();


            } catch (final Exception ex) {

                //picker.clearItems();


            }
        }

    }

    @Override
    public void onButtonClicked(int i) {
        Log.d("Button", " " + i);

    }


    @Override
    public void onListFragmentInteraction(Interest item) {
        Log.d("Search", " count " + myInterests.size());
      /*  if(myInterests.contains(item)){
            PickerActivity.myInterests.remove(item);
            Log.w("Check","Interests removed "+item);

        }else{
            PickerActivity.myInterests.add(item);
            Log.w("Check","Interests added "+item.interestName);

        }*/
       if(!isEvent) {
           int num = 6 - myInterests.size();
           if (myInterests.size() < 6) {
               countText.setText("Select " + num + " more interests");
               if (doneButton.getVisibility() == View.VISIBLE) {
                   YoYo.with(Techniques.ZoomOut)
                           .duration(700)
                           .withListener(new Animator.AnimatorListener() {
                               @Override
                               public void onAnimationStart(Animator animation) {

                               }

                               @Override
                               public void onAnimationEnd(Animator animation) {
                                   doneButton.setVisibility(View.INVISIBLE);

                               }

                               @Override
                               public void onAnimationCancel(Animator animation) {

                               }

                               @Override
                               public void onAnimationRepeat(Animator animation) {

                               }
                           })
                           .playOn(findViewById(R.id.done_button));
               }
           } else {
               if (doneButton.getVisibility() == View.INVISIBLE) {
                   YoYo.with(Techniques.BounceIn)
                           .duration(700)
                           .playOn(findViewById(R.id.done_button));
                   doneButton.setVisibility(View.VISIBLE);
               }
               int num2 = myInterests.size();
               countText.setText("You have  " + num2 + " interests selected");
           }
       }else{
           if (myInterests.size() < 1) {
               countText.setText("Select interests for your Event");
               if (doneButton.getVisibility() == View.VISIBLE) {
                   YoYo.with(Techniques.ZoomOut)
                           .duration(700)
                           .withListener(new Animator.AnimatorListener() {
                               @Override
                               public void onAnimationStart(Animator animation) {

                               }

                               @Override
                               public void onAnimationEnd(Animator animation) {
                                   doneButton.setVisibility(View.INVISIBLE);

                               }

                               @Override
                               public void onAnimationCancel(Animator animation) {

                               }

                               @Override
                               public void onAnimationRepeat(Animator animation) {

                               }
                           })
                           .playOn(findViewById(R.id.done_button));
               }
           } else {
               if (doneButton.getVisibility() == View.INVISIBLE) {
                   YoYo.with(Techniques.BounceIn)
                           .duration(700)
                           .playOn(findViewById(R.id.done_button));
                   doneButton.setVisibility(View.VISIBLE);
               }
               int num2 = myInterests.size();
               countText.setText("You have  " + num2 + " interests selected");
           }
       }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
