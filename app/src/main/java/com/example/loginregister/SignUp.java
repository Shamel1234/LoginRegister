package com.example.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {
TextInputEditText Infullname,Inusername,Inpassword,Inemail;
Button signup;
TextView Login;

ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Infullname=findViewById(R.id.fullname);
        Inusername=findViewById(R.id.username);
        Inpassword=findViewById(R.id.password);
        Inemail=findViewById(R.id.email);
        signup=findViewById(R.id.buttonSignUp);
        Login=findViewById(R.id.loginText);
        progressBar=findViewById(R.id.progress);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname, username, password, email;
                fullname = String.valueOf(Infullname.getText());
                username = String.valueOf(Inusername.getText());
                password = String.valueOf(Inpassword.getText());
                email = String.valueOf(Inemail.getText());

                if (!fullname.isEmpty() && !username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {



                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[4];
                        field[0] = "fullname";
                        field[1] = "username";
                        field[2] = "password";
                        field[3] = "email";
                        //Creating array for data
                        String[] data = new String[4];
                        data[0] = fullname;
                        data[1] = username;
                        data[2]= password;
                        data[3]= email;
                        PutData putData = new PutData("http://192.168.1.3/LoginRegister/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                progressBar.setVisibility(View.GONE);
                                String result = putData.getResult();

                                if (result.equals("Sign Up Success"))
                                {
                                    saveUsernameToCache(username);
                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }
                else {
                    Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void saveUsernameToCache(String username) {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .apply();
    }

}