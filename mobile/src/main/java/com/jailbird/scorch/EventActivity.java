package com.jailbird.scorch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meetic.marypopup.MaryPopup;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyTextView;
import com.yalantis.ucrop.UCrop;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import noman.googleplaces.Place;
import ru.bullyboo.text_animation.AlphaBuilder;
import ru.bullyboo.text_animation.AnimationBuilder;
import ru.bullyboo.text_animation.TextCounter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.key;
import static android.R.attr.name;
import static com.jailbird.scorch.MainActivity.currentEvent;

public class EventActivity extends AppCompatActivity implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, View.OnClickListener {
    private static final int PLACE_PICKER_REQUEST = 333;
    private static final int REQUEST_INVITE = 877;
    ImageView eventImage;
    TextView eventName, eventInfo,eventInfoDetails, admissionText, eventDetails, venueText, goingText, interestedText, shareText,goingNum, interestedNum,dayText,monthText;
    Event event;
    private Context context;
boolean isEdit=false;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView interestView;
    private com.google.android.gms.location.places.Place selectedPlace;
    private String imgUrl;
    private Uri uri;
    CardView interestCard;
    MaryPopup maryPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if(getIntent().hasExtra("notification")){
            event=currentEvent;
        }else {
            event = FeedActivity.thisEvent;
        }
            SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        context = this;
        if(getIntent().hasExtra("isEdit")){
            isEdit=getIntent().getBooleanExtra("isEdit",false);
            Toast.makeText(context, R.string.tap_to_edit, Toast.LENGTH_LONG).show();
        }
        interestCard= (CardView) findViewById(R.id.interest_card);
        interestCard.setOnClickListener(this);
        dayText= (TextView) findViewById(R.id.date_text);
        monthText= (TextView) findViewById(R.id.month_text);
        dayText.setOnClickListener(this);
        goingText = (TextView) findViewById(R.id.going_button);
        interestedText = (TextView) findViewById(R.id.interested_button);
        goingNum = (TextView) findViewById(R.id.going_number);
        interestedNum = (TextView) findViewById(R.id.interested_number);
        shareText= (TextView) findViewById(R.id.share_button);
        shareText.setOnClickListener(this);
        goingText.setOnClickListener(this);
        interestedText.setOnClickListener(this);
        admissionText = (TextView) findViewById(R.id.admission_text);
        admissionText.setOnClickListener(this);
        venueText = (TextView) findViewById(R.id.venue_text);
        venueText.setOnClickListener(this);
        eventImage = (ImageView) findViewById(R.id.app_bar_image);
        eventName = (TextView) findViewById(R.id.event_name);
        eventName.setOnClickListener(this);
        eventDetails = (TextView) findViewById(R.id.details);
        eventName.setText(event.getEventName());
        eventInfo= (TextView) findViewById(R.id.event_info);
        eventInfo.setText(event.eventDetails);
        eventInfoDetails= (TextView) findViewById(R.id.event_info_details);
        eventInfoDetails.setText("");
        Picasso.with(context).load(event.getImgUrl()).into(eventImage);
        //eventDetails.setText(event.getEventDetails());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(event.getEventName());
        }
        eventImage.setOnClickListener(this);
        venueText.setText("Venue: " + event.getPlace().getName() + " | " + event.getEventAddress());
        Date startDate = event.startDate;
        Date endDate=event.endDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        Calendar c=Calendar.getInstance();
        c.setTime(endDate);
        TextView untilText = (TextView) findViewById(R.id.time_until);
        interestView = (RecyclerView) findViewById(R.id.interest_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        interestView.setLayoutManager(gridLayoutManager);
        interestView.setAdapter(new MyInterestRecyclerViewAdapter(event.getInterests(),null,context));
        try {
            String dateDetails = " " + getDayText(startDate) + ", " + getMonth(startDate) + " " + getDay(startDate) + " at " + getTime(startDate) + " - " + getDateTime(c);
            untilText.setText("" + getTimeUntil(Calendar.getInstance(), cal));
            eventDetails.setText(dateDetails);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            dayText.setText(getDay(startDate));
            monthText.setText(getMonth(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        goingNum.setText(String.valueOf(event.getPeopleGoing()));
        interestedNum.setText(String.valueOf(event.getPeopleInterested()));
        getPrice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isEdit){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.event_edit_menu, menu);
            return true;
        }else {
            return super.onCreateOptionsMenu(menu);
        }
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event:
                updateEvent();
                break;
            case R.id.del_event:
                deleteEvent();
                break;
        }
            return super.onOptionsItemSelected(item);
    }

    private void getPrice() {
        String s = event.getEventAdmission();
        int i = Integer.parseInt(s.replaceAll("[\\D]", ""));
        if (i <= 0) {
            admissionText.setText("Admission: Free");
        } else {
            admissionText.setText("Admission: " + event.getEventAdmission());
        }
    }
    private void pickDate() {
        new SingleDateAndTimePickerDialog.Builder(context)
                //.bottomSheet()
                //.curved()
                //.minutesStep(15)
                .title("When is your event?")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        event.startDate = date;
                        event.endDate = date;
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.HOUR, 8);
                        //endDate = cal.getTime();
                    }
                }).display();
    }

    private void addPhoto() {
        final UCrop.Options options = new UCrop.Options();
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setMaxBitmapSize(1024);

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCircleDimmedLayer(false);


        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(context)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        File newFile = new File(getExternalCacheDir() + File.separator + "event" + eventName + ".jpg");
                        Uri tmp = Uri.fromFile(newFile);
                        UCrop.of(uri, tmp)
                                .withOptions(options)
                                .withMaxResultSize(1024, 1024)
                                .start((Activity) context);


                        // here is selected uri
                    }
                })
                .showCameraTile(false)
                .setOnErrorListener(new TedBottomPicker.OnErrorListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Photo ", "Photo error: " + message);

                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }
    private void pickPlace() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();

            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if(requestCode==211 && resultCode==RESULT_OK){
    event.interests= (List<Interest>) data;
}
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            //List<String>placeTypes=new ArrayList<String>();

            // The user has selected a place. Extract the name and address.
            final com.google.android.gms.location.places.Place place = PlacePicker.getPlace(context, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            final String id = place.getId();
            selectedPlace = place;
            //Toast.makeText(context,"Place info: name: "+name+"  Address"+address+" ID "+id,Toast.LENGTH_LONG).show();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }


        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            imgUrl = resultUri.getLastPathSegment();
            uri = resultUri;
            Picasso.with(context).load(resultUri).into(eventImage);
            //collapsingToolbar.setExpanded(true, true);

        }

            super.onActivityResult(requestCode, resultCode, data);

    }

