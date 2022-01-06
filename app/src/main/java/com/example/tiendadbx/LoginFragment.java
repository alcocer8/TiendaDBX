package com.example.tiendadbx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;
    private CallbackManager callbackManager;
    private static final String GOOGLE_ID_CLIENT_TOKEN = "1097651277272-98cnvh1n0ecirmimbh68hoemlb2brvo4.apps.googleusercontent.com";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    EditText et_correo, et_password;
    Button btn_login, btn_google;
    LoginButton btn_facebook;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        signGoogle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login, container, false);

        et_correo = vista.findViewById(R.id.et_correo);
        et_password = vista.findViewById(R.id.et_password);
        btn_login = vista.findViewById(R.id.btn_ingresa);
        btn_google = vista.findViewById(R.id.btn_google);
        btn_facebook = vista.findViewById(R.id.btn_facebook);


        btn_login.setOnClickListener( v -> validaCampos());
        btn_google.setOnClickListener( v -> signIn());
        btn_facebook.setOnClickListener( v -> signInFacebook());
        return vista;
    }

    private void signInFacebook() {
        callbackManager = CallbackManager.Factory.create();
        btn_facebook.setReadPermissions("email");
        btn_facebook.setFragment(this);
        btn_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LogFB","Pasas1");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("LogFB","Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LogFB","Error");
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i("LogFB","Pasas");

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("LogFB","Pasas2");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }



    private void signGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_ID_CLIENT_TOKEN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {}
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(), "Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void validaCampos(){
        if (permissionInternet()){
            return;
        }

        String correo = et_correo.getText().toString();
        String password = et_password.getText().toString();

        if (!correo.isEmpty() && !password.isEmpty()){
            if (password.length() >= 6){
                mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(  getActivity(), task -> {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(), "Error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(getContext(), "Tu password debe ser mayor a 6", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean permissionInternet() {
        Log.i("Permisso", "Obteniendo permiso");
        int permission = getActivity().checkSelfPermission(Manifest.permission.INTERNET);
        if (permission == PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1001);
            Log.i("Permisso", "Permiso dado");
            return false;
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length> 0 && requestCode == 1001){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("Permisso", "Permiso dado");}
        }
    }

}