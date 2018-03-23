package com.inderjs.cbpgec.gecian;

/**
 * Created by Inderjeet Singh on 05-Nov-17.
 */

public class ForumComment {

    private String creatorUid,comment;
    private Long time;

    public ForumComment(){


    }

    public ForumComment(String creatorUid, Long time, String comment) {

        this.creatorUid = creatorUid;
        this.time = time;
        this.comment = comment;

    }


    public void setTime(Long time) {
        this.time = time;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }







    public String getCreatorUid() {
        return creatorUid;
    }

    public Long getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }



}

