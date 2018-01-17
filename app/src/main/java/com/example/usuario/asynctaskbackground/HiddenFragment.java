package com.example.usuario.asynctaskbackground;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


public class HiddenFragment extends Fragment {

    public static final String TAG = "HiddenFragment";
    private static final int MAX_LENGTH = 1000;
    private int[] numbers = new int[MAX_LENGTH];
    private TaskCallBacks callback;
    private ProgressBarTask progressBarTask;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (TaskCallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement TaskCallBacks");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //Se inicia la tarea
        progressBarTask = new ProgressBarTask();
        progressBarTask.execute();
    }
    public void cancelTask(){
        progressBarTask.cancel(true);
    }


    public class ProgressBarTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            generateNumbers();
            if(callback != null)
                callback.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Si no se cancela la operación se actualizará la barra de progreso
            int maxMovimientos = (numbers.length * (numbers.length - 1)) / 2;
            int movimientos = 1;
            for (int i = 0; i < numbers.length; i++)
                for (int j = 1; j < (numbers.length - i); j++) {
                    publishProgress((int) ((100 * movimientos++)/ (float) maxMovimientos));
                    if (numbers[j - 1] > numbers[j]) {
                        int aux = numbers[j - 1];
                        numbers[j - 1] = numbers[j];
                        numbers[j] = aux;
                    }
                    if (isCancelled())
                        return null;
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(callback != null)
                callback.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(callback != null)
                callback.onProgressUpdate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(callback != null)
                callback.onCancelled();
        }

        private void generateNumbers() {
            for (int i = 0; i < numbers.length; i++)
                numbers[i] = (int) Math.floor(Math.random() * MAX_LENGTH);
        }

    }

    interface TaskCallBacks {
        void onPreExecute();
        void onProgressUpdate(Integer... values);
        void onCancelled();
        void onPostExecute(Void avoid);
    }

}
