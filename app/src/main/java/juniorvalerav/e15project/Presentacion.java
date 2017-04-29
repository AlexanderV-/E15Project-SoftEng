package juniorvalerav.e15project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class Presentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_presentacion);

        if (AccessToken.getCurrentAccessToken() != null){
            goToCursos();
        }
    }

    private void goToCursos() {
        Intent intento = new Intent(this,Cursos.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
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


