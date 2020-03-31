package com.example.ecodrive5;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CalculateCarbonFootprintActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    Spinner spinnerFuel;
    EditText editTextkm;
    EditText editTextmil;
    Button buttonCalculate;
    EditText editTextCF;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_carbon_footprint);

        databaseReference = FirebaseDatabase.getInstance().getReference("details");

        mToolbar = (Toolbar) findViewById(R.id.carbo_footprint_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Carbon Footprint");

        spinnerFuel = (Spinner) findViewById(R.id.spinnerFuel);
        editTextkm = (EditText) findViewById(R.id.editTextkm);
        editTextmil = (EditText)findViewById(R.id.editTextmil);
        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        editTextCF = (EditText) findViewById(R.id.editTextCF);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inp();

            }
        });

    }

    private void inp(){
        float kilom = Float.valueOf(editTextkm.getText().toString());
        float mile= Float.valueOf(editTextmil.getText().toString());
        String fuel = spinnerFuel.getSelectedItem().toString();

        if(editTextkm!=null){
            String id = databaseReference.push().getKey();

            CalculateFootprint c = new CalculateFootprint();
            c.setUfuel( fuel );
            c.setUid( id );
            c.setUkilo( kilom );
            c.setUmil(mile);

            Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show();
            float m = 0;
            if(spinnerFuel.getItemAtPosition(1)==fuel){
                m=2392*kilom/mile;
            }
            else if(spinnerFuel.getItemAtPosition(2)==fuel){
                m=2640*kilom/mile;
            }
            else if((spinnerFuel.getItemAtPosition(3)==fuel)){
                m=2666*kilom/mile;
            }
            editTextCF.setText(""+m+"g/L");
            c.setCo2( m );
            databaseReference.child(id).setValue(c);
        }


    }
}
