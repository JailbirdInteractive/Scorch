package com.jailbird.scorch;

/**
 * Created by adria on 12/17/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jailbird.scorch.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.util.List;

import noman.googleplaces.Place;


/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent.DummyItem} and makes a call to the
 * specified {@link ItemFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private final List<String> mValues;
    private final MapsActivity.OnListFragmentInteractionListener mListener;
    private int count = 0;
private Context mContext;
    public PhotoAdapter(List<String> items, MapsActivity.OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (count >= mValues.size()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.no_photo_item, parent, false);
            view.setTag("ADD");

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.photo_item, parent, false);
            view.setTag(null);
        }
        count++;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      if(position<mValues.size()) {
          Picasso.with(mContext).load(mValues.get(position)).fit().into(holder.placeImage);
      }
          holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {

                    mListener.onListFragmentInteraction(holder.mView, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }




    public void swap(List<String> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        ImageView placeImage;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (ImageView) view.findViewById(R.id.place_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}
