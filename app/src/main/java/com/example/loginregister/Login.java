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

public class Login extends AppCompatActivity {
    TextInputEditText Inusername,Inpassword;
    Button Login;
    TextView signup;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Inusername=findViewById(R.id.username);
        Inpassword=findViewById(R.id.password);
        Login=findViewById(R.id.buttonLogin);
        signup=findViewById(R.id.signUpText);
        progressBar=findViewById(R.id.progress);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname, username, password, email;
                username = String.valueOf(Inusername.getText());
                password = String.valueOf(Inpassword.getText());


                if ( !username.isEmpty() && !password.isEmpty()) {



                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];

                            field[0] = "username";
                            field[1] = "password";

                            //Creating array for data
                            String[] data = new String[2];

                            data[0] = username;
                            data[1]= password;

                            PutData putData = new PutData("http://192.168.1.3/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();

                                    if (result.equals("Login Success"))
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