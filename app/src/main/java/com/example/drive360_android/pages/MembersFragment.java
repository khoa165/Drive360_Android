package com.example.drive360_android.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.drive360_android.R;
import com.example.drive360_android.models.Invitation;
import com.example.drive360_android.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.drive360_android.Config.classroomsRef;
import static com.example.drive360_android.Config.invitationsRef;
import static com.example.drive360_android.Config.usersRef;

public class MembersFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private View view;
    private SharedPreferences sharedPreferences;
    private DatabaseReference singleUserInvitationRef;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_members, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("com.example.drive360_android", Context.MODE_PRIVATE);

        setupInviteButtonListener();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<String> random = new ArrayList<String>();
        random.add("dog");
        random.add("cat");
        random.add("bird");

        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, random);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }

    public void setupInviteButtonListener() {
        Button button = (Button) view.findViewById(R.id.inviteUserButton);
        EditText usernameInput = (EditText) view.findViewById(R.id.usernameInput);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instructor = sharedPreferences.getString("username", "");
                String learner = usernameInput.getText().toString();

                if (instructor != null && !instructor.equals("") && learner != null && !learner.equals("")) {
                    checkUsernameExists(instructor, learner);
                }
            }
        });
    }

    public void checkUsernameExists(String instructor, String learner) {
        usersRef.child(learner).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if username already exists.
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.role.equals("Learner")) {
                        inviteUser(instructor, learner);
                    } else {
                        Toast.makeText(getActivity(), "User is not a learner, sorry!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Username does not exist. Please try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void inviteUser(String instructor, String learner) {
        String username = sharedPreferences.getString("username", "");
        String classroomId = sharedPreferences.getString("classroomId", "");
        singleUserInvitationRef = invitationsRef.child(username);

        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Invitation invitation = new Invitation(instructor, learner, now, classroomId);

        // Generate id;
        String id = singleUserInvitationRef.push().getKey();
        // Send data to invitations branch on Firebase.
        singleUserInvitationRef.child(id).setValue(invitation);

        addToClassroom(invitation);
    }

    private void addToClassroom(Invitation invitation) {
        String classroomId = sharedPreferences.getString("classroomId", "");
        if (classroomId.equals("")) {
            goToClassroomDashboardScreen();
        }

        String instructor = invitation.instructor;
        String learner = invitation.learner;
        classroomsRef.child(instructor).child(classroomId).child("learners").child(learner).setValue(true);
        Toast.makeText(getActivity(), learner + " was successfully added to the class!", Toast.LENGTH_LONG).show();
    }

    // Transition to classroom dashboard screen.
    public void goToClassroomDashboardScreen() {
        Intent intent = new Intent(getActivity(), ClassroomDashboardActivity.class);
        startActivity(intent);
    }
}
