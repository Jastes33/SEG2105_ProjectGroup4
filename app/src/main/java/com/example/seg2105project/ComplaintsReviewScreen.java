package com.example.seg2105project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import { getAuth, deleteUser } from "firebase/auth";
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ComplaintsReviewScreen extends AppCompatActivity {

    DatabaseReference reference;
    DatabaseReference accountRef;
    ListView complaints;
    List<Complaint> complaintsList;
    ArrayAdapter arrayAdapter;
    DatabaseReference complaintDB;
    Button insertData;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_review_screen);

        reference = FirebaseDatabase.getInstance().getReference("complaints");
        mAuth = FirebaseAuth.getInstance();
        accountRef = FirebaseDatabase.getInstance().getReference("accounts");

        complaints = findViewById(R.id.complaintsList);
        complaintsList = new ArrayList<>();
        onItemLongClick();

        insertData = findViewById(R.id.insertDataComplaint);
        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertComplaint();
            }
        });

        Button homeBtn = findViewById(R.id.homeScreenComplaints);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeScreen();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintsList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    Complaint complaint = postSnapshot.getValue(Complaint.class);
                    complaintsList.add(complaint);
                }
                ComplaintList complaintAdapter = new ComplaintList(ComplaintsReviewScreen.this, complaintsList);
                complaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onItemLongClick(){
        complaints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaintsList.get(i);

                accountRef.child(complaint.getCook()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cook userProfile = snapshot.getValue(Cook.class);
                        showComplaintDashboardDialog(userProfile.getName(), complaint.getDate().toString(), complaint.getCook(), complaint.getId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return true;
            }
        });
    }

    private void showComplaintDashboardDialog(final String cookname, final String date, final String cookID, final String complaintID){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_complaint_pop_up,null);
        dialogBuilder.setView(dialogView);

        final TextView cookName = (TextView) dialogView.findViewById(R.id.nameOfCook);
        final TextView complaintText = (TextView) dialogView.findViewById(R.id.complaintForCook);
        final TextView dateOfComplaint = (TextView) dialogView.findViewById(R.id.dateComplainedAbout);
        final TextView adminPrompt = (TextView) dialogView.findViewById(R.id.adminPrompt);
        final Button permenantlySuspend = (Button) dialogView.findViewById(R.id.permaSuspend);
        final Button temporarilySuspend = (Button) dialogView.findViewById(R.id.tempSuspend);
        final Button dismissComplaint = (Button) dialogView.findViewById(R.id.dissmissComplaint);
        final Button cancelDialogBtn = (Button) dialogView.findViewById(R.id.cancelDialogComplaint);

        cookName.setText("Cook's Name: " + cookname);
        complaintText.setText("This was issued on: "+ date);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        permenantlySuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pSuspend();
                b.dismiss();
            }
        });
        temporarilySuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        tSuspend(cookID);
                        b.dismiss();
                    }
        });
        dismissComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disCom(complaintID);
                b.dismiss();
            }
        });
        cancelDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }
    public void pSuspend(){
        Toast.makeText(ComplaintsReviewScreen.this, "Permanently Suspended Cook" , Toast.LENGTH_LONG).show();

    }
    public void tSuspend(final String cookID){
        accountRef.child(cookID).child("suspension").setValue(true);
        Date currTime = new Date();
        accountRef.child(cookID).child("suspensionTime").setValue(new Date(currTime.getTime() + 60000*1));
        Toast.makeText(ComplaintsReviewScreen.this, "Temporarily Suspended Cook" , Toast.LENGTH_LONG).show();
    }
    public void disCom(final String complaintID){
        reference.child(complaintID).removeValue();
        Toast.makeText(ComplaintsReviewScreen.this, "Complaint Dismissed" , Toast.LENGTH_LONG).show();

    }


    public void insertComplaint(){
        String summary = "This isc  the first complaint";
        String userID = "irtjttovr0c2JgXd9oHI8Pwrfk72";
        String cookID = "MN3GZyMUG7Rmz3k9oJ5e5nYgGd13";
        String id = reference.push().getKey();

        Complaint complaint = new Complaint(summary, userID, cookID, id);
        reference.child(id).setValue(complaint);
    }

    public void homeScreen(){
        Intent intent = new Intent(this, AdminLoggedInScreen.class);
        startActivity(intent);
    }

}