package com.jailbird.scorch;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyInterestRecyclerViewAdapter extends RecyclerView.Adapter<MyInterestRecyclerViewAdapter.ViewHolder> {

    private final List<Interest> mValues;
    private final InterestFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
boolean needChecked=true;
    public MyInterestRecyclerViewAdapter(List<Interest> items, InterestFragment.OnListFragmentInteractionListener listener,Context context) {
        mValues = items;
        mListener = listener;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view;
       if((Activity)mContext instanceof PlaceInfoActivity){
           Log.e("Interest Activity","Activity Place");
           view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.place_interest, parent, false);
           needChecked=false;
       }else if((Activity)mContext instanceof AccountActivity){
           Log.e("Interest Activity","Activity Account");
           view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.fragment_interest, parent, false);
       } else{
           view = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.fragment_interest, parent, false);
       }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).interestName);
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("interest images/" + mValues.get(position).interestName.trim() + ".jpg");
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Picasso.with(mContext).load(uri).resize(100, 100).placeholder(R.drawable.blue_fire2).into(holder.mContentView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("Images", "error " + exception.getMessage());
                }
            });
            //holder.mContentView.setImageResource(mValues.get(position).content);
            //holder.checkBox.setChecked(true);

            // holder.checkBox.setChecked(false);
        }catch (RejectedExecutionException e){

        }
        holder.mIdView.setText(mValues.get(position).interestName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    didTapButton(v);

                }
if(holder.checkBox.isChecked()){
    holder.checkBox.setChecked(false);
}else{
    holder.checkBox.setChecked(true);
}
            }
        });
    }
    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
BounceInterpolator interpolator=new BounceInterpolator(0.1,20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }
    class BounceInterpolator implements android.view.animation.Interpolator {
        double mAmplitude = 1;
        double mFrequency = 10;

        BounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView mContentView;
        private final AnimCheckBox checkBox;
        public Interest mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_text);
            mContentView = (ImageView) view.findViewById(R.id.item_image);

               checkBox = (AnimCheckBox) view.findViewById(R.id.item_check);
               checkBox.setChecked(false);

            //mIdView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
