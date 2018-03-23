package com.inderjs.cbpgec.gecian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForumCommentActivity extends AppCompatActivity {

    private RecyclerView mForumCmItemlist;
    private LinearLayoutManager mLayoutManager;
    private EditText mForumCmt;
    private Button mSend;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String msgId, chatsId="";
    private Long now, serverTime;
    private String messageTime;
    private DataSnapshot messageId, nodeId;
    private ArrayList<String> nodeIdArray;
    private String messageUser="";
    private String messageItemUser="";
    private String getUserImage="";
    private String name = "";
    private String mUid, itemUserId;
    private DatabaseReference getChatsRef;
    private DatabaseReference mDatabaseRef;
    private String post_key, mNodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Comments");
        setContentView(R.layout.activity_forum_comment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();
        post_key = extras.getString("postKey");
        mNodeRef = extras.getString("branch");

        Intent intent = getIntent();
        post_key = intent.getStringExtra("postKey");
        mNodeRef = intent.getStringExtra("branch");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUid = mUser.getUid();


        mSend = (Button)findViewById(R.id.forumCmBtn);
        mForumCmt = (EditText)findViewById(R.id.forumCmEt);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Forum").child(mNodeRef).child(post_key).child("comments");

        mForumCmItemlist = (RecyclerView)findViewById(R.id.forumCmItemList);


        mLayoutManager = new LinearLayoutManager(ForumCommentActivity.this);


        mForumCmItemlist.setLayoutManager(mLayoutManager);




        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mForumCmt.getText().toString().equals("")){

                    String comment;

                    comment = mForumCmt.getText().toString().trim();

                    DatabaseReference newCmt = mDatabaseRef.push();

                    newCmt.child("creatorUid").setValue(mUid);
                    newCmt.child("comment").setValue(comment);
                    newCmt.child("time").setValue(ServerValue.TIMESTAMP);

                    mForumCmt.setText("");

                }

            }
        });

        mForumCmItemlist.getRecycledViewPool().clear();

        mForumCmItemlist.setHasFixedSize(true);
        mForumCmItemlist.setHasTransientState(true);
        mForumCmItemlist.setNestedScrollingEnabled(false);
        mForumCmItemlist.setItemViewCacheSize(20);
        mForumCmItemlist.setDrawingCacheEnabled(true);
        mForumCmItemlist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }


    @Override
    public void onStart() {
        super.onStart();



        Query query = mDatabaseRef.orderByChild("time"); // or ...orderByChild("username");

        mForumCmItemlist.getRecycledViewPool().clear();

        final FirebaseRecyclerAdapter<ForumComment, ForumCommentViewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ForumComment, ForumCommentViewholder>(

                ForumComment.class,
                R.layout.forumcomment_row,
                ForumCommentViewholder.class,
                query




        ) {
            @Override
            protected void populateViewHolder(final ForumCommentViewholder viewHolder, final ForumComment model, int position) {



                final String post_key = getRef(position).getKey();

                viewHolder.setComment(model.getComment());

                serverTime = model.getTime();

                now = System.currentTimeMillis();


                try {

                    messageTime = String.valueOf(DateUtils.getRelativeTimeSpanString(serverTime, now, 0L, DateUtils.FORMAT_ABBREV_ALL));

                    viewHolder.setTime(messageTime);
                }catch (Exception e){

                }

                // viewHolder.setTime(model.getTime());







                final DatabaseReference getimageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getCreatorUid());

                getimageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getUserImage = "";
                        name = "";

                        if(dataSnapshot.exists()) {

                            getUserImage = dataSnapshot.child("image").getValue(String.class).toString();
                            //do what you want with the email


                            name = dataSnapshot.child("name").getValue(String.class).toString();

                            if (name.equals("")) {
                                viewHolder.mForumCmUser.setText("gecian user");
                            } else {
                                viewHolder.mForumCmUser.setText(name);
                            }

                            if (getUserImage.equals("")) {

                                viewHolder.mCmImage.setImageDrawable(getResources().getDrawable(R.drawable.user_img));


                            } else {

                                Picasso.with(getApplicationContext()).load(getUserImage).into(viewHolder.mCmImage);
                            }

                        }else {


                            if(name.equals("")){
                                viewHolder.mForumCmUser.setText("gecian user");
                            }

                            if(getUserImage.equals("")){

                                viewHolder.mCmImage.setImageDrawable(getResources().getDrawable(R.drawable.user_img));


                            }

                        }







                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });







                // data added inlcuding node






                getChatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid).child("Chats").child(post_key).child("msg");








            }


        };



        mForumCmItemlist.setAdapter(firebaseRecyclerAdapter);


    }


    @Override
    public boolean onSupportNavigateUp() {


        onBackPressed();
        return true;
    }



}




