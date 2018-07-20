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
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by adria on 1/17/2017.
 */

public class InterestFilterAdapter extends RecyclerView.Adapter<InterestFilterAdapter.ViewHolder> {
    private final List<Interest> mValues;
    private final InterestFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    boolean needChecked=true;
    public InterestFilterAdapter(List<Interest> items, InterestFragment.OnListFragmentInteractionListener listener,Context context) {
        mValues = items;
        mListener = listener;
        mContext=context;
    }

    @Override
    public InterestFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_interest, parent, false);


        return new InterestFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final InterestFilterAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).interestName);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("interest images/"+mValues.get(position).interestName.trim()+".jpg");
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
                Log.e("Images","error "+exception.getMessage());            }
        });
        //holder.mContentView.setImageResource(mValues.get(position).content);
        holder.checkBox.setChecked(true);

        // holder.checkBox.setChecked(false);

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
        InterestFilterAdapter.BounceInterpolator interpolator=new InterestFilterAdapter.BounceInterpolator(0.1,20);
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


