package edu.uw.tcss450.kylerr10.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.uw.tcss450.kylerr10.chatapp.ui.auth.login.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}