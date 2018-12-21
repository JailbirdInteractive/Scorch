package com.jailbird.scorch;


import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meetic.marypopup.MaryPopup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;
import noman.googleplaces.Place;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.app.Activity.RESULT_OK;
import static com.jailbird.scorch.MainActivity.isPaid;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, MainActivity.OnListFragmentInteractionListener, ContactSelectionListener {
    private static final int CONTACT_PICKER_REQUEST = 747;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private CoordinatorLayout coordinatorLayout;
    Context context;
    CircleImageView profilePic;
    ImageView background, settingButton, removeAdsButton;
    String myId = "User ID";
    String myName = "User Name";
    TextView userName;
    List<Interest> myInterests;
    RecyclerView interestRecyclerView, favRecycler;
    private GoogleApiClient mGoogleApiClient;
    MainActivity.OnListFragmentInteractionListener mListener;
    File file;
    List<Object> myEvents;
    private RecyclerView eventRecycler;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = getActivity();
        mGoogleApiClient = FeedActivity.mGoogleApiClient;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                int index = id.lastIndexOf(".");

                if (id != null) {
                    String sub = id.replace(com, rep);
                    myId = sub.replaceAll("[-+.^:,]", "");
                } else {
                    myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }
            }

            myEvents = new ArrayList<>();

            //myId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        //getMyEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_account, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.main_content);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Scorch Images/My Avatar");
        myDir.mkdirs();
        String fname = "avatar.jpg";
        file = new File(myDir, fname);
        settingButton = (ImageView) view.findViewById(R.id.settings_button);
        settingButton.setOnClickListener(this);
        userName = (TextView) view.findViewById(R.id.user_name);


        userName.setText(myName);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);
        final android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(myName);

            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_black_48dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        myInterests = new ArrayList<>();
        myInterests = MainActivity.myInterests;
        ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        Button signoutButton = (Button) view.findViewById(R.id.sign_out_button);
        signoutButton.setOnClickListener(this);
        favRecycler = (RecyclerView) view.findViewById(R.id.favorites_layout);
        mListener = this;

        setupLists(view);
        removeAdsButton = (ImageView) view.findViewById(R.id.remove_ads_button);
        removeAdsButton.setOnClickListener(this);
        RelativeLayout removelayout= (RelativeLayout) view.findViewById(R.id.remove_button);
        if(isPaid){
    removelayout.setVisibility(View.GONE);
}
        profilePic = (CircleImageView) view.findViewById(R.id.profile_image);
        background = (ImageView) view.findViewById(R.id.backgroundImage);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
        FloatingActionButton interestButton = (FloatingActionButton) view.findViewById(R.id.interest_button);
        interestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PickerActivity.class).putExtra("isEdit", true));

            }
        });
        FloatingActionButton friendButton = (FloatingActionButton) view.findViewById(R.id.friend_button);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchContactPicker(view);
            }
        });
        refreshPhoto();
        getEvents();
        return view;
    }

    private void getEvents() {
        final EventFragment.OnListFragmentInteractionListener listener = new EventFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Event item) {
                FeedActivity.thisEvent = item;
                startActivity(new Intent(context, EventActivity.class).putExtra("isEdit", true));
            }
        };
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(myId).child("My Events");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot friendSnap : dataSnapshot.getChildren()) {
                        Event event = friendSnap.getValue(Event.class);
                        Log.d("Events", event.toString());
                        if (!myEvents.contains(event)) {
                            if (event.isBoosted) {
                                myEvents.add(0, event);
                            } else {
                                myEvents.add(event);

                            }
                        }
                        eventRecycler.setAdapter(new MyEventAdapter(myEvents, listener));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMyEvents() {
        DatabaseReference ref = mDatabase.child("Events");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    Log.d("Events", "Event1 " + dataSnapshot.getValue());

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(Event.class);
                        myEvents.add(event);
                        Log.d("Events", "Event2 " + myEvents.get(0));

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
              /*
                case R.id.action_settings:
                    startActivity(new Intent(context,SettingsActivity.class));

                    break;
                case R.id.invite:
                    //launchContactPicker(null);
                    startActivity(new Intent(context,FriendActivity.class));


                    break;
*/
            case R.id.edit_photo:
                addPhoto();

                break;

            case R.id.edit_interest:
                startActivity(new Intent(context, PickerActivity.class).putExtra("isEdit", true));


                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void showRequests() {
        RequestAdapter adapter = new RequestAdapter(MainActivity.requesters);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View popup = LayoutInflater.from(context).inflate(R.layout.request_layout, null, false);
        RecyclerView requestView = (RecyclerView) popup.findViewById(R.id.request_list);
        requestView.setLayoutManager(manager);
        requestView.setAdapter(adapter);

        MaryPopup maryPopup = MaryPopup.with((Activity) context)
                .cancellable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                //.backgroundColor(Color.parseColor("#EFF4F5"))
                .content(popup)

                .from(interestRecyclerView)
                .center(true);
        maryPopup.show();
    }

    public void launchContactPicker(View view) {
        // int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        new Permissive.Request(android.Manifest.permission.READ_CONTACTS).whenPermissionsGranted(new PermissionsGrantedListener() {
            @Override
            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                Intent contactPicker = new Intent(context, ContactPickerActivity.class);
                //Don't show Chips
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SHOW_CHIPS, true);

                //Customize Floating action button color
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_FAB_COLOR, "#7f8be9");
                //Customize Selection drawable
                String customSelection = "(" + ContactsContract.Data.MIMETYPE + "=? )";
                String[] customArgs = new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION, customSelection);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_ARGS, customArgs);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS, true);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_DRAWABLE, PickerUtils.sendDrawable(getResources(), R.drawable.ic_done_white_36dp));
                startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);

            }
        })


                .execute(getActivity());

    }

    private void sendFriendRequest(String id) {
        FriendRequest friendRequest = new FriendRequest(myId, false, myName);
        mDatabase.child("Friend Requests").child(id).push().setValue(friendRequest);

    }

    @Override
    public void onResume() {
        getPhotos(myId);
        super.onResume();
    }

    private void setupLists(View view) {
        Log.d("Lists", "Setting up lists");
        FavoriteAdapter adapter = new FavoriteAdapter(MainActivity.myFavorites, mListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        GridLayoutManager friendLayoutManager = new GridLayoutManager(context, 4);
        GridLayoutManager eventLayoutManager = new GridLayoutManager(context, 3);
        MyEventAdapter eventRecyclerViewAdapter = new MyEventAdapter(myEvents, null);
        eventRecyclerViewAdapter.setHasStableIds(true);
        eventRecycler = (RecyclerView) view.findViewById(R.id.myevents_layout);

        eventRecycler.setLayoutManager(eventLayoutManager);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        RecyclerView friendView = (RecyclerView) view.findViewById(R.id.friends_layout);
        FriendAdapter friendAdapter = new FriendAdapter(MainActivity.myFriends, mListener);
        friendView.setLayoutManager(friendLayoutManager);
        friendView.setAdapter(friendAdapter);
        interestRecyclerView = (RecyclerView) view.findViewById(R.id.interest_layout);
        interestRecyclerView.setAdapter(new MyInterestRecyclerViewAdapter(myInterests, null, context));
        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        interestRecyclerView.setLayoutManager(gridLayoutManager);
        interestRecyclerView.addItemDecoration(new DividerItemDecoration(spanCount, spacing, includeEdge));
        eventRecycler.addItemDecoration(new DividerItemDecoration(spanCount, spacing, includeEdge));

        adapter.setHasStableIds(true);
        // eventRecycler.setAdapter(eventRecyclerViewAdapter);

        favRecycler.setLayoutManager(layoutManager);
        favRecycler.setAdapter(adapter);
    }

    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }


    private void addPhoto() {
        final UCrop.Options options = new UCrop.Options();
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setMaxBitmapSize(1024);

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCircleDimmedLayer(true);


        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(context)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        File newFile = new File(getActivity().getExternalCacheDir() + File.separator + "avatar.jpg");
                        Uri tmp = Uri.fromFile(newFile);
                        UCrop.of(uri, tmp)
                                .withOptions(options)
                                .withMaxResultSize(512, 512)
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

        tedBottomPicker.show(getActivity().getSupportFragmentManager());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONTACT_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    String id = "";
                    TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>) data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                    for (SimpleContact selectedContact : selectedContacts) {
                        id = selectedContact.getCommunication();
                        sendFriendRequest(id.replace(".com", ""));

                        Log.e("Selected", selectedContact.toString());
                    }
                } else
                    Toast.makeText(context, "No contacts selected", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.photo_bucket)).child("Profile Images/" + myId);

            final StorageReference photoRef = storageRef.child("images/" + resultUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(resultUri);

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
                    Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();
                    Uri downloadUri = Uri.parse(photoRef.getPath());

                    //DatabaseReference myRef= mDatabase.child("trails").push();
                    //myRef.setValue(img);
                    mDatabase.child("Users").child(myId).child("User").child("photoUrl").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getPhotos(myId);
                        }
                    });

                }
            });


            /*
            Bitmap bmp = BitmapFactory.decodeFile(resultUri.getPath());//your image
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
            bmp.recycle();
            byte[] byteArray = bYtE.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            mDatabase.child("Users").child(myId).child("Profile Pic").setValue(encodedImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();
                    getPhotos(myId);
                }
            });
            */

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Snackbar.make(coordinatorLayout, " Something went wrong! ", Snackbar.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void signOut() {
        new AlertDialog.Builder(context)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (FeedActivity.mGoogleApiClient.isConnected()) {
                            signOutFromGplus();
                        }
                        if (LoginActivity.mAuth != null) {
                            LoginActivity.mAuth.signOut();
                        } else {
                            LoadingActivity.mAuth.signOut();
                        }
                        getActivity().stopService(new Intent(context, NotificationService.class));
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //startActivity(new Intent(context, LoginActivity.class));
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

    private void refreshPhoto() {
        DatabaseReference ref = mDatabase.child("Users").child(myId).child("User").child("photoUrl");
//recyclerView.removeAllViews();


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                  /*
                    String img = (String) dataSnapshot.getValue();
                    byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilePic.setImageBitmap(decodedByte);
                    */
                    MainActivity.myAvatarUrl = (String) dataSnapshot.getValue();
                    Picasso.with(context).load((String) dataSnapshot.getValue()).placeholder(R.drawable.newfire).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Blurry.with(context).capture(profilePic).into(background);

                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPhotos(String id) {

        DatabaseReference ref = mDatabase.child("Users").child(id).child("User").child("photoUrl");

        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.e("Post ", "Picture: " + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                  /*
                    String img = (String) dataSnapshot.getValue();
                    byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilePic.setImageBitmap(decodedByte);
                    */
                    MainActivity.myAvatarUrl = (String) dataSnapshot.getValue();
                    Picasso.with(context).load((String) dataSnapshot.getValue()).placeholder(R.drawable.newfire).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Blurry.with(context).capture(profilePic).into(background);

                        }

                        @Override
                        public void onError() {

                        }
                    });
                    //Glide.with(PlaceInfoActivity.this).load(postsnap.getValue()).into(frissonView);


                    //Log.e("Photo ", "Photo Url: " + dataSnapshot.getValue());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                addPhoto();
                //startActivity(new Intent(context,ProfileActivity.class));
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.settings_button:
                startActivity(new Intent(context, SettingsActivity.class));
                break;
            case R.id.remove_ads_button:
                FeedActivity.bp.purchase(getActivity(), "remove_ads");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onListFragmentInteraction(HotPlaces item, User user, Place place) {
        if (item == null && user == null && place != null) {
            MapsActivity.thisPlace = place;
            startActivity(new Intent(context, PlaceInfoActivity.class));
        } else if (user != null && item == null) {
            MapsActivity.currentFriend = user;
            startActivity(new Intent(context, ProfileActivity.class).putExtra("userId", user.userID).putExtra("photoUrl", user.photoUrl).putExtra("userName", user.username));

        }
    }


    @Override
    public void onContactSelected(Contact contact, String communication) {
        Log.e("contact", "contact: " + contact.toString() + " comm " + communication);

    }

    @Override
    public void onContactDeselected(Contact contact, String communication) {

    }


}


