package juniorvalerav.e15project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Registro extends AppCompatActivity {
    private EditText emailEditTextView;
    private EditText contrasenaEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        bindUI();
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
        if(RegistroValido(email, pass)) {
            LoginMain();
        }
    }

    private boolean RegistroValido(String email, String pass){
        if(!emailValido(email)){
            Toast.makeText(this,"Email no válido, intente de nuevo",Toast.LENGTH_LONG).show();
            return false;
        }else if(!contraValido(pass)) {
            Toast.makeText(this,"Contraseña no válido, intente de nuevo",Toast.LENGTH_LONG).show();
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

    private void LoginMain() {
        Intent intento = new Intent(this,Ingreso.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
    }
}