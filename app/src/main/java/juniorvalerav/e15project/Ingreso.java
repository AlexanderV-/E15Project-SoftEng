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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Ingreso extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText emailEditTextView;
    private EditText contrasenaEditTextView;
    private TextView restableceTextView;
    private TextView accesoTextView;
    private Button ingresarButton;
    private ProgressBar progressBar;


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

                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                fireBaseAuthHandler(credential);
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
            handleSignInResultGoogle(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResultGoogle(GoogleSignInResult result) {

        if (result.isSuccess()){
            AuthCredential credential = GoogleAuthProvider.getCredential(result.getSignInAccount().getIdToken(),null);
            fireBaseAuthHandler(credential);
        }else {
            Toast.makeText(getApplicationContext(),"Error de Conexion 'Google' ",Toast.LENGTH_SHORT).show();
        }
    }

    private void fireBaseAuthHandler(AuthCredential credential) {

        progressBarEffect();

        fireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Error de conexion con Google", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            }
        });
    }


    //Si el login es exitoso te lleva hacia la actividad de Cursos
    public void Login(View view) {
        String email;
        String pass;
        email = emailEditTextView.getText().toString();
        pass = contrasenaEditTextView.getText().toString();
        progressBarEffect();
        fireBaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("SIGN_IN", "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w("SIGN_IN", "signInWithEmail:failed", task.getException());
                    Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void progressBarEffect()
    {
        emailEditTextView.setVisibility(View.GONE);
        contrasenaEditTextView.setVisibility(View.GONE);
        restableceTextView.setVisibility(View.GONE);
        accesoTextView.setVisibility(View.GONE);
        ingresarButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        loginBoton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void bindUI(){
        emailEditTextView = (EditText) findViewById(R.id.emailIngreso);
        contrasenaEditTextView = (EditText) findViewById(R.id.passwordIngreso);
        restableceTextView = (TextView) findViewById(R.id.restablecerTextView);
        accesoTextView= (TextView) findViewById(R.id.acceso_rapido);
        ingresarButton= (Button) findViewById(R.id.ingresarBoton);
        progressBar = (ProgressBar) findViewById(R.id.progres_bar);
    }



    //Redirige hacia la activadad Cursos y no permite el uso de Back del telf despues de logeado
    private void CursosMain() {
        Intent intento = new Intent(this,Cursos.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
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