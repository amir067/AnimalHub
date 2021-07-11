package com.example.animalhub.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animalhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.animalhub.Chat.ChatDetailsActivity.KEY_FRIEND;


public class ChatFragment extends Fragment {
    private static final String TAG = "Main_ChatActivity";


    LinearLayoutManager mLayoutManager;
    public ChatsListAdapter mAdapter;

    ValueEventListener valueEventListener;
    DatabaseReference ref;

    View view;

    ParseFirebaseData pfbd;
    SettingsAPI set;

    @BindView(R.id.ry_chats)
    RecyclerView recyclerView ;

    @BindView(R.id.progressBar)
    ProgressBar progressBar  ;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_main_chat, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pfbd = new ParseFirebaseData(requireContext());
        set = new SettingsAPI(requireContext());

        // activate fragment menu
        //setHasOptionsMenu(true);



        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new ChatsListAdapter(requireContext(),new ArrayList<ChatMessage>());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(Constants.LOG_TAG, "Data changed from fragment");
                if (dataSnapshot.getValue() != null)
                    // TODO: 25-05-2017 if number of items is 0 then show something else
                    mAdapter = new ChatsListAdapter(requireContext(), pfbd.getAllLastMessages(dataSnapshot,requireContext()));
                    recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, ChatMessage obj, int position) {
                        if (obj.getFriendId().equals(set.readSetting(Constants.PREF_MY_ID)))
                            navigate( requireParentFragment(), v.findViewById(R.id.lyt_parent),  new Friend(obj.senderId,obj.senderName,obj.senderPhoto));
                        else if (obj.getSenderId().equals(set.readSetting(Constants.PREF_MY_ID)))
                            navigate( requireParentFragment(), v.findViewById(R.id.lyt_parent), new Friend(obj.friendId,obj.friendName,obj.friendPhoto));
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ref = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_CHILD);
        ref.addValueEventListener(valueEventListener);

        try {
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }

    // give preparation animation activity transition
    public void navigate(Fragment activity, View transitionImage, Friend obj) {
        Intent intent = new Intent(requireContext(), ChatDetailsActivity.class);
        intent.putExtra(KEY_FRIEND, obj);
        activity.startActivity(intent);
    }



    @Override
    public void onDestroy() {
        ref.removeEventListener(valueEventListener);
        super.onDestroy();
    }
//
//    private void initComponent() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ChatsFragment ctf = new ChatsFragment();
//        fragmentTransaction.add(R.id.container_fragment, ctf, "Chat History");
//        fragmentTransaction.commit();
//
//    }


    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(requireContext(), "Press again to exit!", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

        }
    }


}
