package edu.uw.tcss450.kylerr10.chatapp.ui.setting;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

import java.util.regex.Pattern;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentChangePasswordBinding;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * Fragment for changing the user's password.
 * @author Betelhem
 */
public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding mBinding;
    private ChangePassViewModel changePassViewModel;
    private boolean mPasswordsMatch;

    private AlertDialog mSuccessDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().setTitle("");
        }


        changePassViewModel = new ViewModelProvider(requireActivity()).get(ChangePassViewModel.class);
        UserInfoViewModel userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        changePassViewModel.setUserInfoViewModel(userInfoViewModel);

        mBinding.buttonChange.setEnabled(false);

        mBinding.editReEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = mBinding.editNewPass.getText().toString();
                String reenteredPassword = s.toString();

                mPasswordsMatch = newPassword.equals(reenteredPassword);

                if (!mPasswordsMatch) {
                    mBinding.editReEnterPassLayout.setError("Passwords do not match");
                } else {
                    mBinding.editReEnterPassLayout.setError(null);
                }
                updateChangeButtonState();
            }
        });


        mBinding.buttonChange.setOnClickListener(button -> {
            String email = mBinding.editEmail1.getText().toString();
            String oldPassword = mBinding.editOldPass.getText().toString();
            String newPassword = mBinding.editNewPass.getText().toString();

            if (!validateEmail(email)) {
                mBinding.emailLayout.setError("Invalid email");
                return;
            }

            if (!mPasswordsMatch) {
                mBinding.editReEnterPassLayout.setError("Passwords do not match");
                return;
            }

            if (checkNewPassword(newPassword)) {
                changePassViewModel.changePassword(email, oldPassword, newPassword);
            }

        });

        /**
         * Observes the response from the changePassViewModel and performs actions based on the received response.
         */
        changePassViewModel.addResponseObserver(getViewLifecycleOwner(), response -> {
            if (response.has("error")) {
                try {
                    String errorMessage = response.getString("error");
                    showErrorDialog(errorMessage); // Show error message
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorDialog("Failed to update password");
                }
            } else if (response.has("message")) {
                try {
                    String message = response.getString("message");
                    if (message.equals("Password updated successfully")) {
                        showSuccessDialog();
                    } else {
                        showErrorDialog(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorDialog("Failed to update password in correct email or old password.");
                }
            }
        });
    }

    /**
     * Updates the change button when correct info provided
     */

    private void updateChangeButtonState() {
        mBinding.buttonChange.setEnabled(mPasswordsMatch);
    }

    /**
     * Open dialog to show the success message
     */

    private void showSuccessDialog() {
        dismissSuccessDialog();
        mSuccessDialog =  new MaterialAlertDialogBuilder(requireContext())
                    .setMessage("Password updated successfully!")
                    .setPositiveButton("OK", (dialog, which) ->{
                        navigateToHome();
                    })
                    .show();
    }

    /**
     *Dismiss the success dialog if it is showing
     */
    private void dismissSuccessDialog() {

        if (mSuccessDialog != null && mSuccessDialog.isShowing()) {
            mSuccessDialog.dismiss();
            mSuccessDialog = null;
        }
    }

    /**
     * If the response contains an error, it displays the error message
     * @param errorMessage the error
     */
    private void showErrorDialog(String errorMessage) {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Validates the email
     * @param email input email
     */
    private boolean validateEmail(String email) {
        // Use a regular expression pattern to validate the email format
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    /**
     *Validate user password
     */
    private boolean checkNewPassword(String newPassword) {
        // Check password length
        boolean validLength = newPassword.length() >= 8;

        // Check for special characters
        boolean containsSpecialChar = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~-]").matcher(newPassword).find();

        // Check for whitespace
        boolean containsWhitespace = newPassword.contains(" ");

        // Check for lowercase letters
        boolean containsLowercase = Pattern.compile("[a-z]").matcher(newPassword).find();

        // Check for uppercase letters
        boolean containsUppercase = Pattern.compile("[A-Z]").matcher(newPassword).find();

        // Check for digits
        boolean containsDigit = Pattern.compile("\\d").matcher(newPassword).find();

        // Clear error messages on all TextInputLayouts
        mBinding.editNewPassLayout.setError(null);

        // Display error messages on the edit_new_pass_layout TextInputLayout
        if (!validLength) {
            mBinding.editNewPassLayout.setError("Password must be at least 8 characters long");
            return false;
        }

        if (!containsSpecialChar) {
            mBinding.editNewPassLayout.setError("Password must contain a special character");
            return false;
        }

        if (containsWhitespace) {
            mBinding.editNewPassLayout.setError("Password must not contain whitespace");
            return false;
        }

        if (!containsLowercase) {
            mBinding.editNewPassLayout.setError("Password must contain a lowercase letter");
            return false;
        }

        if (!containsUppercase) {
            mBinding.editNewPassLayout.setError("Password must contain an uppercase letter");
            return false;
        }

        if (!containsDigit) {
            mBinding.editNewPassLayout.setError("Password must contain a digit");
            return false;
        }

        // Return true if all checks pass
        return true;
    }

    private void navigateToHome() {
        Log.d("ChangePasswordFragment", "navigateToHome() called");
        Navigation.findNavController(requireView()).navigate(R.id.action_ChangePasswordFragment_to_navigation_home);
    }
    public void onResume() {
        super.onResume();
        dismissSuccessDialog();
    }

}
