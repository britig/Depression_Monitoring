package com.example.briti.ecslab;

/**
 * Created by Briti on 12-Mar-18.
 */

public class SensorFilter {
    private SensorFilter() {
    }

    public static float sum(float[] array) {
        float retval = 0;
        for (int i = 0; i < array.length; i++) {
            retval += array[i];
        }
        return retval;
    }

    public static float[] cross(float[] arrayA, float[] arrayB) {
        float[] retArray = new float[3];
        retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1];
        retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2];
        retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0];
        return retArray;
    }

    public static float norm(float[] array) {
        float retval = 0;
        for (int i = 0; i < array.length; i++) {
            retval += array[i] * array[i];
        }
        return (float) Math.sqrt(retval);
    }


    public static float dot(float[] a, float[] b) {
        float retval = a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
        return retval;
    }

    public static float[] normalize(float[] a) {
        float[] retval = new float[a.length];
        float norm = norm(a);
        for (int i = 0; i < a.length; i++) {
            retval[i] = a[i] / norm;
        }
        return retval;
    }

    static void kalmanfilter(double[][][] x,int num){
        double Q[][]=identity(9,0.1);
        double R[][]=identity(3,1);
        double P[][]=identity(9,5);
        double X[]= new double[num];
        double KK[]=new double[num];
        double a_m[]=new double[num];
        double a_e[]=new double[num];

    }

    static double[][] identity(int num,double val)
    {
        int row, col;
        double mat[][] = new double[num][num];
        for (row = 0; row < num; row++)
        {
            for (col = 0; col < num; col++)
            {
                // Checking if row is equal to column
                if (row == col)
                    mat[row][col]=1;
                else
                    mat[row][col]=0;
            }
        }
        return mat;
    }
}
