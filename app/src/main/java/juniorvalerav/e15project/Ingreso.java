package juniorvalerav.e15project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Ingreso extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText emailEditTextView;
    private EditText contrasenaEditTextView;
    private TextView restableceTextView;
    //Google
    private GoogleApiClient googleApiClient;

    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;

    private FirebaseAuth fireBaseAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthListener;


    //Facebook
    private LoginButton loginBoton;
    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);


        //Manejo de Sesion con Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton = (SignInButton) findViewById(R.id.google_loginBoton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

       fireBaseAuth = FirebaseAuth.getInstance();
       fireBaseAuthListener = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               FirebaseUser user = firebaseAuth.getCurrentUser();
               if (user != null){
                   CursosMain();
               }
           }
       };



        //Manejo de Sesion con Facebook
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
    protected void onStart() {
        super.onStart();

        fireBaseAuth.addAuthStateListener(fireBaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

       if(fireBaseAuthListener != null){
           fireBaseAuth.removeAuthStateListener(fireBaseAuthListener);
       }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult (result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){
            fireBaseAuthWithGoogle(result.getSignInAccount());
        }else {
            Toast.makeText(getApplicationContext(),"Error de Conexion 'Google' ",Toast.LENGTH_SHORT).show();
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        fireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error de conexion con Google", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            Toast.makeText(getApplicationContext(),"Email no válido, intente de nuevo",Toast.LENGTH_LONG).show();
            return false;
        }else if(!contraValido(pass)) {
            Toast.makeText(getApplicationContext(),"Contraseña no válido, Intentar de nuevo",Toast.LENGTH_LONG).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Error en la conexion Facebook",Toast.LENGTH_SHORT).show();
    }


}