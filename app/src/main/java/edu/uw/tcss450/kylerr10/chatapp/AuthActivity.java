package edu.uw.tcss450.kylerr10.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import edu.uw.tcss450.kylerr10.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.ThemeManager;
import me.pushy.sdk.Pushy;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this); // Required to apply user's chosen theme to activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Pushy.listen(this);
        initiatePushyTokenRequest();
    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}