package com.example.ecodrive5;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderBoardFragment extends Fragment {
    ListView listView;
    private  View view;
    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> CarbonFootPrint = new ArrayList<>();
    MyAdapter adapter;
    String TAG ="fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_leader_board, container, false);
        listView = view.findViewById(R.id.listView);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserDatabase");

        Query query = FirebaseDatabase.getInstance().getReference("UserDatabase").orderByChild("carbonfootprint");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Log.i(TAG, "onDataChange: "+snapshot);
                    Log.i(TAG, "onDataChange: "+snapshot.child("Profile").child("fname").getValue()+snapshot.child("Profile").child("carbonfootprint").getValue());

                    Name.add(String.valueOf(snapshot.child("Profile").child("fname").getValue()));
                    CarbonFootPrint.add(String.valueOf(snapshot.child("Profile").child("carbonfootprint").getValue()));
                    adapter = new MyAdapter(getActivity(),Name,CarbonFootPrint);//images);
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        Name.clear();
        CarbonFootPrint.clear();
        // int images[]={R.drawable.f,R.drawable.s,R.drawable.index,R.drawable.images,R.drawable.images};

        return view;
    }


    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        /*String rTitle[];
        String rDescription[];
        int rImgs[];*/
        ArrayList<String> Name = new ArrayList<>();
        ArrayList<String> CarbonFootprint = new ArrayList<>();

        /*  MyAdapter(Context c,String title[],String description[]);//,int imgs[]){
              super(c,R.layout.leaderboard_row,R.id.textView1, title);
              this.context = c;
              this.rTitle = title;
              this.rDescription = description;
              this.rImgs = imgs;

          }*/
        MyAdapter(Context c, ArrayList<String> Name, ArrayList<String> CarbonFootprint){
            super(c,R.layout.leaderboard_row,R.id.textView1, Name);
            this.context = c;
            this.Name = Name;
            this.CarbonFootprint = CarbonFootprint;
            //this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.leaderboard_row,parent,false);
          //  ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            // images.setImageResource(rImgs[position]);
            myTitle.setText(Name.get(position));
            myDescription.setText(CarbonFootprint.get(position));

            return row;
        }
    }

}
