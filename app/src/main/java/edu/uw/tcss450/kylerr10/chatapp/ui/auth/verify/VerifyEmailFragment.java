package edu.uw.tcss450.kylerr10.chatapp.ui.auth.verify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentVerifyEmailBinding;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;
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

    private FragmentVerifyEmailBinding mBinding;

    private LoginViewModel mLoginViewModel;

    private VerifyViewModel mVerifyViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        mVerifyViewModel = new ViewModelProvider(getActivity()).get(VerifyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using the binding object
        mBinding = FragmentVerifyEmailBinding.inflate(inflater, container, false);
        // Return the root view from the binding object
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonVerify.setOnClickListener(button -> mVerifyViewModel.connect(
                mLoginViewModel.getUserEmail(),
                mBinding.editCode.getText().toString().trim()
        ));

        mBinding.buttonResend.setOnClickListener(button -> {
            Request<JSONObject> request = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:5000/verify/" + mLoginViewModel.getUserEmail(),
                    null,
                    response -> {},
                    error -> showErrorNotification("An error occurred")
            );
            RequestQueueSingleton.getInstance(getActivity().getApplication().getApplicationContext())
                    .addToRequestQueue(request);
        });

        mVerifyViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeVerifyResponse);
        mLoginViewModel.addResponseObserver(getViewLifecycleOwner(), this::observeLoginResponse);
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
                    showErrorNotification("An error occurred");
                }
            } else {
                sendLoginRequest();
            }
        } else {
            Log.d("JSON Response", "No Response");
            showErrorNotification("An error occurred");
        }
    }

    private void sendLoginRequest() {
        mLoginViewModel.connect();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to the ViewModel.
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
                    navigateToSuccess(
                            mLoginViewModel.getUserEmail(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    navigateToLogin();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            showErrorNotification("An error occurred");
        }
    }

    private void navigateToSuccess(String email, String jwt) {
        getActivity().finish();
        Navigation.findNavController(getView()).navigate(
                VerifyEmailFragmentDirections.actionVerifyEmailFragmentToHomeActivity(email, jwt)
        );
    }

    private void navigateToLogin() {
        Navigation.findNavController(getView()).navigate(
                VerifyEmailFragmentDirections.actionVerifyEmailFragmentToLoginFragment()
        );
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