package com.example.hallmanagement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    String name,TAG="===Main Fragment===";
    View view;
    ArrayList<String> arrayList=new ArrayList<>();
    TextView totalTv;
    RecyclerView recyclerView;

    public MainFragment(String name) {
        this.name=name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_main, container, false);
        totalTv=view.findViewById(R.id.totalTv);
        recyclerView=view.findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        return view;
    }



    void loadData(){
        arrayList.clear();
        FirebaseDatabase.getInstance().getReference(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(TAG,snapshot.getKey());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date))
                    arrayList.add(date);
                recyclerView.setAdapter(new RecyAdapter(getContext(),arrayList,totalTv));


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(TAG,snapshot.getValue().toString());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date))
                    arrayList.add(date);
                recyclerView.setAdapter(new RecyAdapter(getContext(),arrayList,totalTv));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}