package com.jailbird.scorch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import noman.googleplaces.Place;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    DatabaseReference mDatabase;
    private final List<User> mValues;
    //private final MainActivity.OnListFragmentInteractionListener mListener;

    public RequestAdapter(List<User> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_item, parent, false);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String userName=mValues.get(position).username;

        holder.mIdView.setText(userName);

        Picasso.with(holder.placeImage.getContext()).load(mValues.get(position).photoUrl).fit().into(holder.placeImage);
        holder.placeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=mValues.get(position);
                MapsActivity.currentFriend=user;
                holder.placeImage.getContext().startActivity(new Intent(holder.placeImage.getContext(),ProfileActivity.class).putExtra("userId",user.userID).putExtra("photoUrl",user.photoUrl).putExtra("userName",user.username));


            }
        });
holder.addFriend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FriendRequest request=new FriendRequest(mValues.get(position).userID,true,mValues.get(position).username);
        FriendRequest mine=new FriendRequest(MainActivity.myID,true,MainActivity.myName);
        mDatabase.child("Users").child(mValues.get(position).userID).child("Friends").child(MainActivity.myID).setValue(mine);
        mDatabase.child("Users").child(MainActivity.myID).child("Friends").child(mValues.get(position).userID).setValue(request);

        mDatabase.child("Friend Requests").child(MainActivity.myID).child(mValues.get(position).userID).removeValue();

        holder.addFriend.setText(R.string.accepted);
        if(!MainActivity.myFriends.contains(mValues.get(position)))
            MainActivity.myFriends.add(mValues.get(position));

        if(MainActivity.requesters.contains(mValues.get(position)))
        //MainActivity.requesters.remove(mValues.get(position));
            //removeAt(position);



        Toast.makeText(holder.addFriend.getContext(),""+mValues.get(position).username+" added to friends.",Toast.LENGTH_LONG).show();
    }
});
        holder.declineFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendRequest request=new FriendRequest(mValues.get(position).userID,false,userName);
                FriendRequest mine=new FriendRequest(MainActivity.myID,false,MainActivity.myName);
                mDatabase.child("Friend Requests").child(MainActivity.myID).child(mValues.get(position).userID).removeValue();
                mDatabase.child("Users").child(mValues.get(position).userID).child("Friends").child(MainActivity.myID).setValue(mine);
                mDatabase.child("Users").child(MainActivity.myID).child("Friends").child(mValues.get(position).userID).setValue(request);
                if(MainActivity.requesters.contains(mValues.get(position))) {
                    //MainActivity.requesters.remove(mValues.get(position));

                    YoYo.with(Techniques.SlideOutLeft).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeAt(position);


                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).duration(500)
                            .playOn(holder.mView);


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        Object listItem = mValues.get(position);
        return listItem.hashCode();
    }

    public void removeAt(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }

    public void swap(List<User> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        CircleImageView placeImage;
        public User mItem;
public Button addFriend;
        public Button declineFriend;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.friend_name);
            placeImage = (CircleImageView) view.findViewById(R.id.friend_image);
            addFriend= (Button) view.findViewById(R.id.add_friend);
            declineFriend= (Button) view.findViewById(R.id.decline_friend);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }




}
