package com.example.briti.ecslab;

/**
 * Created by Briti on 03-Mar-18.
 */
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class PredictActivityType extends Fragment implements SensorEventListener,StepListener{
    private static final int N_SAMPLES = 200;
    private static List<Float> x;
    private static List<Float> y;
    private static List<Float> z;
    private TextView downstairsTextView;
    private TextView joggingTextView;
    private TextView sittingTextView;
    private TextView standingTextView;
    private TextView upstairsTextView;
    private TextView walkingTextView;
    private float[] results;
    /*Variables for classification*/
    private TensorFlowClassifier classifier;
    /*Variables for step detection*/
    TextView TvSteps;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;
    private StepDetector simpleStepDetector;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Activity Predictor");
        x = new ArrayList<>();
        y = new ArrayList<>();
        z = new ArrayList<>();
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        TvSteps = (TextView)getView().findViewById(R.id.tv_steps);
        downstairsTextView = (TextView)getView().findViewById(R.id.downstairs_prob);
        joggingTextView = (TextView)getView().findViewById(R.id.jogging_prob);
        sittingTextView = (TextView)getView().findViewById(R.id.sitting_prob);
        standingTextView = (TextView)getView().findViewById(R.id.standing_prob);
        upstairsTextView = (TextView)getView().findViewById(R.id.upstairs_prob);
        walkingTextView = (TextView)getView().findViewById(R.id.walking_prob);

        classifier = new TensorFlowClassifier(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.prediction, container, false);
        return view;
    }

    @Override
    public void onPause() {
        getSensorManager().unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSensorManager().registerListener(this, getSensorManager().getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    private float[] toFloatArray(List<Float> list) {
        int i = 0;
        float[] array = new float[list.size()];

        for (Float f : list) {
            array[i++] = (f != null ? f : Float.NaN);
        }
        return array;
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Code for counting steps
        simpleStepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        activityPrediction();
        x.add(event.values[0]);
        y.add(event.values[1]);
        z.add(event.values[2]);
    }

    private void activityPrediction() {
        if (x.size() == N_SAMPLES && y.size() == N_SAMPLES && z.size() == N_SAMPLES) {
            List<Float> data = new ArrayList<>();
            data.addAll(x);
            data.addAll(y);
            data.addAll(z);

            results = classifier.predictProbabilities(toFloatArray(data));

            downstairsTextView.setText(Float.toString(round(results[0], 2)));
            joggingTextView.setText(Float.toString(round(results[1], 2)));
            sittingTextView.setText(Float.toString(round(results[2], 2)));
            standingTextView.setText(Float.toString(round(results[3], 2)));
            upstairsTextView.setText(Float.toString(round(results[4], 2)));
            walkingTextView.setText(Float.toString(round(results[5], 2)));

            x.clear();
            y.clear();
            z.clear();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TvSteps.setText(""+numSteps);
        Log.e("Error", "Inside method");
    }

    private SensorManager getSensorManager() {
        return (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
    }
}
