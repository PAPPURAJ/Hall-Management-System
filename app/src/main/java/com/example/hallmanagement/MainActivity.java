package com.example.hallmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.tabLay);
        tabLayout.setupWithViewPager(viewPager);

}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        EditText editText=new EditText(getApplicationContext());
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint("Type meal rate");

        if(item.getItemId()==R.id.mealRate){


            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Meal rate")
                    .setView(editText)
                    .setPositiveButton("Ok",(dialog, which) -> {
                        String rate=editText.getText().toString();
                        if(rate.equals("")){
                            Toast.makeText(getApplicationContext(), "Please input first", Toast.LENGTH_SHORT).show();
                        }else{
                            getSharedPreferences("MySp",MODE_PRIVATE).edit().putString("rate",rate).apply();
                            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                        }

                    }).setNegativeButton("Cancel",null)
                    .create().show();
        }

        return true;
    }
}