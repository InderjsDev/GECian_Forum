package com.inderjs.cbpgec.gecian;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Inderjeet Singh on 05-Nov-17.
 */

public class ForumCommentViewholder extends RecyclerView.ViewHolder {

    View mView;


    TextView mForumCmUser;
    CircleImageView mCmImage;



    public ForumCommentViewholder(View itemView) {
        super(itemView);

        mView = itemView;

        mForumCmUser = (TextView)mView.findViewById(R.id.forumCmUser);
        mCmImage = (CircleImageView)mView.findViewById(R.id.forumCmImage);

    }

    public void setTime(String time) {

        TextView cmTime = (TextView) mView.findViewById(R.id.forumCmTime);
        cmTime.setText(time);
    }

    public void setComment(String comment) {

        TextView cmComment = (TextView) mView.findViewById(R.id.forumCmnt);
        cmComment.setText(comment);
    }



}