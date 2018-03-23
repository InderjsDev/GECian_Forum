package com.inderjs.cbpgec.gecian;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.inderjs.cbpgec.gecian.R.id.forumTime;
import static com.inderjs.cbpgec.gecian.R.id.forumVote;

/**
 * Created by Inderjeet Singh on 05-Nov-17.
 */

public class ForumViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView mDate;
    ImageView mForumDetails;
    TextView cmtCount;
    TextView mGoing, mInterested, mInt, mGo;
    LinearLayout mGoingLayout, mInterestLayout;
    CardView mCardForum, mDescCard, mInterestCard;
    TextView mtimeFrom, mtimeTo, mEventDate, mEventMonth, mEventYear, mTimeToGo;



    public ForumViewHolder(View itemView) {
        super(itemView);

        mView = itemView;


        mCardForum = (CardView)mView.findViewById(R.id.cardForum);
        mForumDetails = (ImageView)mView.findViewById(R.id.forumDetails);


    }

    public void setCreatorName(String creatorName) {

        TextView creator = (TextView) mView.findViewById(R.id.forumCreatorName);
        creator.setText(creatorName);
    }

    public void setDescription(String description) {

        TextView event_desc = (TextView) mView.findViewById(R.id.forumDesc);
        event_desc.setText(description);
    }

    public void setVote(String vote) {

        TextView event_Name = (TextView) mView.findViewById(forumVote);
        event_Name.setText(vote);
    }

    public void setTime(String time) {

        TextView event_Time = (TextView) mView.findViewById(forumTime);
        event_Time.setText(time);
    }



}

