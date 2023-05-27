package com.example.javapassmtdua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.jar.JarException;

public class RegisterPage extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    public static final String PASSWORD_KEY = "password_key";

    SharedPreferences sharedpreferences;
    String email, password;

    //list widget yg akan dikenakan aksi
    EditText txt_name, txt_usn, txt_email, txt_pw;
    TextView txt_signup;
    Button btnSignup;
//    ProgressBar pbLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        //konekkan semua komponen dgn xml
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_usn = (EditText) findViewById(R.id.txt_usn);
        txt_pw = (EditText) findViewById(R.id.txt_pw);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
//        pbLoading = findViewById(R.id.pbLoading);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_usn.getText().toString();
                String password = txt_pw.getText().toString();
                String name = txt_name.getText().toString();
                String email = txt_email.getText().toString();
                btnSignup.setEnabled(false);

                //hit api signup
                AndroidNetworking.post("https://mediadwi.com/api/latihan/register-user")
                        .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                        .addBodyParameter("username", username)
                        .addBodyParameter("password", password)
                        .addBodyParameter("full_name", name)
                        .addBodyParameter("email", email)
                        .setTag("POST_REQUEST")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Handle succeful response
                                Log.d("sukses login", "onResponse");
                                try {
                                    boolean status = response.getBoolean("status");
                                    String message = response.getString("message");
                                    if(status){
                                        Toast.makeText(RegisterPage.this, message, Toast.LENGTH_SHORT).show();

                                        //buat  dialog
                                        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(EMAIL_KEY, username.toString());
                                        editor.putString(PASSWORD_KEY, "");

//                                        pbLadingBar.setVisibility(View.GONE);
                                        btnSignup.setEnabled(true);
                                        Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(RegisterPage.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        });
    }
}