package com.jailbird.scorch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcoscg.easylicensesdialog.EasyLicensesDialog;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.GooglePlusButton;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jailbird.scorch.MainActivity.isNightMode;
import static com.jailbird.scorch.MainActivity.isNotified;
import static com.jailbird.scorch.MainActivity.myLocation;
import static com.jailbird.scorch.MainActivity.nearbyPlaces;
import static com.jailbird.scorch.MainActivity.searchDistance;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_INVITE = 989;
    private static final String TAG = "SettingsAct";
    private static final int CONTACT_PICKER_REQUEST = 744;
    private static final int READ_CONTACT_REQUEST = 222;
    private String distanceUnit = "Km.";
    Context context;
    private DatabaseReference mDatabase;
    boolean isNight=false;
    CoordinatorLayout coordinatorLayout;
    TextView mileText, kmText, unitText, privacy, tos, license;
    String MYID = "User ID";
    String name = "";
SwitchCompat notificationSwitch,nightModeSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (getSupportActionBar() != null) {
            //actionBar.setTitle("Settings");
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                if (id != null) {
                    String sub = id.replace(com, rep);
                    MYID = sub.replaceAll("[-+.^:,]", "");

                }
            }else{
                MYID=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

        }
        distanceUnit = MainActivity.distanceUnit;
notificationSwitch= (SwitchCompat) findViewById(R.id.hot_switch);
        notificationSwitch.setChecked(isNotified);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isNotified=b;
            }
        });
        nightModeSwitch= (SwitchCompat) findViewById(R.id.vis_switch);
        nightModeSwitch.setChecked(isNightMode);
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                isNightMode=b;
                isNight=b;
                Log.e("NightMode"," "+isNightMode);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getNearby();
                startService(new Intent(context,LoaderService.class));
                writePrefs(distanceUnit, searchDistance, isNotified,isNight);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.content);
        setupSeekbar();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mileText = (TextView) findViewById(R.id.mile_text);
        CardView share = (CardView) findViewById(R.id.share_button);
        share.setOnClickListener(this);
        kmText = (TextView) findViewById(R.id.km_text);
        mileText.setOnClickListener(this);
        kmText.setOnClickListener(this);
        unitText = (TextView) findViewById(R.id.unit_text);
        unitText.setText(distanceUnit);
        if (distanceUnit.equals("Mi.")) {
            mileText.setSelected(true);
            kmText.setSelected(false);
        } else {
            mileText.setSelected(false);
            kmText.setSelected(true);
        }
        privacy = (TextView) findViewById(R.id.privacy);
        privacy.setOnClickListener(this);
        tos = (TextView) findViewById(R.id.tos);
        tos.setOnClickListener(this);

        findViewById(R.id.licenses).setOnClickListener(this);

    }
    void getNearby() {
        final List<Place>nearList=new ArrayList<Place>();
        //nearbyPlaces.clear();
        int distance=(searchDistance*100)+1;
        new NRPlaces.Builder()
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(List<Place> places) {
                        for (noman.googleplaces.Place place : places) {
if(!nearList.contains(place))
                            nearList.add(place);
                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        //stopAnim();
                        //startActivity(new Intent(getApplicationContext(), MapsActivity.class).putStringArrayListExtra("placeIds", placeIdList));
nearbyPlaces=nearList;
                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(myLocation.getLatitude(), myLocation.getLongitude())

                .radius(distance)

                .build()
                .execute();
    }
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupSeekbar() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setMax(50);
        seekBar.setProgress(MainActivity.searchDistance);
        final TextView seekText = (TextView) findViewById(R.id.distance_text);
        seekText.setText("" + searchDistance + distanceUnit);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekText.setText("" + progress + distanceUnit);
                MainActivity.searchDistance = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void writePrefs(String distanceUnit, int distance, boolean notification,boolean isNightMode) {
        UserPreferences preferences = new UserPreferences(distance, distanceUnit, notification,isNightMode);
        mDatabase.child("Users").child(MYID).child("Preferences").setValue(preferences).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(coordinatorLayout, "Settings saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.km_text:
                kmText.setSelected(true);
                kmText.setTextColor(Color.WHITE);
                mileText.setSelected(false);
                mileText.setTextColor(Color.BLACK);
                distanceUnit = "Km.";
                unitText.setText(distanceUnit);
                MainActivity.distanceUnit = distanceUnit;
                break;
            case R.id.mile_text:
                kmText.setSelected(false);
                kmText.setTextColor(Color.BLACK);
                mileText.setSelected(true);
                mileText.setTextColor(Color.WHITE);
                distanceUnit = "Mi.";
                MainActivity.distanceUnit = distanceUnit;

                unitText.setText(distanceUnit);
                break;
            case R.id.share_button:
//onInviteClicked();
                //inviteFriends();
                showCustomDialog();
                break;
            case R.id.privacy:
                openUrl(getString(R.string.privacy_link));
                break;
            case R.id.tos:
                openUrl(getString(R.string.tos_link));
                break;
            case R.id.licenses:
                EasyLicensesDialog easyLicensesDialog = new EasyLicensesDialog(this);
                easyLicensesDialog.setTitle("Licenses"); //by default EasyLicensesDialog comes without any title.
                easyLicensesDialog.setCancelable(true); //true or false
                //easyLicensesDialog.setIcon(R.mipmap.ic_launcher); //add an icon to the title
                easyLicensesDialog.show(); //show the dialog
        }

    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

// Always use string resources for UI text. This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
// Create and start the chooser
        Intent chooser = Intent.createChooser(intent, title);
        startActivity(chooser);
    }

    private void onInviteClicked() {
        String name = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage("" + name + " " + getString(R.string.invitation_message))
                //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse("https://jailbirdinteractive.com/images/newfire.png"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        switch (requestCode){
            case CONTACT_PICKER_REQUEST:
                if(resultCode == RESULT_OK){
                    TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>)data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                    for (SimpleContact selectedContact : selectedContacts)
                        Log.e("Selected", selectedContact.toString());
                }else
                    Toast.makeText(this, "No contacts selected", Toast.LENGTH_LONG).show();
                break;
            case REQUEST_INVITE:
                if (resultCode == RESULT_OK) {
                    // Get the invitation IDs of all sent messages
                    String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);

                    for (String id : ids) {
                        Log.d(TAG, "onActivityResult: sent invitation " + data.getData());
                    }

                } else {
                    // Sending failed or it was canceled, show failure message to the user
                    // ...
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }

    }

    private void inviteFriends() {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = getString(R.string.fb_link);
        previewImageUrl = "https://www.Jailbirdinteractive.com/images/newfire.png";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl("https://fb.me/573949842798289")

                    .setPreviewImageUrl(previewImageUrl)

                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    private void showCustomDialog() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        View mView;
        Dialog mDialog;
        LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(
                LAYOUT_INFLATER_SERVICE);
        ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);

        mView = mInflater.inflate(R.layout.share_dialog, null);
        // mDialog = new Dialog(this,0); // context, theme

        mDialog = new Dialog(mTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mView);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
        ImageButton facebookButton = (ImageButton) mView.findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteFriends();
            }
        });
        ImageButton googlePlusButton = (ImageButton) mView.findViewById(R.id.google_button);
        googlePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });
        ImageButton shareButton = (ImageButton) mView.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "" + name + " " + getString(R.string.invitation_message);
                String body = getString(R.string.join_us) + " " + getResources().getString(R.string.invitation_deep_link);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
        // do some thing with the text view or any other view

    }

}
