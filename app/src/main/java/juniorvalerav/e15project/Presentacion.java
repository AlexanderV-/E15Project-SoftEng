package juniorvalerav.e15project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Presentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
    }

    public void registroActivity(View view) {
        Intent intento = new Intent(this,Registro.class);
        startActivity(intento);
    }

    public void ingresoActivity(View view) {
        Intent intento = new Intent(this,Ingreso.class);
        startActivity(intento);
    }

}


