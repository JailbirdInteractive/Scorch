package com.jailbird.scorch;

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

import com.bumptech.glide.Glide;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;
import com.uniquestudio.library.CircleCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import static android.R.attr.data;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
private Context mContext;
    private final List<Interest> mValues;
    private List<String>urls;
    List<String> interests;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Interest> items, OnListFragmentInteractionListener listener,Context context) {
        mValues = items;
        mListener = listener;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
       urls=new ArrayList<>();
        interests=new ArrayList<>();

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).interestName);
       try {
           StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("interest images/" + mValues.get(position).interestName.trim() + ".jpg");
           storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

               @Override
               public void onSuccess(Uri uri) {
                   // Got the download URL for 'users/me/profile.png'
                   Picasso.with(mContext).load(uri).resize(130, 130).placeholder(R.drawable.blue_fire2).into(holder.mContentView);

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
       }catch(RejectedExecutionException e){

       }
       // holder.checkBox.setChecked(false);
if(PickerActivity.myInterests.contains(mValues.get(position))){
    holder.checkBox.setVisibility(View.VISIBLE);
    holder.checkBox.setChecked(true);
}else{
    holder.checkBox.setVisibility(View.GONE);
    holder.checkBox.setChecked(false);
}
/*
holder.checkBox.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
    @Override
    public void onChange(boolean checked) {
        if(PickerActivity.myInterests.contains(mValues.get(position))){
            PickerActivity.myInterests.remove(mValues.get(position));
            Log.w("Check","Interests removed "+mValues.get(position));

        }else{
            PickerActivity.myInterests.add(mValues.get(position));
            Log.w("Check","Interests added "+mValues.get(position));

        }
        holder.checkBox.setChecked(!holder.checkBox.isChecked());


        Log.w("Check","Interests "+PickerActivity.interestList.size());
    }
});
        */
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didTapButton(v);
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
                if(PickerActivity.myInterests.contains(mValues.get(position))){
                    PickerActivity.myInterests.remove(mValues.get(position));
                    Log.w("Check","Interests removed "+mValues.get(position));
                    holder.checkBox.setVisibility(View.GONE);

                }else{
                    PickerActivity.myInterests.add(mValues.get(position));
                    Log.w("Check","Interests added "+mValues.get(position));
holder.checkBox.setVisibility(View.VISIBLE);
                }

                holder.checkBox.setChecked(!holder.checkBox.isChecked());
            }
        });
    }
    public void didTapButton(View view) {
        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 20);
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
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public long getItemId(int position) {
        Object listItem = mValues.get(position);
        return listItem.hashCode();    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public void swap(List<Interest> datas){
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
