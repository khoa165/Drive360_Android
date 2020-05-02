package com.example.drive360_android.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.drive360_android.R;
import com.example.drive360_android.forms.AddDiscussionActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.drive360_android.Config.classroomsRef;
import static com.example.drive360_android.Config.discussionsRef;

public class DiscussionFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private View view;
    private SharedPreferences sharedPreferences;
    private List<String> discussionThreads;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discussion, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListView();
        setupFloatingButtonOnClick();
    }

    private void setupFloatingButtonOnClick() {
        FloatingActionButton addDiscussionButton = (FloatingActionButton) getActivity().findViewById(R.id.floatingAddButton);
        addDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDiscussionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupListView() {
        discussionThreads = new ArrayList<String>();
        String classroomId = sharedPreferences.getString("classroomId", "");

        discussionsRef.child(classroomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
//                        discussionThreads.add(d.getKey());
                    }

                    ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, discussionThreads);
                    setListAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}
