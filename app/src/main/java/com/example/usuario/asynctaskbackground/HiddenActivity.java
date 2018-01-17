package com.example.usuario.asynctaskbackground;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.usuario.asynctaskbackground.HiddenFragment.TaskCallBacks;

public class HiddenActivity extends AppCompatActivity implements TaskCallBacks {

    HiddenFragment hiddenFragment;
    private Button btnSort, btnCancel;
    private TextView txvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);

        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSort = findViewById(R.id.btnSort);
    }

    public void onClickSort(View view) {
        hiddenFragment = new HiddenFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(android.R.id.content, hiddenFragment, HiddenFragment.TAG);
        fragmentTransaction.commit();
    }
    public void onCancelSort(View view) {
        hiddenFragment.cancelTask();
    }


    @Override
    public void onPreExecute() {
        btnCancel.setVisibility(View.VISIBLE);
        btnSort.setEnabled(false);
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        //Este caso es de un único parámetro
        txvMessage.setText(values[0] + "%");
    }

    /**
     * Método que se ejecuta cuando se cancela la tarea
     */
    @Override
    public void onCancelled() {
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort.setEnabled(true);
        txvMessage.setText("Operación cancelada");
    }

    @Override
    public void onPostExecute(Void aVoid) {
        btnCancel.setVisibility(View.INVISIBLE);
        btnSort.setEnabled(true);
        txvMessage.setText("Operación terminada");
    }

}
