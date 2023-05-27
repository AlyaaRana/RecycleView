package com.example.javapassmtdua;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity {
    //Deklarasi Variabel

    // Deklarasikan konstanta untuk kode permintaan
    private static final int RC_SIGN_IN = 100;
    // Deklarasikan klien Google Sign-In
    private GoogleSignInClient mClient;
    //firebase
    private FirebaseAuth mAuth;
    private SignInButton btnSignIn;
    private boolean isGuest = false;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";

    SharedPreferences sharedpreferences;
    String email, password;

    //list widget yang akan dikenakan aksi
    EditText txtUsername, txtPassword;
    Button btnLogin;
    SignInButton btn_google;
//    ProgressBar pbLadingBar;
    TextView txt_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // konekkan semua komponen dengan xml nya
        txtUsername = (EditText) findViewById(R.id.txt_username);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//        pbLadingBar = findViewById(R.id.pbloadingbar);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        btn_google = findViewById(R.id.btn_SignInGoogle);

        //ini buat tombol sign up, buat pindah ke register
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        // and then, ini buat tombol login nya
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
//                pbLadingBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);

                //Mengirim permintaan login ke server menggunakan AndroidNetworking
                // hit api login
                AndroidNetworking.post("https://mediadwi.com/api/latihan/login")
                        .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                        .addBodyParameter("username", username)
                        .addBodyParameter("password", password)
                        .setTag("POST_REQUEST")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Menggirim respons sukses dari server
                                Log.d("sukses login", "onResponse");
                                try {
                                    boolean status = response.getBoolean("status");
                                    String message = response.getString("message");
                                    if(status){
                                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();

                                        //menyimpan email ke shared preference
                                        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(EMAIL_KEY, username.toString());
                                        editor.putString(PASSWORD_KEY, "");

//                                        pbLadingBar.setVisibility(View.GONE);
                                        btnLogin.setEnabled(true);
                                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                // Handle error
                            }
                        });

            }
        });

        //konfigurasi google sign in
        GoogleSignInOptions SignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("242652632777-spfg90h078tjtkish1afobj3bql0j0n3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mClient=GoogleSignIn.getClient(this,SignInOptions);

        //on click for tombol sign in bwaatt google
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

    }

    //memulai proses sign-in Google. Ini memperoleh intent sign-in dari instance GoogleSignInClient
    //but, idk why "startActivityForResult" it's kecoret
    private void SignIn(){
        Intent intent = mClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                HomeActivity();

            }catch(ApiException e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Itu buat membuka halaman utama
    private void HomeActivity() {
        finish();
        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}