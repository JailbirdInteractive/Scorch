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
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseData;
import com.anjlab.android.iab.v3.PurchaseState;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meetic.marypopup.MaryPopup;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyEditText;
import com.wajahatkarim3.easymoneywidgets.EasyMoneyTextView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;
import noman.googleplaces.*;
import noman.googleplaces.Location;
import noman.googleplaces.Place;

import static com.jailbird.scorch.MainActivity.myID;
import static com.jailbird.scorch.MainActivity.myLocation;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler {
    private static final int PLACE_PICKER_REQUEST = 111;
    Context context;
    Toolbar toolbar;
    LinearLayout boost8, boost12, boost24;
    String product="";

    MaterialEditText eventNameTxt;
    String eventName, imgUrl, eventDetails, eventAdmission;
    Place eventPlace;
    EditText details;
    public static List<Interest> selectedInterests;
    com.google.android.gms.location.places.Place selectedPlace;
    Date startDate, endDate;
    ImageView eventImage, photo_button;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private CoordinatorLayout coordinatorLayout;
    AppBarLayout collapsingToolbar;
    TextView costView;
    Uri uri;
    GeoFire geoFire;
    private boolean isBoosted = false;
    FloatingActionButton boosterButton;
    private int duration;
    private BillingProcessor bp;
    MaryPopup maryPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        context = this;
        bp = new BillingProcessor(this, getString(R.string.licence_key), this);

        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        collapsingToolbar = (AppBarLayout) findViewById(R.id.appbar);
        selectedInterests = new ArrayList<Interest>();
        //selectedInterests.add(Interests.Alc_Drinks);
        geoFire = new GeoFire(mDatabase.child("Event Locations"));
        costView = (TextView) findViewById(R.id.cost_button);
        eventNameTxt = (MaterialEditText) findViewById(R.id.materialEditText);
        eventNameTxt.setMinCharacters(1);
        eventNameTxt.setMaxCharacters(30);
        eventNameTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        boosterButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        boosterButton.setOnClickListener(this);
        details = (EditText) findViewById(R.id.details_button);
        eventImage = (ImageView) findViewById(R.id.app_bar_image);
        eventImage.setOnClickListener(this);
        photo_button = (ImageView) findViewById(R.id.add_photo);
        photo_button.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.time_button).setOnClickListener(this);
        findViewById(R.id.details_button).setOnClickListener(this);
        findViewById(R.id.interest_button).setOnClickListener(this);
        findViewById(R.id.location_button).setOnClickListener(this);
        findViewById(R.id.cost_button).setOnClickListener(this);
        eventAdmission = "$ 0";
        eventDetails = "";
        startDate = Calendar.getInstance().getTime();
        duration=8;
    }

    @Override
    public void onBackPressed() {


            if (!isBoosted) {
                new AlertDialog.Builder(context)
                        .setTitle("Discard Event?")
                        .setMessage(R.string.discard_event)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //super.onBackPressed();

            } else {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.discard_boosted)
                        .setMessage(R.string.discard_boosted_prompt)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
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

    }

    private void makeBoost() {
        List<SkuDetails> boost_skus = new ArrayList<>();
        ArrayList<String> boostIds = new ArrayList<String>(Arrays.asList("boost_8", "boost_12", "boost_24"));
        boost_skus = bp.getPurchaseListingDetails(boostIds);

        View popup = LayoutInflater.from(context).inflate(R.layout.boost_event_layout, null);
        Button button = (Button) popup.findViewById(R.id.boost_confirm);
        popup.findViewById(R.id.boost_12);
        boost8 = (LinearLayout) popup.findViewById(R.id.boost_8);
        boost12 = (LinearLayout) popup.findViewById(R.id.boost_12);
        boost24 = (LinearLayout) popup.findViewById(R.id.boost_24);
        TextView b8 = (TextView) popup.findViewById(R.id.boost_price8);
        TextView b12 = (TextView) popup.findViewById(R.id.boost_price12);
        TextView b24 = (TextView) popup.findViewById(R.id.boost_price24);

        if (!boost_skus.isEmpty()) {
            b8.setText(boost_skus.get(2).priceText);
            b12.setText(boost_skus.get(0).priceText);
            b24.setText(boost_skus.get(1).priceText);
        }
        final TextSwitcher mSwitcher = (TextSwitcher) popup.findViewById(R.id.boost_text_switcher);

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(context);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(13);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        // Declare the in and out animations and initialize them

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
        mSwitcher.setText("Boost the visibility of your Event");
        mSwitcher.postDelayed(new Runnable() {
            int i = 0;

            public void run() {
                mSwitcher.setText(
                        i++ % 2 == 0 ?
                                "Show up first in your area" :
                                "Send notifications when your Event starts");
                mSwitcher.postDelayed(this, 3002);
            }
        }, 3002);
        final ImageSwitcher imageSwitcher = (ImageSwitcher) popup.findViewById(R.id.boost_switcher);
        //imageSwitcher.setImageResource(R.drawable.ic_launcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
        imageSwitcher.setImageResource(R.drawable.blue_rocket);
        imageSwitcher.postDelayed(new Runnable() {
            int i = 0;

            public void run() {
                imageSwitcher.setImageResource(
                        i++ % 3 == 0 ?
                                R.drawable.icon_location :
                                R.drawable.flat_city);
                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);

        ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);

        // mDialog = new Dialog(this,0); // context, theme
        final Dialog mDialog = new Dialog(mTheme);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.boost_8:
                        duration = 8;
product="boost_8";
                        view.setSelected(!view.isSelected());
//isBoosted=view.isSelected();
                        if (boost12 != null && boost24 != null) {
                            boost12.setSelected(false);
                            boost24.setSelected(false);
                        }
                        Log.v("Product","product: "+product);

                        break;
                    case R.id.boost_12:
                        product="boost_12";

                        duration = 12;
                        if (boost8 != null && boost24 != null) {
                            boost8.setSelected(false);
                            boost24.setSelected(false);
                        }
                        view.setSelected(!view.isSelected());
                        //isBoosted=view.isSelected();
                        Log.v("Product","product: "+product);

                        break;
                    case R.id.boost_24:
                        product="boost_24";

                        duration = 24;
                        if (boost12 != null && boost8 != null) {
                            boost12.setSelected(false);
                            boost8.setSelected(false);
                        }
                        view.setSelected(!view.isSelected());
                        //isBoosted=view.isSelected();
                        Log.v("Product","product: "+product);

                        break;
                    case R.id.boost_confirm:
                        bp.purchase(CreateEventActivity.this,product);

                        mDialog.dismiss();
                        break;
                }
            }
        };
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        button.setOnClickListener(listener);
        popup.findViewById(R.id.boost_8).setOnClickListener(listener);
        popup.findViewById(R.id.boost_12).setOnClickListener(listener);
        popup.findViewById(R.id.boost_24).setOnClickListener(listener);

        mDialog.getWindow().setAttributes(lp);
        mDialog.show();


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
                        startDate = date;
                        //endDate = date;
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.HOUR, 8);
                        endDate = cal.getTime();
                    }
                }).display();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_button:
                pickDate();
                break;
            case R.id.location_button:
                pickPlace();
                break;
            case R.id.interest_button:
                startActivity(new Intent(context, PickerActivity.class).putExtra("isEvent", true));
                break;
            case R.id.add_photo:
                addPhoto();
                break;
            case R.id.app_bar_image:
                //showPhoto();
                break;
            case R.id.cost_button:
                showCustomDialog();
                break;
            case R.id.floatingActionButton:
                makeBoost();
                break;
            case R.id.boost_8:
                duration = 8;

                view.setSelected(!view.isSelected());
                if (boost12 != null && boost24 != null) {
                    boost12.setSelected(false);
                    boost24.setSelected(false);
                }
                break;
            case R.id.boost_12:
                duration = 12;
                if (boost8 != null && boost24 != null) {
                    boost8.setSelected(false);
                    boost24.setSelected(false);
                }
                view.setSelected(!view.isSelected());
                break;
            case R.id.boost_24:

                duration = 24;
                if (boost12 != null && boost8 != null) {
                    boost12.setSelected(false);
                    boost8.setSelected(false);
                }
                view.setSelected(!view.isSelected());
                break;
        }
    }

    private void showCustomDialog() {
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
                eventAdmission = moneyEditText.getFormattedString();
                costView.setText(eventAdmission);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = String.valueOf(eventNameTxt.getText());

        switch (item.getItemId()) {
            case R.id.create_event:
//makeBoost();
                if (imgUrl == null) {
                    Snackbar.make(coordinatorLayout, R.string.img_select_prompt, Snackbar.LENGTH_INDEFINITE).setAction("Choose Photo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addPhoto();
                        }
                    }).show();

                } else {
                    if (selectedPlace != null && !selectedInterests.isEmpty()) {
                        if (!isBoosted) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Create Event?")
                                    .setMessage("Create the Event "+name+"?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            getTypes(selectedPlace, selectedInterests);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            //super.onBackPressed();

                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Create your Boosted Event?")
                                    .setMessage("Create the Boosted Event "+name+"?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            //finish();
                                            getTypes(selectedPlace, selectedInterests);

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
                    } else {
                        Snackbar.make(coordinatorLayout, R.string.location_select_prompt, Snackbar.LENGTH_LONG).show();

                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
            collapsingToolbar.setExpanded(true, true);

        }
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

    }

    private void getTypes(final com.google.android.gms.location.places.Place place, final List<Interest> interests) {
        String name = String.valueOf(eventNameTxt.getText());
        final int min = 10;
        final int max = 30;
        Random r=new Random();
        final int intnum = r.nextInt((max - min) + 1) + min;
        final int gNum=r.nextInt((max - min) + 1) + min;
        if (!name.isEmpty() && eventNameTxt.isCharactersCountValid()) {
            eventName = String.valueOf(eventNameTxt.getText());
        } else {
            eventNameTxt.setError("Your Event must have a name");

        }
        eventDetails = String.valueOf(details.getText());

        new AsyncTask<Void, Void, Void>() {
            List<String> placeTypes = new ArrayList<String>();
            String address = "";

            @Override
            protected Void doInBackground(Void... voids) {
                if (!place.getPlaceTypes().isEmpty()) {
                    for (Integer t : place.getPlaceTypes()) {
                        Field[] fields = com.google.android.gms.location.places.Place.class.getDeclaredFields();

                        for (Field field : fields) {
                            Class<?> type = field.getType();

                            if (type == int.class) {
                                try {

                                    if (t == field.getInt(null)) {
                                        //Toast.makeText(context,"types:"+field.getName(),Toast.LENGTH_LONG).show();
                                        //Log.i("Place", "onCreate: " + field.getName());
                                        String s = field.getName().substring(field.getName().indexOf("_") + 1).toLowerCase();
                                        Log.i("Place", "subString: " + s);

                                        placeTypes.add(s);
                                        //break;
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                for (Interest interest : interests) {
                    for (String s : interest.getPlaceTypes()) {
                        if (!placeTypes.contains(s)) {
                            placeTypes.add(s);

                        }
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                final String uniqueID = UUID.randomUUID().toString();
                if (place.getAddress().toString() != null) {
                    address = place.getAddress().toString();
                }
                String name = "";
                if (place.getPlaceTypes().size() <= 1) {
                    name = eventName;
                } else {
                    name = place.getName().toString();
                }

                Location targetLocation = new Location("");
                targetLocation.setLatitude(place.getLatLng().latitude);
                targetLocation.setLongitude(place.getLatLng().longitude);
                eventPlace = new Place.Builder().name(name).placeId(place.getId()).types(placeTypes).location(targetLocation).build();
                final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.photo_bucket)).child("Event Images/" + eventPlace.getPlaceId());

                StorageReference photoRef = storageRef.child("images/" + imgUrl);
                UploadTask uploadTask = photoRef.putFile(uri);

// Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Snackbar.make(coordinatorLayout, " Event Created! ", Snackbar.LENGTH_SHORT).show();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Event event;
if(!isBoosted) {
    event = new Event(uniqueID, address, eventPlace, downloadUri.toString(), eventName, eventDetails, interests, startDate, endDate, eventAdmission, 1, 1, isBoosted);
}else{
    event = new Event(uniqueID, address, eventPlace, downloadUri.toString(), eventName, eventDetails, interests, startDate, endDate, eventAdmission, intnum, gNum, isBoosted);
}
                        geoFire.setLocation(uniqueID, new GeoLocation(eventPlace.getLatitude(), eventPlace.getLongitude()), new GeoFire.CompletionListener() {

                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                //startService(new Intent(getApplicationContext(), LoaderService.class));

                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }
                            }
                        });
                        mDatabase.child("Events").child(myID).child(uniqueID).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
mDatabase.child("Users").child(myID).child("My Events").child(uniqueID).setValue(event);
                    }
                });
                super.onPostExecute(aVoid);

            }
        }.execute();

    }

    private void showPhoto() {
        View popup = LayoutInflater.from(context).inflate(R.layout.gallery_layout, null, false);
        final ScrollGalleryView scrollGalleryView = (ScrollGalleryView) popup.findViewById(R.id.scroll_gallery_view);
        List<MediaInfo> infos = new ArrayList<>();
        String path = "file://" + uri.getPath();
        infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(path)));
        scrollGalleryView
                .setThumbnailSize(200)
                .setZoom(true)

                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);

        MaryPopup maryPopup = MaryPopup.with((Activity) context)
                .cancellable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                .backgroundColor(Color.parseColor("#EFF4F5"))
                .content(popup)
                .from(eventImage)
                .center(true);
        maryPopup.show();

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
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
      PurchaseState state=details.purchaseInfo.purchaseData.purchaseState;

        showToast("Your Event is boosted!");
        isBoosted = true;
        bp.consumePurchase(productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
