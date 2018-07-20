package com.jailbird.scorch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import noman.googleplaces.Place;

/**
 * Created by adria on 2/14/2017.
 */
public class RecyclerPlaceItem extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView,peopleText;
        ImageView placeImage, bg;
        public Place mItem;

        public RecyclerPlaceItem(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (ImageView) view.findViewById(R.id.place_image);
            bg = (ImageView) view.findViewById(R.id.background_image_view);
            peopleText= (TextView) view.findViewById(R.id.peopleText);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


