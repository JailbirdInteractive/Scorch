package com.jailbird.scorch;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.jailbird.scorch.EventFragment.OnListFragmentInteractionListener;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;
import com.kd.dynamic.calendar.generator.ImageGenerator;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    private final List<Object> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyEventAdapter(List<Object> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_event_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Event event = (Event) mValues.get(position);
        Date startDate = event.startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        SimpleDateFormat format = new SimpleDateFormat("MMM/dd/yyyy HH:MM:SS a");
        String dateText = format.format(startDate);
        Log.d("Date", format.format(startDate));
        holder.mItem = event;
        String placeTime = "";
        try {
            holder.dayText.setText(getDay(startDate));
            holder.monthText.setText(getMonth(startDate));
            placeTime = "" + getTime(startDate) + " | " + event.eventAddress;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //holder.monthText.setText(getMonthName(startDate.getDay()));

        Picasso.with(holder.imageView.getContext()).load(event.getImgUrl()).into(holder.imageView);
        holder.nameText.setText(event.eventName);
        holder.detailsText.setText(placeTime);
        holder.iText.setText(String.valueOf(event.getPeopleInterested()));
        holder.gText.setText(String.valueOf(event.getPeopleGoing()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        if(event.isBoosted){
        //    holder.eventCard.setCardBackgroundColor(Color.parseColor("#7f8be9"));
          //  holder.nameText.setTextColor(Color.WHITE);
            //holder.detailsText.setTextColor(Color.WHITE);

            //holder.gText.setTextColor(Color.parseColor("#53D8FB"));
            //holder.iText.setTextColor(Color.parseColor("#53D8FB"));
            holder.lit.setVisibility(View.VISIBLE);
            //mValues.set(0,event);

        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        CardView eventCard;
        public final TextView monthText;
        public final TextView dayText;
        public final TextView nameText;
        public final TextView detailsText;
        public final TextView gText,iText;
        public Event mItem;
        public ImageView imageView,lit;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            lit= (ImageView) view.findViewById(R.id.event_lit);
            iText= (TextView) view.findViewById(R.id.interested_number);
            gText= (TextView) view.findViewById(R.id.going_number);
            monthText = (TextView) view.findViewById(R.id.month_text);
            dayText = (TextView) view.findViewById(R.id.date_text);
            imageView = (ImageView) view.findViewById(R.id.place_image);
            nameText = (TextView) view.findViewById(R.id.textView3);
            detailsText = (TextView) view.findViewById(R.id.details);
            eventCard = (CardView) view.findViewById(R.id.event_card);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameText.getText() + "'";
        }
    }

    private static String getMonth(Date date) throws ParseException {
        //Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;
    }

    private static String getDay(Date date) throws ParseException {
        //Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("dd").format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;
    }

    private static String getTime(Date date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String monthName = new SimpleDateFormat("hh:mm a", Locale.US).format(cal.getTime());
        Log.d("Date", monthName);
        return monthName;

    }
    /*
    private static Bitmap getCal(Calendar cal,View view){
        Bitmap bitmap;
        ImageGenerator mImageGenerator= new ImageGenerator(view.getContext());
        mImageGenerator.setIconSize(50, 50);
        mImageGenerator.setDateSize(30);
        mImageGenerator.setMonthSize(10);

        mImageGenerator.setDatePosition(42);
        mImageGenerator.setMonthPosition(14);

        mImageGenerator.setDateColor(Color.BLACK);
        mImageGenerator.setMonthColor(R.color.colorPrimary);
        bitmap=mImageGenerator.generateDateImage(cal,R.drawable.blank_calendar);
        return bitmap;

    }
    */
}
