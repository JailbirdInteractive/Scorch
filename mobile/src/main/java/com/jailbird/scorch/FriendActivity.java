package com.jailbird.scorch;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.meetic.marypopup.MaryPopup;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;
import noman.googleplaces.Place;

import static com.marcoscg.easylicensesdialog.EasyLicensesDialog.context;

public class FriendActivity extends AppCompatActivity implements ContactSelectionListener {

    private static final int CONTACT_PICKER_REQUEST = 999;
    private String myId = "UID";
    private String myName = "User Name";
    private DatabaseReference mDatabase;
    Context context;
    List<PlaceInvite> placeInviteList = new ArrayList<PlaceInvite>();
    RecyclerView requestView;
    SpaceNavigationView spaceNavigationView;
    Toolbar toolbar;
    boolean isInvite=false;
    InviteAdapter adapter;
    RequestAdapter rAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = this;
       requestView= (RecyclerView) findViewById(R.id.request_list);
        placeInviteList=MainActivity.placeInviteList;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            //actionBar.setDisplayHomeAsUpEnabled(true);

        }
        toolbar.setTitle("Friend Requests");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                if (id != null) {
                    String sub = id.replace(com, rep);
                    myId = sub.replaceAll("[-+.^:,]", "");
                }
            }else{
                myId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

            //myId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        showRequests();
        getInvites();
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Requests", R.drawable.ic_fire_empty));

        spaceNavigationView.addSpaceItem(new SpaceItem("Invites", R.drawable.ic_place_white_24dp));
        spaceNavigationView.showTextOnly();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                launchContactPicker(null);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
switch(itemIndex){
    case 0:

        showRequests();
        break;

    case 1:

        showInvites();

        break;
}
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex){
                    case 0:

                        showRequests();
                        break;

                    case 1:

                        showInvites();

                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isInvite){
            clearInvites();
        }else{
            clearRequests();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInvites() {
        DatabaseReference ref = mDatabase.child("Users").child(myId).child("Invites");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.e("Invites","invite ");

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        PlaceInvite placeInvite = d.getValue(PlaceInvite.class);
                        Log.e("Invites","invite "+placeInvite.toString());
if(!MainActivity.placeInviteList.contains(placeInvite))
                        MainActivity.placeInviteList.add(placeInvite);
                    }
                    int badge = MainActivity.placeInviteList.size();
                    if (badge > 0&&spaceNavigationView.getChildCount()>0)
                        spaceNavigationView.showBadgeAtIndex(1, badge, getResources().getColor(R.color.colorAccent));
                }else{
                    Log.e("Invites","invite null");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showRequests() {
        toolbar.setTitle(R.string.f_request);
        isInvite=false;
        rAdapter = new RequestAdapter(MainActivity.requesters);

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        requestView.setLayoutManager(manager);
        requestView.setAdapter(rAdapter);


    }
    private void clearInvites(){
        if (MainActivity.placeInviteList.size()>0) {

            new AlertDialog.Builder(context)
                .setTitle("Clear Invites")
                .setMessage(R.string.clear_invites)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                            DatabaseReference ref = mDatabase.child("Users").child(myId).child("Invites");
                            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    MainActivity.placeInviteList.clear();
                                    adapter.notifyDataSetChanged();
                                    requestView.invalidate();
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

    }
    private void clearRequests(){
        if (MainActivity.friendRequests.size()>0) {

            new AlertDialog.Builder(context)
                .setTitle("Clear Friend requests")
                .setMessage(R.string.clear_requests)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                            DatabaseReference ref = mDatabase.child("Friend Requests").child(myId);
                            ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    MainActivity.friendRequests.clear();
                                    MainActivity.requesters.clear();
                                    rAdapter.notifyDataSetChanged();
                                    requestView.invalidate();
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
    }
private void showInvites(){
    isInvite=true;
    toolbar.setTitle(R.string.i_text);
    adapter = new InviteAdapter(MainActivity.placeInviteList,context);
    LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    requestView.removeAllViews();
    requestView.setLayoutManager(manager);
    requestView.setAdapter(adapter);
}
    public void launchContactPicker(View view) {
        // int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Permissive.Request(android.Manifest.permission.READ_CONTACTS).whenPermissionsGranted(new PermissionsGrantedListener() {
                @Override
                public void onPermissionsGranted(String[] permissions) throws SecurityException {
                    Intent contactPicker = new Intent(getApplicationContext(), ContactPickerActivity.class);
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


                    .execute(FriendActivity.this);
        } else {
            Intent contactPicker = new Intent(getApplicationContext(), ContactPickerActivity.class);
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

    }

    private void sendFriendRequest(String id) {
        FriendRequest friendRequest = new FriendRequest(myId, false, myName);
        mDatabase.child("Friend Requests").child(id).child(myId).setValue(friendRequest);

    }

    void shareSetup() {
        String shareBody = "" + myName + " " + getString(R.string.friend_invite);
        String body = getString(R.string.join_us) + " " + getResources().getString(R.string.invitation_deep_link);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case CONTACT_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    new AlertDialog.Builder(context)
                            .setTitle("Adding friends")
                            .setMessage(R.string.adding_prompt)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    String id = "";

                                    TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>) data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                                    for (SimpleContact selectedContact : selectedContacts) {
                                        id = selectedContact.getCommunication();
                                        String sub=id.replace(".com","");
                                        String fId = sub.replaceAll("[-+.^:,]","");
                                        sendFriendRequest(fId);
                                        Log.e("Selected", selectedContact.toString());
                                    }
                                    shareSetup();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else
                    Toast.makeText(this, "No contacts selected", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onContactSelected(Contact contact, String communication) {

    }

    @Override
    public void onContactDeselected(Contact contact, String communication) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
