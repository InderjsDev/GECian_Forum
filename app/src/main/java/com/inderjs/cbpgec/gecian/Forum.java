package com.inderjs.cbpgec.gecian;

/**
 * Created by Inderjeet Singh on 05-Nov-17.
 */

public class Forum {

    private String creatorName,creatorUid,time,description, vote;

    public Forum(){

    }

    public Forum(String creatorName, String creatorUid, String time,String description, String vote) {
        this.creatorName = creatorName;
        this.creatorUid = creatorUid;
        this.time = time;
        this.description = description;
        this.vote = vote;


    }


    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setVote(String vote){this.vote = vote;}

    public void setTime(String time){this.time = time;}







    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorUid() {
        return creatorUid;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVote(){ return  vote;}



}

