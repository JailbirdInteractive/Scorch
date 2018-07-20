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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jailbird.scorch.ItemFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
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

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
DatabaseReference mDatabase;
    private final List<Post> mValues;
    //private final MainActivity.OnListFragmentInteractionListener mListener;

    public CommentsAdapter(List<Post> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item, parent, false);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       String userName=mValues.get(position).author;
        String comment=mValues.get(position).body;
        String date=mValues.get(position).date;
        holder.mIdView.setText(userName);
        holder.mContentView.setText(comment);
        holder.dateView.setText(date);
       if(mValues.get(position).uid!=null) {
           DatabaseReference ref = mDatabase.child("Users").child(mValues.get(position).uid).child("Profile Image");
           ref.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   String url = (String) dataSnapshot.getValue();
                   Picasso.with(holder.placeImage.getContext()).load(url).fit().into(holder.placeImage);

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }
        // Picasso.with(holder.placeImage.getContext()).load(MainActivity.myAvatarUrl).fit().into(holder.placeImage);
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

        String stringDate = dateFormat.format(today);
        if(!mValues.get(position).date.isEmpty()) {
            //String time = get_count_of_days(mValues.get(position).date, stringDate);
           //Log.e("Date", "date " + time);
            holder.dateView.setText(printDifference(mValues.get(position).date,stringDate));
        }
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


    public void swap(List<Post> datas) {
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        CircleImageView placeImage;
        public Post mItem;
        TextView dateView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            dateView= (TextView) view.findViewById(R.id.date_text);
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (CircleImageView) view.findViewById(R.id.place_image);
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






    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public String printDifference(String Created_date_String, String Expire_date_String){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String time="";

        Date startDate = null, endDate = null, todayWithZeroTime = null;
        try {
            startDate = dateFormat.parse(Created_date_String);
            endDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
            if(elapsedDays>0){
                time=String.valueOf(elapsedDays)+" Days";
            }else if(elapsedHours>0){
                time=String.valueOf(elapsedHours)+" Hours ago ";
            }else if(elapsedMinutes>0){
                time=String.valueOf(elapsedMinutes)+" Minutes ago";
            }else if(elapsedSeconds>0){
                time= String.valueOf(elapsedSeconds)+" Seconds ago";
            }else{
                time=MyApplication.resources.getString(R.string.just_now);
            }

Log.w("TIME",time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    public String get_count_of_days(String Created_date_String, String Expire_date_String) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int c_year = 0, c_month = 0, c_day = 0;

        if (Created_convertedDate.after(todayWithZeroTime)) {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);
            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar today_cal = Calendar.getInstance();
    int today_year = today_cal.get(Calendar.YEAR);
    int today = today_cal.get(Calendar.MONTH);
    int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount + " Days");
    }
}
