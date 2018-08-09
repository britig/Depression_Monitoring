package com.example.briti.ecslab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by Briti on 15-Mar-18.
 */

public class PredictDepression extends Fragment {
    private static List<Float> x;
    private static List<Float> y;
    private static List<Float> z;
    int sitTime;
    int standTime;
    int jogTime;
    int walkTime;
    int upTime;
    int downTime;
    private TensorFlowClassifier classifier;
    private float[] results;
    private static final int N_SAMPLES = 200;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        x = new ArrayList<>();
        y = new ArrayList<>();
        z = new ArrayList<>();
        classifier = new TensorFlowClassifier(getActivity().getApplicationContext());
        Button btnFetch = (Button)getActivity().findViewById(R.id.btnFetch);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgressBar();
                load();
                hideProgressBar();
            }
        });
    }

    public void showProgressBar() {
        ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);
    }

    public void hideProgressBar() {
        ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.depressionscreening, container, false);
        return view;
    }

    //Method to load data into the data base from the excel file
    public void load(){
        try {
            BufferedReader reader  = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("data.txt")));
            String nextLine;
            String data[];
            while ((nextLine = reader.readLine()) != null) {
                    data=nextLine.split(",");
                    // nextLine[] is an array of values from the line
                    x.add(Float.parseFloat(data[1]));
                    y.add(Float.parseFloat(data[2]));
                    z.add(Float.parseFloat(data[3]));
                    Log.e("error", "data" + data[1] + data[2] + data[3]);
                    //activityPrediction();
                }

            GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, jogTime),
                    new DataPoint(1, downTime),
                    new DataPoint(2, sitTime),
                    new DataPoint(3, standTime),
                    new DataPoint(4, upTime),
                    new DataPoint(5, walkTime)
            });
            graph.addSeries(series);
            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
            // use static labels for horizontal and vertical labels
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[] {"down", "jogging", "sitting","standing","up","walking"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            // legend
            series.setTitle("Activity");
            // styling
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb((int) data.getX()*255/5, (int) Math.abs(data.getY()*255/6), 100);
                }
            });

            series.setSpacing(15);

// draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.RED);
        }
        catch(IOException e){

        }
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

    private void activityPrediction() {
        if (x.size() == N_SAMPLES && y.size() == N_SAMPLES && z.size() == N_SAMPLES) {
            List<Float> data = new ArrayList<>();
            data.addAll(x);
            data.addAll(y);
            data.addAll(z);
            results = classifier.predictProbabilities(toFloatArray(data));
            float max = results[0];
            int loc = 0;
            for(int i=1;i<results.length;i++){
                if(results[i]>max){
                    max = results[i];
                    loc = i;
                }
            }
            Log.e("error", "max" + max + loc);
            switch(loc){
                case 0:
                    downTime = downTime+1;
                    break;
                case 1:
                    jogTime = jogTime+1;
                    break;
                case 2:
                    sitTime = sitTime+1;
                    break;
                case 3:
                    standTime = standTime+1;
                    break;
                case 4:
                    upTime = upTime+1;
                    break;
                case 5:
                    walkTime = walkTime+1;
                    break;
                default:
                    break;

            }
            x.remove(0);
            y.remove(0);
            z.remove(0);
        }
    }



}
