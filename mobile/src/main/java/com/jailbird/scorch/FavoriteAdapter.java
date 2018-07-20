package com.jailbird.scorch;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import noman.googleplaces.Place;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<Place> mValues;
    private final MainActivity.OnListFragmentInteractionListener mListener;

    public FavoriteAdapter(List<Place> items, MainActivity.OnListFragmentInteractionListener listener) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Bitmap bitmap;
        new PhotoTask(70, 70, MapsActivity.mGoogleApiClient, mValues.get(position).getPlaceId()) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);

            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {

                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    //  mImageView.setImageBitmap(attributedPhoto.bitmap);
                    holder.placeImage.setImageBitmap(attributedPhoto.bitmap);
                    holder.bg.setVisibility(View.INVISIBLE);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    // Display the attribution as HTML content if set.


                } else {
                    Log.e("Photo", "no photo");

                    Picasso.with(holder.placeImage.getContext()).load(mValues.get(position).getIcon()).fit().into(holder.placeImage);
                    //holder.placeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
            }
        }.execute(mValues.get(position).getPlaceId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(null,null,holder.mItem);
                }
            }
        });
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        //holder.mContentView.setText("Nearby:" + mValues.get(position).getVicinity());

    }

    @Override
    public int getItemCount() {
       if(mValues!=null) {
           return mValues.size();
       }else {
           return 0;
       }

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


    public void swap(List<Place> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        ImageView placeImage, bg;
        public Place mItem;

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

    private void placePhotosAsync(String id) {
        new PhotoTask(100, 100, MapsActivity.mGoogleApiClient, id) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    //  mImageView.setImageBitmap(attributedPhoto.bitmap);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        //    mText.setVisibility(View.GONE);
                    } else {
                        //  mText.setVisibility(View.VISIBLE);
                        //mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                } else {
                    Log.e("Photo", "no photo");
                    //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nightsml);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
            }
        }.execute(id);
    }

}
