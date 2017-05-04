package juniorvalerav.e15project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Restablecer extends AppCompatActivity {

    private EditText resetPass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        resetPass = (EditText) findViewById(R.id.restablecer_contrasena);

    }

    public void ResetPassword(View view) {
        String email;
        email = resetPass.getText().toString();
        SendResetEmail(email);
    }

    private void SendResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("RESET_PASSWORD", "resetEmailPassword:onComplete:" + task.isSuccessful());
                Toast.makeText(getApplicationContext(), "Email ha sido enviado", Toast.LENGTH_SHORT).show();
                goToLogin();
                if(!task.isSuccessful()){
                    Log.w("RESET_PASSWORD", "resetEmailPassword:failed", task.getException());
                    Toast.makeText(getApplicationContext(), "Error al restablecer contrasena", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToLogin() {
        Intent intento = new Intent(this,Ingreso.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
    }
}
