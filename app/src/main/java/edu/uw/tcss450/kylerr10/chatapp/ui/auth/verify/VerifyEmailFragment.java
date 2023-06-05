package edu.uw.tcss450.kylerr10.chatapp.ui.auth.verify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentVerifyEmailBinding;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.kylerr10.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.auth.login.LoginViewModel;

/**
 * Fragment for user email verification.
 *
 * @author Kyler Robison
 */
public class VerifyEmailFragment extends Fragment {
    /**
     * Flag used to prevent the response handler from being called the first time it is set to
     * observe the response.
     */
    private boolean verifyObserverCreated = false;

    /**
     * Flag used to prevent the response handler from being called the first time it is set to
     * observe the response.
     */
    private boolean loginObserverCreated = false;

    /**
     * Indicates whether to stay logged in upon successful login
     */
    private boolean stayLogged;

    private FragmentVerifyEmailBinding mBinding;

    private UserInfoViewModel mUserInfoViewModel;

    private PushyTokenViewModel mPushyTokenViewModel;

    private LoginViewModel mLoginViewModel;

    private VerifyViewModel mVerifyViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mVerifyViewModel = new ViewModelProvider(requireActivity()).get(VerifyViewModel.class);
        mPushyTokenViewModel = new ViewModelProvider(requireActivity()).get(PushyTokenViewModel.class);
        mUserInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);

        assert getArguments() != null;
        VerifyEmailFragmentArgs args = VerifyEmailFragmentArgs.fromBundle(getArguments());
        stayLogged = args.getStayLogged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentVerifyEmailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonVerify.setOnClickListener(button -> mVerifyViewModel.connect(
                mLoginViewModel.getUserEmail(),
                Objects.requireNonNull(mBinding.editCode.getText()).toString().trim()
        ));

        // Send simple request for sending a code
        mBinding.buttonResend.setOnClickListener(button -> {
            Request<JSONObject> request = new JsonObjectRequest(
                    Request.Method.POST,
                    BuildConfig.BASE_URL + "/verify/" + mLoginViewModel.getUserEmail(),
                    null,
                    response -> {},
                    error -> showErrorNotification()
            );
            RequestQueueSingleton.getInstance(requireActivity().getApplication().getApplicationContext())
                    .addToRequestQueue(request);
        });

        TextWatcher enterCodeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.codeLayout.setErrorEnabled(false);
            }
        };

        mBinding.editCode.addTextChangedListener(enterCodeWatcher);

        mVerifyViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeVerifyResponse);
        mLoginViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeLoginResponse);
        mPushyTokenViewModel.addResponseObserver(getViewLifecycleOwner(), this::observePushyPutResponse);
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to the ViewModel.
     *
     * @param response the Response from the server
     */
    private void observeVerifyResponse(final JSONObject response) {
        if(!verifyObserverCreated) { //Prevents method from executing just because the observer is added
            verifyObserverCreated = true;
            return;
        }
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    String message = response.getJSONObject("data").getString("message");
                    mBinding.codeLayout.setError("Error: " + message);
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    showErrorNotification();
                }
            } else if (response.has("success")) {
                sendLoginRequest();
            }
            else {
                showErrorNotification();
            }
        } else {
            Log.d("JSON Response", "No Response");
            showErrorNotification();
        }
    }

    /**
     * Sends an http request to log in the user
     */
    private void sendLoginRequest() {
        mLoginViewModel.connect();
    }

    /**
     * An observer for the login response from the server.
     *
     * @param response the Response from the server
     */
    private void observeLoginResponse(final JSONObject response) {
        if(!loginObserverCreated) { //Prevents method from executing just because the observer is added
            loginObserverCreated = true;
            return;
        }
        if (response.length() > 0) {
            if (response.has("code")) {
                navigateToLogin();
            } else {
                try {
                    mUserInfoViewModel.setToken(new JWT(response.getString("token")));
                    sendPushyToken();
                } catch (JSONException e) {
                    navigateToLogin();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            showErrorNotification();
        }
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserInfoViewModel.getJWT().toString());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                navigateToLogin();
            } else {
                navigateToSuccess(
                        mLoginViewModel.getUserEmail(),
                        mUserInfoViewModel.getJWT().toString()
                );
            }
        }
    }

    /**
     * Navigates to the main activity.
     * @param email the user's email
     * @param jwt the jwt obtained upon login
     */
    private void navigateToSuccess(String email, String jwt) {
        if(stayLogged) {
            SharedPreferences prefs =
                    requireActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }
        Navigation.findNavController(requireView()).navigate(
                VerifyEmailFragmentDirections.actionVerifyEmailFragmentToHomeActivity(email, jwt)
        );
        requireActivity().finish();
    }

    /**
     * Navigates back to the login fragment
     */
    private void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(
                VerifyEmailFragmentDirections.actionVerifyEmailFragmentToLoginFragment()
        );
    }

    /**
     * Displays an error notification to the user.
     */
    private void showErrorNotification() {
        Snackbar.make(requireView(), "An error occurred", Snackbar.LENGTH_SHORT).show();
    }
}