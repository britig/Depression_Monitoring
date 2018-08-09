package com.example.briti.ecslab;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Briti on 21-Jan-18.
 */

public class DatabaseOperation {
    //Class level variables
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uid;

    public DatabaseOperation(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

    }

    /*@Descrption : This methods sets data to the database
      @Parameters : void - the values are hardcoded for now later they will be sent as a list of parametrs from the sensors
    */
    public void setDataBaseData(String lat,String lng,String acc,String x,String y,String z){
        //push the data
        final String keyOld = mDatabase.child("users").push().getKey();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        String time = simpleTimeFormat.format(calendar.getTime());
        Data data = new Data(lat,lng,acc,x,y,z);
        final Map<String, Object> posData = new HashMap<>();
        posData.put("position", data.getPos());
        posData.put("acceleration", data.getAcc());
        posData.put("x", data.getX());
        posData.put("y", data.getY());
        posData.put("z", data.getZ());
        mDatabase.child("users/" + uid + "/"+"UserData/"+date+"/"+time+"/").updateChildren(posData);
    }

    public void initializeUser(String name,String age,String gender,String weight,String height){
        UserData data = new UserData(name,age,weight,height,gender);
        final Map<String, Object> uData = new HashMap<>();
        uData.put("name", data.getName());
        uData.put("age", data.getAge());
        uData.put("weight", data.getWeight());
        uData.put("height", data.getHeight());
        uData.put("gender", data.getGender());
        mDatabase.child("users/" + uid + "/"+"UserData/UserRecord").updateChildren(uData);
    }
}