private void makeEditDialog(String item){
    ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
            R.style.YOUR_STYE);

    // mDialog = new Dialog(this,0); // context, theme
    final Dialog mDialog = new Dialog(mTheme);

    View popup = LayoutInflater.from(context).inflate(R.layout.edit_layout, null);
TextView editHeader= (TextView) popup.findViewById(R.id.edit_text);
    final MaterialEditText mainText= (MaterialEditText) popup.findViewById(R.id.post_text);
    TextView okButton= (TextView) popup.findViewById(R.id.post_button);

   if(item.equalsIgnoreCase("Event Name")) {
       mainText.setHint("Edit your Event name");
       okButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String newName = String.valueOf(mainText.getText());

               event.eventName = newName;
               eventName.setText(newName);
               mDialog.dismiss();
           }
       });

   }
   if(item.equalsIgnoreCase("Event Details")){
       mainText.setHint("Edit your Event Details");
       okButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String newName = String.valueOf(mainText.getText());

               event.eventDetails = newName;
               eventInfoDetails.setText(newName);
               mDialog.dismiss();
           }
       });

       //editHeader.setText("Edit " + item);
   }
    editHeader.setText("Edit " + item);

    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    mDialog.setContentView(popup);
    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(mDialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.gravity = Gravity.CENTER;


    mDialog.getWindow().setAttributes(lp);
    mDialog.show();

}
private void updateEvent(){
    new AlertDialog.Builder(context)
                                        .setTitle("Save Event")
                                        .setMessage("Save your changes to "+event.eventName+"?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                mDatabase.child("Events").child(MainActivity.myID).child(event.getEventID()).setValue(event);
                                                mDatabase.child("Users").child(MainActivity.myID).child("My Events").child(event.getEventID()).setValue(event);

                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();




}
private void deleteEvent(){
new AlertDialog.Builder(context)
                                    .setTitle("Delete Event?")
                                    .setMessage("Are you sure you want to delete this Event?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            mDatabase.child("Events").child(MainActivity.myID).child(event.getEventID()).removeValue();
                                            mDatabase.child("Users").child(MainActivity.myID).child("My Events").child(event.getEventID()).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    finish();
                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

}
    private void updateEventG(boolean isUp) {
        Integer g = event.getPeopleGoing();
        Map<String, Object> childUpdates = new HashMap<>();
        if (isUp) {

            event.setPeopleGoing(event.getPeopleGoing() + 1);

            mDatabase.child("Users").child(MainActivity.myID).child("G Events").child(event.getEventID()).setValue(event.getEventID());

        } else {
            event.setPeopleGoing(event.getPeopleGoing() - 1);
            mDatabase.child("Users").child(MainActivity.myID).child("G Events").child(event.getEventID()).removeValue();

        }
        childUpdates.put("/Events/" + MainActivity.myID + "/" + event.eventID, event);

        goingNum.setText(String.valueOf(event.getPeopleGoing()));
        mDatabase.updateChildren(childUpdates);
    }
    private void updateEventInterested(boolean isUp) {
        Integer g = event.getPeopleInterested();
        Map<String, Object> childUpdates = new HashMap<>();
        if (isUp) {

            event.setPeopleInterested(event.getPeopleInterested() + 1);

            mDatabase.child("Users").child(MainActivity.myID).child("I Events").child(event.getEventID()).setValue(event.getEventID());

        } else {
            event.setPeopleInterested(event.getPeopleInterested() - 1);
            mDatabase.child("Users").child(MainActivity.myID).child("I Events").child(event.getEventID()).removeValue();

        }
        childUpdates.put("/Events/" + MainActivity.myID + "/" + event.eventID, event);

        interestedNum.setText(String.valueOf(event.getPeopleInterested()));
        mDatabase.updateChildren(childUpdates);
    }

    @Override
    protected void onResume() {
        getmeGoing();
        getmeInterested();
        super.onResume();
    }

    private void getmeGoing() {
        DatabaseReference ref = mDatabase.child("Users").child(MainActivity.myID).child("G Events");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.v("Events", "Event " + snapshot.getValue());
                        //Event thisEvent = snapshot.getValue(Event.class);
                        String evID=snapshot.getValue().toString();
                        if (evID.matches(event.getEventID())) {
                            goingText.setSelected(true);
                            goingText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_favorite_dark, 0, 0);
                            goingText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getmeInterested() {
        DatabaseReference ref = mDatabase.child("Users").child(MainActivity.myID).child("I Events");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.v("Events", "Event " + snapshot.getValue());
                        //Event thisEvent = snapshot.getValue(Event.class);
                        String evID=snapshot.getValue().toString();
                        if (evID.matches(event.getEventID())) {
                            interestedText.setSelected(true);
                            interestedText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_done_dark, 0, 0);

                            interestedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getTimeUntil(Calendar start, Calendar end) {
        String dateDiff = "";
        DateTime dateTime1 = new DateTime(start);
        DateTime dateTime2 = new DateTime(end);
        int days = Days.daysBetween(dateTime1, dateTime2).getDays();

        int weeks = Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
        int months = Months.monthsBetween(dateTime1, dateTime2).getMonths();
        if (days <= 7) {
            dateDiff = "This week";
        } else if (weeks <= 4) {
            dateDiff = "" + weeks + " Weeks";
        } else {
            dateDiff = " " + months + " Months";
        }

        return dateDiff;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng eventLocation = new LatLng(event.getPlace().getLatitude(), event.getPlace().getLongitude());

        googleMap.addMarker(new MarkerOptions().position(eventLocation)
                .title("" + event.getEventName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));

    }

    private static String getMonth(Date date) throws ParseException {
        //Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;
    }

    private static String getDateTime(Calendar cal) {
        //cal.add(Calendar.HOUR, 8);

        String month = new SimpleDateFormat("MMM").format(cal.getTime());
        String day = new SimpleDateFormat("dd").format(cal.getTime());
        String time = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        String dayText = new SimpleDateFormat("E").format(cal.getTime());
        String dateTime = dayText + ", " + month + " " + day + " at " + time;
        return dateTime;
    }

    private static String getDay(Date date) throws ParseException {
        //Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("dd").format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;
    }

    private static String getDayText(Date date) throws ParseException {
        //Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("E").format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;
    }

    private static String getTime(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("hh:mm a", Locale.US).format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;

    }
    private void editPrice() {
        LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(
                LAYOUT_INFLATER_SERVICE);
        ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);

        View mView = mInflater.inflate(R.layout.dollar_picker_layout, null);
        // mDialog = new Dialog(this,0); // context, theme
        final EasyMoneyEditText moneyEditText = (EasyMoneyEditText) mView.findViewById(R.id.moneyEditText);
        final EasyMoneyTextView moneyTextView = (EasyMoneyTextView) mView.findViewById(R.id.moneyTextView);
        final Spinner spinnerCurrency = (Spinner) mView.findViewById(R.id.spinnerCurrency);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String itemName = spinnerCurrency.getSelectedItem().toString();
                String symbol = itemName.substring(itemName.indexOf("(") + 1, itemName.indexOf(")"));
                moneyEditText.setCurrency(symbol);
                moneyTextView.setCurrency(symbol);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);

        alertbox.setMessage("Admission Price"); // Message to be displayed
        alertbox.setView(mView);
        alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                event.eventAdmission = moneyEditText.getFormattedString();
                admissionText.setText(event.eventAdmission);
            }

        });

        alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub

            }

        });

