package com.example.usuario.asynctaskbackground;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 100;
    private int[] numbers = new int[MAX_LENGTH];
    private TextView txvMessage;
    private Button btnSort, btnCancel;
    private SimpleAsyncTask simpleAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvMessage = findViewById(R.id.txvMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnSort = findViewById(R.id.btnSort);
    }

    private void generateNumbers() {
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = (int) Math.floor(Math.random() * MAX_LENGTH);
    }

    /**
     * Método que se ejecuta cuando se pulsa el botón ordenar
     * @param view
     */
    public void onClickSort(View view) {
        //OPCION 1: Forzando el error ANR
        //bubbleSort(numbers);
        //txvMessage.setText("Operación terminada");

        //Opción 2: Con hilos para bubbleSort y mensaje
        //execWithThread();

        //Opción 3: Con AsyncTask
        simpleAsyncTask = new SimpleAsyncTask();
        simpleAsyncTask.execute();
    }
    public void onCancelSort(View view) {
        //Opción 3: Con AsyncTask
        simpleAsyncTask.cancel(true);
    }



    private void execWithThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleSort();
                //Opcion 2.1: Esta es la única forma de tener acceso a la vista
                //Esta opción no es limpia
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txvMessage.setText("Operación terminada");
                        Toast.makeText(MainActivity.this, "Fin", Toast.LENGTH_SHORT).show();
                    }
                });
                //Opcion 2.2:
                //txvMessage.setText("Operación terminada");
            }
        }).start();
    }

    /**
     * Método que ordena un array mediante el algoritmo de la burbuja
     */
    private void bubbleSort() {
        for (int i = 0; i < numbers.length - 1; i++)
            for (int j = i + 1; j < numbers.length - 1; j++)
                if(numbers[i] > numbers[j]) {
                    int aux = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = aux;
                }
    }

    /**
     * Primer parámetro: Parámetro de entrada de doInBackground.
     * Segundo parámetro: Parámetro de entrada de publishProgress.
     * Tercer parámetro: Parámetro de entrada de onPostExecute.
     */
    private class SimpleAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnCancel.setVisibility(View.VISIBLE);
            btnSort.setEnabled(false);
            generateNumbers();
        }

        /**
         * Es el único método que se ejecuta en el hilo principal.
         * La comunicación con la interfaz gráfica se ejecuta con publishProgress.
         * En el método principal se va a ejecutar onProgressUpdate.
         * En la interfaz gráfica se ejecutan todos menos doInBackground
         * (onPreExecute, onProgressUpdate, publishProgress, onPostExecute, onCancelled).
         * @param
         * @return
         */
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Este caso es de un único parámetro
            txvMessage.setText(values[0] + "%");
        }

        /**
         * Método que se ejecuta cuando se cancela la tarea
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            btnCancel.setVisibility(View.INVISIBLE);
            btnSort.setEnabled(true);
            txvMessage.setText("Operación cancelada");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btnCancel.setVisibility(View.INVISIBLE);
            btnSort.setEnabled(true);
            //txvMessage.setText("Operación terminada");
        }
    }

}
