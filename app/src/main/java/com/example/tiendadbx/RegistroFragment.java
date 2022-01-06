package com.example.tiendadbx;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistroFragment extends Fragment {

    View vista;
    EditText et_correo, et_password;
    Button btn_registro;
    private FirebaseAuth mAuth;
    LoginFragment loginFragment;

    public RegistroFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        loginFragment = new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_registro, container, false);
        et_correo = vista.findViewById(R.id.et_correo);
        et_password = vista.findViewById(R.id.et_password);
        btn_registro = vista.findViewById(R.id.btn_registro);

        btn_registro.setOnClickListener( v -> validaCampos() );
        return vista;
    }

    private void validaCampos() {
        String correo = et_correo.getText().toString();
        String password = et_password.getText().toString();

        if (!correo.isEmpty() && !password.isEmpty()){
            if (password.length() >= 6){
                mAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener((Activity) vista.getContext(), task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(vista.getContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_frament, loginFragment).commit();
                    }else{
                        Toast.makeText(vista.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(vista.getContext(), "La contraseña debe ser mayor a 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(vista.getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
    }


    //import com.facebook.FacebookSdk;
    //import com.facebook.appevents.AppEventsLogger;

}