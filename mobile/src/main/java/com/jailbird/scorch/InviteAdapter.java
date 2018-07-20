package com.jailbird.scorch;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meetic.marypopup.MaryPopup;
import com.nineoldandroids.animation.Animator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import noman.googleplaces.Place;

import static com.jailbird.scorch.MainActivity.myID;

/**
 * Created by adria on 2/2/2017.
 */

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {
    public List<PlaceInvite> mValues;
    DatabaseReference mDatabase;
Context context;

    public InviteAdapter(List<PlaceInvite> list, Context context) {
        this.mValues = list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invite_layout, parent, false);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText("Come hang out at "+mValues.get(position).place.getName());
        holder.peopleText.setText(mValues.get(position).date);
        Picasso.with(holder.placeImage.getContext()).load(mValues.get(position).url).placeholder(R.drawable.ic_account_circle_black_48dp).into(holder.placeImage);
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity.thisPlace=mValues.get(position).place;
                view.getContext().startActivity(new Intent(view.getContext(),PlaceInfoActivity.class));
            }
        });
        if (mValues.get(position).isAccepted) {
            holder.acceptButton.setText("Accepted");
//holder.acceptButton.setClickable(false);
        }
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

replyToInvite(view,position);
                holder.acceptButton.setText("Accepted");
            }
        });
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Users").child(myID).child("Invites").child(mValues.get(position).id).removeValue();
                if(MainActivity.placeInviteList.contains(mValues.get(position))) {
                    //MainActivity.requesters.remove(mValues.get(position));

                    YoYo.with(Techniques.SlideOutLeft).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeAt(position);


                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).duration(500)
                            .playOn(holder.mView);


                }
            }
        });
            }
    public void removeAt(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }

    void declineInvite(){
        new AlertDialog.Builder(context)
                .setTitle("Decline Invite")
                .setMessage("Decline invite?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();

    }
private void replyToInvite(View view, final int position){
    View popup = LayoutInflater.from(context).inflate(R.layout.make_post_layout, null, false);

    final MaryPopup maryPopup = MaryPopup.with((Activity) context)
            .cancellable(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            .backgroundColor(Color.parseColor("#EFF4F5"))
            .content(popup)
            .draggable(true)
            .from(view)
            .center(true);

    maryPopup.show();
    final MaterialEditText editText = (MaterialEditText) popup.findViewById(R.id.post_text);
    editText.setHint("Reply");
    TextView ok = (TextView) popup.findViewById(R.id.post_button);
    ok.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String post = String.valueOf(editText.getText());
            Date date = new Date();

            Log.e("Post Date", "" + date);

            if (!post.isEmpty() && editText.isCharactersCountValid()) {
                writeNewPost(mValues.get(position).id, MainActivity.myName,"", post);

                Log.e("Post Error", "" + editText.getError() + " valid " + editText.isCharactersCountValid());
                maryPopup.close(true);
                mValues.get(position).isAccepted=true;
                mDatabase.child("Users").child(myID).child("Invites").child(mValues.get(position).id).setValue(mValues.get(position));

            }
        }
    });
}
    private void writeNewPost(String userId, String username, String title, String body) {

InviteReply reply=new InviteReply(myID,body,username,false);
        mDatabase.child("Users").child(userId).child("Invite Replies").child(myID).setValue(reply).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //refreshComments();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView, peopleText;
        ImageView  bg, lit;
        CircleImageView placeImage;
        Button acceptButton,declineButton;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            placeImage = (CircleImageView) view.findViewById(R.id.place_image);
            bg = (ImageView) view.findViewById(R.id.background_image_view);
            lit = (ImageView) view.findViewById(R.id.lit);
            peopleText = (TextView) view.findViewById(R.id.peopleText);
acceptButton= (Button) view.findViewById(R.id.accept_invite);
            declineButton= (Button) view.findViewById(R.id.decline_invite);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
