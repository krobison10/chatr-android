package edu.uw.tcss450.kylerr10.chatapp.ui.auth.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentLoginBinding;
import edu.uw.tcss450.kylerr10.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.chat.ChatViewModelHelper;

/**
 * The page where the user can attempt to login to the application.
 *
 * @author Kyler Robison
 */
public class LoginFragment extends Fragment {
    private PushyTokenViewModel mPushyTokenViewModel;

    public FragmentLoginBinding mBinding;

    /**
     * Flag used to prevent the response handler from being called the first time it is set to
     * observe the response.
     */
    private boolean observerCreated = false;

    /**
     * Flag used to indicate whether a login was attempted to update errors dynamically.
     */
    private boolean loginAttempted = false;

    private LoginViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(getActivity()).get(PushyTokenViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            if(!jwt.isExpired(999999)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentLoginBinding.bind(requireView());

        mBinding.buttonToRegister.setOnClickListener(button ->
            Navigation.findNavController(getView()).navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            ));

        mBinding.buttonLogin.setEnabled(false);
        mBinding.buttonLogin.setOnClickListener(this::attemptLogin);

        mViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        //Universal handler for change of all input fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                //validateInputs() is a side-effecting method call
                mBinding.buttonLogin.setEnabled(validateInputs());
            }
        };

        mBinding.editEmail.addTextChangedListener(textWatcher);
        mBinding.editPassword.addTextChangedListener(textWatcher);


        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
        mBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Takes steps to attempt a login.
     *
     * @param button the button view from the click listener.
     */
    private void attemptLogin(final View button) {
        loginAttempted = true;
        if(validateInputs()) {
            verifyAuthWithServer();
        }
    }

    /**
     * Verifies that the values entered in the fields are valid. If the user has made an
     * attempt to log in (loginAttempted == true) then the error states of the fields will
     * be updated as well.
     * @return true if all inputs are valid and a login can be attempted
     */
    private boolean validateInputs() {
        String email = mBinding.editEmail.getText().toString();
        boolean emailValid = email.contains("@") && email.contains(".");
        if(!emailValid) {
            mBinding.emailLayout.setError(getString(R.string.error_email));
        }
        mBinding.emailLayout.setErrorEnabled(loginAttempted && !emailValid);

        String password = mBinding.editPassword.getText().toString();
        boolean passwordValid = password.length() >= 8;
        if(!passwordValid) {
            mBinding.passwordLayout.setError(getString(R.string.error_password));
        }
        mBinding.passwordLayout.setErrorEnabled(loginAttempted && !passwordValid);

        return emailValid && passwordValid;
    }

    /**
     * Makes an async call to the API for login using the current values of the input fields.
     */
    private void verifyAuthWithServer() {
        mViewModel.setUserEmail(mBinding.editEmail.getText().toString());
        mViewModel.setUserPassword(mBinding.editPassword.getText().toString());
        mViewModel.connect();
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     * @param email users email
     * @param jwt the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        if(mBinding.switchStayLogged.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }
        Navigation.findNavController(getView()).navigate(LoginFragmentDirections
                .actionLoginFragmentToHomeActivity(email, jwt));
        getActivity().finish();
    }
    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken(String token, String jwt) {
        mPushyTokenViewModel.sendTokenToWebservice(token, jwt);
    }

    /**
     * Helper to abstract the navigation to the verify fragment.
     */
    private void navigateToVerify() {
        boolean stayLogged = mBinding.switchStayLogged.isChecked();
        Navigation.findNavController(getView()).navigate(LoginFragmentDirections
                .actionLoginFragmentToVerifyEmailFragment(stayLogged));
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to the ViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if(!observerCreated) { //Prevents method from executing just because the observer is added
            observerCreated = true;
            return;
        }
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if(response.getInt("code") == 300) {
                        navigateToVerify();
                    }
                    else {
                        String message = response.getJSONObject("data").getString("message");
                        mBinding.emailLayout.setError("Error: " + message);
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    showErrorNotification("An error occurred");
                }
            } else {
                try {
                    String token = response.getString("token");
                    String jwt = response.getString("token");
                    Log.d("Pushy Token222", token);

// Retrieve the Pushy device token
                    mPushyTokenViewModel.retrieveToken(new PushyTokenViewModel.PushyTokenCallback() {
                        @Override
                        public void onTokenReceived(String token) {
                            // Handle the retrieved Pushy device token
                            // You can now use the token to send push notifications from the server-side
                            Log.d("Pushy Token222", token);
                            Log.d("Pushy Token222", jwt);

                            // Send the token to the web service along with the JWT
                            sendPushyToken(token, jwt);
                        }

                        @Override
                        public void onTokenError(String errorMessage) {
                            // Token retrieval failed, handle the error
                            Log.d("Pushy Token222", "Token retrieval failed: " + errorMessage);
                        }
                    });

                    // Navigate to the next screen or perform any required action
                    navigateToSuccess(
                            mBinding.editEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    showErrorNotification("An error occurred");
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            showErrorNotification("An error occurred");
        }
    }

    /**
     * Displays an error notification to the user.
     *
     * @param message message to show.
     */
    private void showErrorNotification(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }
}