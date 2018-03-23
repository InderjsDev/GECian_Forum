package com.inderjs.cbpgec.gecian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import de.mrapp.android.bottomsheet.BottomSheet;


public class ForumFragment extends Fragment {

    private RecyclerView mForumList;
    private FirebaseRecyclerAdapter<Forum, ForumViewHolder> firebaseRecyclerAdapter;
    private View rootView;
    private String str, itemTime;
    private AVLoadingIndicatorView mLoader;
    private CardView mEventcard;
    private String mLikes = "0", mLikedBy = "0";
    private ImageView mDatePicker;
    private Long serverTimeLong, now;
    private CardView mCardView, reportCard, mOccasionalCard;


    private DatePicker picker;
    private View shadowView;
    private Animation fab_open, fab_close, rotate_clockwise, rotate_anticlockwise, card_in, fab_out, fab_in, card_down, card_up;

    private FloatingActionButton fab;
    private String likedFeed = "Like";
    private String interestEvents = "ok";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentFireUser;
    private LinearLayoutManager mLayoutManager;
    private AlertDialogLayout mAlertDialog;
    private AlertDialog.Builder a;
    private TextView mDiscardTv, mMyDate, mCancel;
    private LinearLayout mLinearLayout;
    private Button mSubmit;
    private Fragment mLayoutMain;
    private Animation event_card_reveal;
    private String Date = "Date";
    private String inputString = "";

    private EditText forumEt;
    private Button forumBtn;
    public Button mSpinSearch;
    private String forumDescrip="";
    private Spinner mSpin;
    private String mySpinValue = "IT";
    private String addSpin = "IT";
    private Query query;


    static final int DATE_DIALOG_ID = 0;

    private int mYear, mMonth, mDay, mHour, mMinute;




    private boolean isOpen = false;

