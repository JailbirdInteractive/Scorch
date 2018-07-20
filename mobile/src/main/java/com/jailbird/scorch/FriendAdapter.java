package com.jailbird.scorch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import noman.googleplaces.Place;

/**
 * Created by adria on 2/2/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>  {
    private final List<User> mValues;
    private final MainActivity.OnListFragmentInteractionListener mListener;

    public FriendAdapter(List<User> items,MainActivity.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
holder.mIdView.setText(mValues.get(position).username);
        holder.mItem = mValues.get(position);

        Picasso.with(holder.placeImage.getContext()).load(mValues.get(position).photoUrl).placeholder(R.drawable.ic_account_circle_black_48dp).into(holder.placeImage);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(null,holder.mItem,null);
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


    public void swap(List<User> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        ImageView placeImage, bg;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (ImageView) view.findViewById(R.id.place_image);
            bg = (ImageView) view.findViewById(R.id.background_image_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
