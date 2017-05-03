package juniorvalerav.e15project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Registro extends AppCompatActivity {
    private EditText emailEditTextView;
    private EditText contrasenaEditTextView;

    private FirebaseAuth fireBaseAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();

        fireBaseAuth = FirebaseAuth.getInstance();
        fireBaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    CursosMain();
                }else {
                    Toast.makeText(getApplicationContext(), "Cuenta no pudo ser Creada", Toast.LENGTH_SHORT).show();
                }
            }
        };


        bindUI();
    }


    private void CursosMain() {
        Intent intento = new Intent(this,Cursos.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBaseAuth.addAuthStateListener(fireBaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fireBaseAuthListener != null)
            fireBaseAuth.removeAuthStateListener(fireBaseAuthListener);
    }

    private void bindUI(){
        emailEditTextView = (EditText) findViewById(R.id.emailRegistro);
        contrasenaEditTextView = (EditText) findViewById(R.id.contraseñaRegistro);
    }

    public void Registrar(View view) {
        String email;
        String pass;
        email = emailEditTextView.getText().toString();
        pass = contrasenaEditTextView.getText().toString();

        fireBaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(), "Cuenta creada con exito", Toast.LENGTH_SHORT).show();
                Log.d("SIGN UP", "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()){
                    Log.w("SIGN_UP", "createUserWithEmail:failed", task.getException());
                    Toast.makeText(getApplicationContext(), "Error de registro", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}