    public ForumFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);

        final View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

        //  Long due = 1497674373151L;


        shadowView = rootView.findViewById(R.id.shadowView);
        reportCard = (CardView)rootView.findViewById(R.id.reportCard);

        mForumList = (RecyclerView) rootView.findViewById(R.id.forumView);
        mForumList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mForumList.setLayoutManager(mLayoutManager);



        mLinearLayout = (LinearLayout)rootView.findViewById(R.id.forumAddLayout);

        mLinearLayout.setVisibility(View.GONE);


        mSpin = (Spinner)rootView.findViewById(R.id.forumSpinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.forum_branch_array)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpin.setAdapter(spinnerArrayAdapter);


        forumEt = (EditText)rootView.findViewById(R.id.forumEt);
        forumBtn = (Button)rootView.findViewById(R.id.addForumBtn);


        mAuth = FirebaseAuth.getInstance();

        currentFireUser = mAuth.getCurrentUser();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Forum");


        mForumList.getRecycledViewPool().clear();

        mForumList.setHasFixedSize(true);
        mForumList.setHasTransientState(true);
        mForumList.setNestedScrollingEnabled(false);
        mForumList.setItemViewCacheSize(20);
        mForumList.setDrawingCacheEnabled(true);
        mForumList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mSpinSearch = (Button)rootView.findViewById(R.id.forumSearch);

        mSpinSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySpinValue = mSpin.getSelectedItem().toString();
                addSpin = mSpin.getSelectedItem().toString();

                onDataChange(mySpinValue);

            }
        });


        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();

                forumDescrip = forumEt.getText().toString().trim();

                if(!forumDescrip.equals("")) {

                    DatabaseReference mAddRef;


                    mAddRef = FirebaseDatabase.getInstance().getReference().child("Forum").child(addSpin).push();




                    mAddRef.child("creatorName").setValue(currentFireUser.getDisplayName());
                    mAddRef.child("creatorUid").setValue(currentFireUser.getUid());
                    mAddRef.child("time").setValue(String.valueOf(now));
                    mAddRef.child("description").setValue(forumDescrip);
                    mAddRef.child("vote").setValue("0");

                    Toast.makeText(getActivity(), "New Thread created successfully", Toast.LENGTH_SHORT).show();

                    new SendNotification().execute(forumDescrip,currentFireUser.getDisplayName(),"Forum");


                    forumEt.setText("");

                    deleteExtraThread();

                }else {
                    Toast.makeText(getActivity(), "Empty Thread can't be created", Toast.LENGTH_SHORT).show();
                }

                mLinearLayout.setVisibility(View.GONE);

            }
        });


        reportCard.setVisibility(View.GONE);



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here

       inflater.inflate(R.menu.forum_menu, menu);
        final MenuItem addViewItem = menu.findItem(R.id.menu_forum_add);

        addViewItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {


            @Override
            public boolean onMenuItemClick(MenuItem item) {

              mLinearLayout.setVisibility(View.VISIBLE);

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);


    }

    public void deleteExtraThread(){

        final DatabaseReference forumRef = FirebaseDatabase.getInstance().getReference().child("Forum").child(addSpin);


        forumRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int childCount = (int) dataSnapshot.getChildrenCount();



                if(childCount>=21){

                    dataSnapshot.getChildren();

                    ArrayList<String> childKey_list = new ArrayList<>();

                    for (DataSnapshot p : dataSnapshot.getChildren()) {
                        childKey_list.clear();    //clear the arrayList everytime the firebase refresh
                        String childKey = p.getKey();
                        childKey_list.add(childKey);



                        for (int i = 0 ; i<(childCount-20);i++) {
                            String delete_childID = childKey_list.get(i);
                            forumRef.child(delete_childID).removeValue();


                        }

                        break;
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void onDataChange(String myValue) {



        firebaseRecyclerAdapter.cleanup();
        firebaseRecyclerAdapter.onAttachedToRecyclerView(mForumList);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        fireAdapter();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();

        fireAdapter();
    }


    private void fireAdapter() {


        mForumList.getRecycledViewPool().clear();

        query = mDatabaseRef;

        try {

            if (!mySpinValue.equals("")) {

                query = mDatabaseRef.child(mySpinValue);
                mySpinValue = "";
            } else {
                query = mDatabaseRef.child("IT");

                mSpinSearch.callOnClick();
            }

        }catch (Exception e){

        }

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Forum, ForumViewHolder>(

                Forum.class,
                R.layout.forum_row,
                ForumViewHolder.class,
                query


        ) {
            @Override
            protected void populateViewHolder(final ForumViewHolder viewHolder, final Forum model, int position) {

                final String post_key = getRef(position).getKey();


                viewHolder.setCreatorName(model.getCreatorName());

                viewHolder.setVote(model.getVote());

                viewHolder.setDescription(model.getDescription());




                now = System.currentTimeMillis();

                String nowString = model.getTime();

                try {
                    serverTimeLong = Long.parseLong(nowString);
                }catch (Exception e){

                }

                itemTime = (String) DateUtils.getRelativeTimeSpanString(serverTimeLong, now, 0L, DateUtils.FORMAT_ABBREV_ALL);


                viewHolder.setTime(itemTime);


                viewHolder.mForumDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());

                        builder.setDimAmount(0.6f);

                        builder.addItem(1, "Delete");
                        builder.addItem(2, "Report");



                        builder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                if(l==2) {



                                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                            .setTitle("Report Thread")
                                            .setMessage("Are you sure you want to Report this thread?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    DatabaseReference reportDataRef = FirebaseDatabase.getInstance().getReference().child("Reports").child("NewsFeedReports").push();

                                                    reportDataRef.child("ReporterId").setValue(currentFireUser.getDisplayName());
                                                    reportDataRef.child("ReporterName").setValue(currentFireUser.getUid());
                                                    reportDataRef.child("Post").setValue(post_key);
                                                    reportDataRef.child("CreatorId").setValue(model.getCreatorUid());
                                                    reportDataRef.child("CreatorName").setValue(model.getCreatorName());

                                                    reportCard.setVisibility(View.GONE);

                                                    String subject = "Report for Forum Feed";
                                                    String msg =currentFireUser.getDisplayName()+" having UID : "
                                                            +currentFireUser.getUid()+"\n"+
                                                            "just reported for a Forum Feed post of post ID : "+post_key+"\n"+
                                                            "which was created by "+model.getCreatorName()+" with UID : "+model.getCreatorUid();

                                                    new SendReportMail().execute(subject, msg);

                                                    Toast.makeText(getActivity(), "Thanks for reporting... :)", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .create();
                                    alertDialog.show();



                                }
                                if(l==1){


                                    if(model.getCreatorUid().equals(mAuth.getCurrentUser().getUid())) {
                                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                                .setTitle("Delete Thread")
                                                .setMessage("Are you sure you want to delete this thread?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        DatabaseReference deletePost = FirebaseDatabase.getInstance().getReference().child("Forum").child(addSpin).child(post_key);


                                                        deletePost.removeValue();

                                                        Toast.makeText(getActivity(), "Thread Deleted Successfully", Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .create();
                                        alertDialog.show();

                                    }else{
                                        Toast.makeText(getActivity(), "Error : This thread doesn't belongs to you.", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            }
                        });


                        BottomSheet bottomSheet = builder.create();
                        bottomSheet.show();

                    }
                });


                final DatabaseReference admRef1 = FirebaseDatabase.getInstance().getReference().child("Forum").child(addSpin).child(post_key).child("comments");

                admRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long count;

                        count = dataSnapshot.getChildrenCount();

                        if(count!=0) {

                            viewHolder.setVote("+" + String.valueOf(count));
                        }else {
                            viewHolder.setVote(String.valueOf(count));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                viewHolder.mCardForum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Intent i = new Intent(getActivity(), ForumCommentActivity.class);
                        i.putExtra("postKey", post_key);
                        i.putExtra("branch", addSpin);
                        startActivity(i);


                    }
                });




            }


        };


        mForumList.setAdapter(firebaseRecyclerAdapter);

    }





    private boolean isTooLarge (TextView text, String newText) {
        float textWidth = text.getPaint().measureText(newText);
        return (textWidth >= text.getMeasuredWidth ());
    }




}


