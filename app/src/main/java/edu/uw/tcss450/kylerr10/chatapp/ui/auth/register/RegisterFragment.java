package edu.uw.tcss450.kylerr10.chatapp.ui.auth.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentRegisterBinding;

/**
 * @author Kyler Robison, Betlhem Bada
 */
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding mBinding;

    private RegisterViewModel mViewModel;

    /**
     * Flag representing whether the user has attempted to continue or not.
     * If so, dynamic updating of the error state of the input fields will begin.
     */
    private boolean continueAttempted = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegisterBinding.inflate(inflater);
        return  mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonNext.setOnClickListener(button -> {
            continueAttempted = true;
            boolean valid = validateInputs();

            if(valid) {
                //Store current values in viewModel for use in the next step
                mViewModel.setUserFName(mBinding.editFirst.getText().toString());
                mViewModel.setUserLName(mBinding.editLast.getText().toString());
                mViewModel.setUserEmail(mBinding.editEmail.getText().toString());
                mViewModel.setUserUsername(mBinding.editUsername.getText().toString());

                Navigation.findNavController(getView())
                        .navigate(R.id.action_registerFragment_to_registerPasswordFragment);
            }
        });

        //Universal handler for change of all input fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                validateInputs();
            }
        };

        mBinding.editFirst.addTextChangedListener(textWatcher);
        mBinding.editLast.addTextChangedListener(textWatcher);
        mBinding.editEmail.addTextChangedListener(textWatcher);
        mBinding.editUsername.addTextChangedListener(textWatcher);

        mBinding.buttonToLogin.setOnClickListener(button -> {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }

    /**
     * Helper method to update the validity of the input fields based on current values.
     *
     * @return true if all input fields are valid.
     */
    private boolean validateInputs() {
        // TODO: consider updating logic to check if email or username is taken before continuing is attempted

        if(!continueAttempted) return false;

        boolean valid = true;

        if(mBinding.editFirst.getText().toString().length() == 0) {
            mBinding.editFirstLayout.setErrorEnabled(true);
            mBinding.editFirstLayout.setError("Please enter a valid first name");
            valid = false;
        } else {
            mBinding.editFirstLayout.setErrorEnabled(false);
        }

        if(mBinding.editLast.getText().toString().length() == 0) {
            mBinding.editLastLayout.setErrorEnabled(true);
            mBinding.editLastLayout.setError("Please enter a valid last name");
            valid = false;
        } else {
            mBinding.editLastLayout.setErrorEnabled(false);
        }

        if(mBinding.editUsername.getText().toString().length() == 0) {
            mBinding.editUsernameLayout.setErrorEnabled(true);
            mBinding.editUsernameLayout.setError("Please enter a valid username");
            valid = false;
        } else {
            if(usernameExists(mBinding.editUsername.getText().toString())) {
                mBinding.editUsernameLayout.setErrorEnabled(true);
                mBinding.editUsernameLayout.setError("Username is taken");
                valid = false;
            } else {
                mBinding.editUsernameLayout.setErrorEnabled(false);
            }
        }

        String email = mBinding.editEmail.getText().toString();
        if(!(email.contains("@") && email.contains("."))) {
            mBinding.editEmailLayout.setErrorEnabled(true);
            mBinding.editEmailLayout.setError("Please enter a valid email");
            valid = false;
        } else {
            if(emailExists(email)) {
                mBinding.editEmailLayout.setErrorEnabled(true);
                mBinding.editEmailLayout.setError("Account already exists");
                valid = false;
            } else {
                mBinding.editEmailLayout.setErrorEnabled(false);
            }
        }

        return valid;
    }

    /**
     * @return true if the username exists in the database.
     */
    private boolean usernameExists(String username) {
        // TODO Implement api call
        return false;
    }

    /**
     * @return true if the email exists in the database.
     */
    private boolean emailExists(String email) {
        // TODO Implement api call
        return false;
    }
}
