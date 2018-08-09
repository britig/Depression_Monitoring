package com.example.briti.ecslab;

/**
 * Created by Briti on 04-Feb-18.
 */

public class UserData {
    String name;
    String age;
    String gender;
    String weight;
    String height;
    UserData(String name,String age,String weight,String height,String gender){
        this.name = name;
        this.age=age;
        this.gender=gender;
        this.weight=weight;
        this.height=height;
    }
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }
    public String getHeight(){
        return height;
    }
    public String getGender(){
        return gender;
    }

}
