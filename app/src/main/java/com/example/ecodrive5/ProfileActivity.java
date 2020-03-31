package com.example.ecodrive5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    private CircleImageView ProfileImage;
    private Button choose_btn;
    private EditText textInputFname;
    private EditText textInputLname;
    private EditText textInputAdd;
    private EditText textInputDob;
    private EditText textInputLicense;
    private EditText textInputMob;
    private ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private UserDatabase PPC;
    private StorageReference mStorageRef;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("ProfileCompletion",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("ProfileNotEdited",false).apply();


        ProfileImage =findViewById(R.id.Profile_image);
        choose_btn = findViewById(R.id.choose);
        textInputFname = findViewById(R.id.first_name);
        textInputLname = findViewById(R.id.last_name);
        textInputAdd = findViewById(R.id.add);
        textInputMob = findViewById(R.id.mob);
        textInputDob = findViewById(R.id.dob);
        textInputLicense = findViewById(R.id.lno);
        progressBar = findViewById(R.id.progressBar2);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        PPC = new UserDatabase();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        /*
        myRef = database.getReference("UserDatabase").child(user.getUid()).child("ProfileUpdated");
        PPC.setProfilecompletionprocess("NotCompletedProfileProcess");
        myRef.setValue(PPC);*/

        ChoodeImageButton();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(this,"Permission denied..",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            ProfileImage.setImageURI(data.getData());
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    private void ChoodeImageButton(){
        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });
    }

    private boolean ValidateFname(){
        String fnameinput = textInputFname.getText().toString().trim();

        if(fnameinput.isEmpty()){
            textInputFname.setError("Field can't be empty");
            return false;
        }
        else {
            textInputFname.setError(null);
            return true;
        }
    }

    private boolean ValidateLname(){
        String lnameinput = textInputLname.getText().toString().trim();

        if(lnameinput.isEmpty()){
            textInputLname.setError("Field can't be empty");
            return false;
        }
        else {
            textInputLname.setError(null);
            return true;
        }
    }

    private boolean ValidateAdd(){
        String addinput = textInputAdd.getText().toString().trim();

        if(addinput.isEmpty()){
            textInputAdd.setError("Field can't be empty");
            return false;
        }else {
            textInputAdd.setError(null);
            return true;
        }
    }

    private boolean ValidateMob(){
        String mobinput = textInputMob.getText().toString().trim();

        if(mobinput.isEmpty()){
            textInputMob.setError("Field can't be empty");
            return false;
        }else if(mobinput.length() >10 ){
            textInputMob.setError("Mobile number too long");
            return false;
        }
        else {
            textInputMob.setError(null);
            return true;
        }
    }

    private boolean ValidateDob(){
        String dobinput = textInputDob.getText().toString().trim();

        if(dobinput.isEmpty()){
            textInputDob.setError("Field can't be empty");
            return false;
        }else {
            textInputDob.setError(null);
            return true;
        }
    }

    private boolean ValidateLicense(){
        String licenseinput = textInputLicense.getText().toString().trim();

        if(licenseinput.isEmpty()){
            textInputLicense.setError("Field can't be empty");
            return false;
        }else {
            textInputLicense.setError(null);
            return true;
        }
    }

    private void SetToast(String mssg){
        Toast.makeText(ProfileActivity.this, mssg, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void CreateAccount (View v) {

        try {
            if (ValidateFname() & ValidateLname() & ValidateAdd() & ValidateMob() & ValidateDob() & ValidateLicense()) {

                user = FirebaseAuth.getInstance().getCurrentUser();
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("UserDatabase").child(user.getUid()).child("Profile");

                UserDatabase ud = new UserDatabase();
                ud.setMobileno(textInputMob.getText().toString().trim());
                ud.setFname(textInputFname.getText().toString().trim());
                ud.setLname(textInputLname.getText().toString().trim());
                ud.setAddress(textInputAdd.getText().toString().trim());
                ud.setDob(textInputDob.getText().toString().trim());
                ud.setLicenceno(textInputLicense.getText().toString().trim());
                ud.setCarbonfootprint(0);
                myRef.setValue(ud);

                hideKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                Log.i("Cckk", "UpdateProfile: i am here");
                SetToast("Profile Updated Successfuly");
                sharedPreferences.edit().putBoolean("ProfileNotEdited",true).apply();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                /*myRef = database.getReference("UserDatabase").child(user.getUid()).child("ProfileUpdated");
                PPC.setProfilecompletionprocess("CompletedProfileProcess");
                myRef.setValue(PPC);*/

                startActivity(i);
                finish();
            }
            else{
                hideKeyboard();
                SetToast("Profile Not Updated");
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
        catch (Exception E) {
            Log.i("ckk", "UpdateProfile: " + E);
        }
    }

}
