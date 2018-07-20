package com.jailbird.scorch;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.NativeExpressAdView;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;

import static com.jailbird.scorch.MainActivity.isPaid;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPlaceItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PLACE_ITEM_VIEW_TYPE=0;
    private static final int AD_ITEM_VIEW_TYPE=1;

    private final List<Object> mValues;
    private final MainActivity.OnListFragmentInteractionListener mListener;
    public MyPlaceItemRecyclerViewAdapter(List<Object> items, MainActivity.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {

            case PLACE_ITEM_VIEW_TYPE:

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_placeitem, parent, false);
            return new MenuViewHolder(view);
            case AD_ITEM_VIEW_TYPE:
            default:
                View adView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.native_ad_layout, parent, false);
                return new NativeExpressAdViewHolder(adView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
       int viewType=getItemViewType(position);
        switch(viewType) {

            case PLACE_ITEM_VIEW_TYPE:
                    final MenuViewHolder holder=(MenuViewHolder)viewHolder;
            Bitmap bitmap;
                if (!(mValues.get(position) instanceof NativeExpressAdView)) {

                    final HotPlaces hotPlace = (HotPlaces) mValues.get(position);
                    if(MainActivity.boostedPlaces.contains(hotPlace.getPlace())){
                        CardView cardView= (CardView) holder.mView;
                        cardView.setCardBackgroundColor(holder.mView.getContext().getResources().getColor(R.color.colorAccent));
                        holder.lit.setVisibility(View.VISIBLE);
                        //holder.mView.setBackgroundColor(holder.mView.getContext().getResources().getColor(R.color.colorAccent));
                        holder.peopleText.setTextColor(Color.WHITE);
                        holder.mIdView.setTextColor(holder.mIdView.getResources().getColor(R.color.colorPrimary));
                        holder.mContentView.setTextColor(Color.WHITE);
                        //hotPlace.setBoosted(true);
                    }else{
                        //hotPlace.setBoosted(false);
                        CardView cardView= (CardView) holder.mView;
                        cardView.setCardBackgroundColor(Color.WHITE);
                        holder.lit.setVisibility(View.INVISIBLE);
                        //holder.mView.setBackgroundColor(holder.mView.getContext().getResources().getColor(R.color.colorAccent));
                        holder.peopleText.setTextColor(holder.peopleText.getResources().getColor(R.color.colorPrimary));
                        holder.mIdView.setTextColor(holder.mIdView.getResources().getColor(R.color.colorPrimary));
                        holder.mContentView.setTextColor(holder.mContentView.getResources().getColor(R.color.colorAccent));
                    }
                    new PhotoTask(100, 100, MapsActivity.mGoogleApiClient, hotPlace.getPlace().getPlaceId()) {
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
                                            mListener.onListFragmentInteraction(holder.mItem, null,null);
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
                                Picasso.with(holder.placeImage.getContext()).load(hotPlace.getPlace().getIcon()).into(holder.placeImage);
                                //holder.placeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (null != mListener) {
                                            // Notify the active callbacks interface (the activity, if the
                                            // fragment is attached to one) that an item has been selected.
                                            mListener.onListFragmentInteraction(holder.mItem, null,null);
                                        }
                                    }
                                });

                                //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                            }

                        }
                    }.execute(hotPlace.getPlace().getPlaceId());
                    holder.mItem = hotPlace;
                    holder.mIdView.setText(hotPlace.getPlace().getName());
                    if(hotPlace.isBoosted){

                    }
                    //holder.mContentView.setText("Nearby:" + mValues.get(position).getVicinity());

                    new AsyncTask<Void, Void, Void>() {
                        List<String> types = hotPlace.getPlace().getTypes();
                        List<String> interestList = new ArrayList<String>();

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
                            String text = interestList.toString().replace("[", "").replace("]", "");
                            holder.peopleText.setText("" + hotPlace.getCount() + " People");
                            holder.mContentView.setText(holder.mContentView.getContext().getResources().getString(R.string.interests) + text);
                        }


                    }.execute();
                }
break;
            case AD_ITEM_VIEW_TYPE:

            default:
                if ((mValues.get(position) instanceof NativeExpressAdView)) {

                    NativeExpressAdViewHolder nativeExpressAdViewHolder = (NativeExpressAdViewHolder) viewHolder;
                    Object ad = (Object) mValues.get(position);
                    NativeExpressAdView adView = (NativeExpressAdView) ad;
                    ViewGroup adCardView = (ViewGroup) nativeExpressAdViewHolder.itemView;
                    if (adCardView.getChildCount() > 0) {
                        adCardView.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    adCardView.addView(adView);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
if(!isPaid) {
    return (position % 6 == 1) ? AD_ITEM_VIEW_TYPE : PLACE_ITEM_VIEW_TYPE;
}else {
    return PLACE_ITEM_VIEW_TYPE;
}
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
public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {
    public NativeExpressAdViewHolder(View itemView) {
        super(itemView);
    }
}

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView,peopleText;
        ImageView placeImage, bg,lit;
        public HotPlaces mItem;

        public MenuViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (ImageView) view.findViewById(R.id.place_image);
            bg = (ImageView) view.findViewById(R.id.background_image_view);
            peopleText= (TextView) view.findViewById(R.id.peopleText);
            lit= (ImageView) view.findViewById(R.id.lit);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    void getInterests(final noman.googleplaces.Place place) {
        //placePhotosAsync(thisPlace.getPlaceId());
        new AsyncTask<Void, Void, Void>() {
            List<String> types = place.getTypes();
            List<Interest> interestList=new ArrayList<Interest>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                //Looper.prepare();

                for (Interest interest : MainActivity.myInterests) {
                   // Log.d("Place Info", " Place interest type " + interest.placeTypes);

                    for (int i = 0; i < types.size(); i++) {
                        //Log.d("Place Info", " Place type " + types[i]);

                        if (interest.placeTypes.contains(types.get(i))) {
                            if (!interestList.contains(interest)) {
                                interestList.add(interest);
                                //Log.d("Place Info", " Place fits Interest: " + interest.interestName);
                            }

                        } else {
                           // Log.d("Place Info", " Place fits no Interests.");

                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                for (Interest interest : interestList) {

                }
            }


        }.execute();


    }

}
