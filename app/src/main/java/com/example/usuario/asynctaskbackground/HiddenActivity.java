package com.example.usuario.asynctaskbackground;

import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.usuario.asynctaskbackground.HiddenFragment.TaskCallBacks;

public class HiddenActivity extends AppCompatActivity implements TaskCallBacks {

    HiddenFragment hiddenFragment;
    private Button btnSort, btnCancel;
    private TextView txvMessage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);
        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSort = findViewById(R.id.btnSort);
        progressBar = findViewById(R.id.progressBar);

        hiddenFragment = (HiddenFragment) getSupportFragmentManager().findFragmentByTag(HiddenFragment.TAG);
        if(hiddenFragment == null) {
            hiddenFragment = new HiddenFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, hiddenFragment, HiddenFragment.TAG);
            fragmentTransaction.commit();
        } else {
            if(hiddenFragment.getProgressBarTask().getStatus() == AsyncTask.Status.RUNNING) {
                btnSort.setEnabled(false);
                btnCancel.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                btnSort.setEnabled(true);
                btnCancel.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onClickSort(View view) {
        if(hiddenFragment != null)
            hiddenFragment.startTask();
    }
    public void onCancelSort(View view) {
        if(hiddenFragment != null)
            hiddenFragment.cancelTask();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPreExecute() {
        btnSort.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        //Este caso es de un único parámetro
        if(progressBar != null)
            progressBar.setProgress(values[0]);
        if(txvMessage != null)
            txvMessage.setText(values[0] + "%");
    }

    /**
     * Método que se ejecuta cuando se cancela la tarea
     */
    @Override
    public void onCancelled() {
        btnSort.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        txvMessage.setText("Operación cancelada");
    }

    @Override
    public void onPostExecute(Void aVoid) {
        btnSort.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        txvMessage.setText("Operación terminada");
    }

}
