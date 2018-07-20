package com.jailbird.scorch;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HotPlacesAdapter extends RecyclerView.Adapter<HotPlacesAdapter.ViewHolder> {

    private final List<HotPlaces> mValues;
    private final MainActivity.OnListFragmentInteractionListener mListener;

    public HotPlacesAdapter(List<HotPlaces> items, MainActivity.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_placeitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Bitmap bitmap;
        new PhotoTask(100, 100, MapsActivity.mGoogleApiClient, mValues.get(position).getPlace().getPlaceId()) {
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
                    RelativeLayout.LayoutParams infoViewParams = new RelativeLayout.LayoutParams(
                            100, 100);
                    //holder.placeImage.setLayoutParams(infoViewParams);
                    Picasso.with(holder.placeImage.getContext()).load(mValues.get(position).getPlace().getIcon()).into(holder.placeImage);
                    //holder.placeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
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

                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
            }
        }.execute(mValues.get(position).getPlace().getPlaceId());
        new AsyncTask<Void, Void, Void>() {
            List<String> types = mValues.get(position).getPlace().getTypes();
            List<String> interestList=new ArrayList<String>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                //Looper.prepare();

                for (Interest interest : MapsActivity.myInterests) {
                    //Log.d("Place Info", " Place interest type " + interest.placeTypes);

                    for (int i = 0; i < types.size(); i++) {
                        //Log.d("Place Info", " Place type " + types[i]);

                        if (interest.placeTypes.contains(types.get(i))) {
                            if (!interestList.contains(interest)) {
                                interestList.add(interest.getInterestName());
                                //Log.d("Place Info", " Place fits Interest: " + interest.interestName);
                            }

                        } else {
                            //Log.d("Place Info", " Place fits no Interests.");

                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                holder.mItem = mValues.get(position).getPlace();
                holder.mIdView.setText(mValues.get(position).getPlace().getName());
if(interestList.size()>0) {
    String text = interestList.toString().replace("[", "").replace("]", "");
    holder.mContentView.setText(holder.mContentView.getContext().getResources().getString(R.string.interests) + text);
}
                holder.peopleText.setText(""+mValues.get(position).getCount()+" People");

            }


        }.execute();


        //holder.mContentView.setText("People: " + mValues.get(position).getCount());

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


    public void swap(List<HotPlaces> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView,peopleText;
        ImageView placeImage, bg,lit;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (ImageView) view.findViewById(R.id.place_image);
            bg = (ImageView) view.findViewById(R.id.background_image_view);
            lit= (ImageView) view.findViewById(R.id.lit);
            peopleText= (TextView) view.findViewById(R.id.peopleText);

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
