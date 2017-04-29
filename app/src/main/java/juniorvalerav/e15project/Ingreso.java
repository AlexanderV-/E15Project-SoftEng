package juniorvalerav.e15project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class Ingreso extends AppCompatActivity {

    private EditText emailEditTextView;
    private EditText contrasenaEditTextView;
    private TextView restableceTextView;

    private LoginButton loginBoton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        callbackManager = CallbackManager.Factory.create();

        loginBoton = (LoginButton) findViewById(R.id.facebook_loginBoton);
        loginBoton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            CursosMain();
            }

            @Override
            public void onCancel() {
            Toast.makeText(getApplicationContext(),R.string.cancel_login,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(),R.string.error_login,Toast.LENGTH_SHORT).show();
            }
        });


        bindUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void bindUI(){
        emailEditTextView = (EditText) findViewById(R.id.emailIngreso);
        contrasenaEditTextView = (EditText) findViewById(R.id.passwordIngreso);
        restableceTextView = (TextView) findViewById(R.id.restablecerTextView);
    }


    //Si el login es exitoso te lleva hacia la actividad de Cursos
    public void Login(View view) {
        String email;
        String pass;
        email = emailEditTextView.getText().toString();
        pass = contrasenaEditTextView.getText().toString();
        if(logeo(email, pass)){
            CursosMain();
        }

    }
    //Redirige hacia la activadad Cursos y no permite el uso de Back del telf despues de logeado
    private void CursosMain() {
        Intent intento = new Intent(this,Cursos.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
    }

    private boolean logeo(String email, String pass){
        if(!emailValido(email)){
            Toast.makeText(this,"Email no válido, intente de nuevo",Toast.LENGTH_LONG).show();
            return false;
        }else if(!contraValido(pass)) {
            Toast.makeText(this,"Contraseña no válido, Intentar de nuevo",Toast.LENGTH_LONG).show();
            return false;
        }else
            return true;
    }

    private boolean emailValido(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean contraValido(String pass){
        return pass.length() >= 6;
    }

    public void restablecerContraseña(View view) {
        Intent intento = new Intent(this,Restablecer.class);
        startActivity(intento);
    }
}