// show the alert box will be swapped by other code later
        alertbox.show();


        //TextView someText = (TextView) mView.findViewById(R.id.textViewID);
        // do some thing with the text view or any other view

    }


    private void showPhoto(String path) {
        View popup = LayoutInflater.from(context).inflate(R.layout.gallery_layout, null, false);
        final ScrollGalleryView scrollGalleryView = (ScrollGalleryView) popup.findViewById(R.id.scroll_gallery_view);
        List<MediaInfo> infos = new ArrayList<>();
        //String path="file://"+uri.getPath();
        infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(path)));
        scrollGalleryView
                .setThumbnailSize(200)
                .setZoom(true)

                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);

        maryPopup = MaryPopup.with((Activity) context)
                .cancellable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                .backgroundColor(Color.parseColor("#EFF4F5"))
                .content(popup)
                .from(eventImage)
                .center(true);
        maryPopup.show();
        /*ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);
        final Dialog mDialog = new Dialog(mTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;


        mDialog.getWindow().setAttributes(lp);
        mDialog.show();*/
        final ImageView more = (ImageView) popup.findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.place_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }

        });

    }

    @Override
    public void onBackPressed() {
       if(maryPopup!=null) {
           if (!maryPopup.isOpened()) {
               super.onBackPressed();
           } else {
               maryPopup.close(true);
           }
       }else{
           super.onBackPressed();
       }
    }

    private void showShareDialog() {

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
                String shareBody = "Check out this Event!";
                String body = "Check out this Event, "+event.eventName+", on Scorch! \n"+ getResources().getString(R.string.invitation_deep_link);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
        // do some thing with the text view or any other view

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
    private void onInviteClicked() {
        String name = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        Intent intent = new AppInviteInvitation.IntentBuilder("Check out this Event!")
                .setMessage("Check out this Event: "+event.eventName+", on Scorch!")
                //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse("https://jailbirdinteractive.com/images/newfire.png"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_bar_image:
                if(isEdit){
                    addPhoto();
                }else {
                    showPhoto(event.getImgUrl());
                }
                    break;
            case R.id.share_button:
                showShareDialog();
                break;
            case R.id.admission_text:
                editPrice();
                break;
            case R.id.going_button:
                if (!goingText.isSelected()) {
                    goingText.setSelected(true);
                    goingText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_favorite_dark, 0, 0);

                    goingText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    updateEventG(true);


                } else {
                    goingText.setSelected(false);
                    goingText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_favorite, 0, 0);

                    goingText.setTextColor(Color.GRAY);
                    updateEventG(false);
                }
                break;
            case R.id.interested_button:
                if (!interestedText.isSelected()) {
                    interestedText.setSelected(true);
                    interestedText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_done_dark, 0, 0);

                    interestedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    updateEventInterested(true);


                } else {
                    interestedText.setSelected(false);
                    interestedText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_action_done, 0, 0);

                    interestedText.setTextColor(Color.GRAY);

                    updateEventInterested(false);
                }
                break;
            case R.id.date_text:
                if(isEdit){
                    pickDate();
                }
                break;
            case R.id.event_name:
                makeEditDialog("Event Name");
                break;
            case R.id.venue_text:
               if(isEdit)
                pickPlace();
                break;
            case R.id.interest_card:
                if(isEdit)
                    //startActivityForResult(new Intent(context, PickerActivity.class).putExtra("isEvent", true),211);
break;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
