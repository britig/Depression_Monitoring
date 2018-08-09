package com.example.briti.ecslab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Briti on 07-Apr-18.
 */

public class DemoClass extends Fragment {

    private TextView phq;
    private TextView age;
    private TextView sex;
    private TextView spe;
    private TextView obesity;
    private TextView substanceuse;
    private TextView sedentary;
    private TextView mvpa;
    private TextView prediction;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.demoscreen, container, false);

        btn = (Button) view.findViewById(R.id.predict);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                phq = (TextView)getView().findViewById(R.id.phq);
                age = (TextView)getView().findViewById(R.id.age);
                sex = (TextView)getView().findViewById(R.id.sex);
                spe = (TextView)getView().findViewById(R.id.spe);
                obesity = (TextView)getView().findViewById(R.id.obesity);
                substanceuse = (TextView)getView().findViewById(R.id.substanceuse);
                sedentary = (TextView)getView().findViewById(R.id.sedentary);
                mvpa = (TextView)getView().findViewById(R.id.mvpa);

                float phqi = Float.parseFloat(phq.getText().toString());
                phqi = phqi/27;
                float agei = Float.parseFloat(age.getText().toString());
                agei = (agei-20)/9;
                float sexi = Float.parseFloat(sex.getText().toString());
                sexi = (sexi-1)/1;
                float spei = Float.parseFloat(spe.getText().toString());
                spei = (spei-1)/2;
                float obesityi = Float.parseFloat(obesity.getText().toString());
                obesityi = (obesityi-10)/50;
                float substanceusei = Float.parseFloat(substanceuse.getText().toString());
                float sedentaryi = Float.parseFloat(sedentary.getText().toString());
                sedentaryi = (sedentaryi-130)/(600-130);
                float mvpai = Float.parseFloat(mvpa.getText().toString());
                mvpai = (mvpai-10)/(470-10);

                double predict = (phqi*1.82382619)+(agei*-0.1885512)+(sexi*0.94955128)+(spei*-0.12272608)+
                        (obesityi*-1.30381572)+(substanceusei*0.16192029)+(sedentaryi*0.92028898)+(mvpai*-0.63416207);
                double result = (1/( 1 + Math.pow(Math.E,(-1*predict))));

                prediction.setText(String.valueOf(result));


            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prediction = (TextView)getView().findViewById(R.id.prediction);
    }

}
