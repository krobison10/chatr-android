package edu.uw.tcss450.kylerr10.chatapp.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Pattern;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentRegisterPasswordBinding;

/**
 * create an instance of this fragment.
 */
public class RegisterPasswordFragment extends Fragment {

    private FragmentRegisterPasswordBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterPasswordBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mBinding.buttonRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_registerPasswordFragment_to_loginFragment);

        });

        // Start out with register button disabled
        mBinding.buttonRegister.setEnabled(false);

        // Text watcher for password input listeners
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mBinding.buttonRegister.setEnabled(checkPasswords());
            }
        };

        mBinding.editPassword.addTextChangedListener(textWatcher);
        mBinding.editPasswordreenter.addTextChangedListener(textWatcher);

    }

    /**
     * Helper method to set states of password requirement icons to be called upon changes
     * in the values of either password input. Doubles as a method to check if the password
     * requirements are met.
     *
     * @return true if all the password requirements are met
     */
    private boolean checkPasswords() {
        String passText = mBinding.editPassword.getText().toString();
        String passReenterText = mBinding.editPasswordreenter.getText().toString();

        final int green = R.drawable.ic_check_circle_green_24dp;
        final int grey = R.drawable.ic_check_circle_grey_24dp;

        //Set length icon
        boolean sufficientLength = passText.length() >= 8;
        mBinding.iconPassLength.setImageResource(sufficientLength ? green : grey);

        //Set lowercase icon
        boolean containsLowercase = Pattern.compile("[a-z]").matcher(passText).find();
        mBinding.iconPassLowercase.setImageResource(containsLowercase ? green : grey);

        //Set uppercase icon
        boolean containsUppercase = Pattern.compile("[A-Z]").matcher(passText).find();
        mBinding.iconPassUppercase.setImageResource(containsUppercase ? green : grey);

        //Set number icon
        boolean containsNumber = Pattern.compile("\\d").matcher(passText).find();
        mBinding.iconPassNumber.setImageResource(containsNumber ? green : grey);

        //Set special character icon
        boolean containsSpecial =
                Pattern.compile("[()|¬¦!£$%^&*<>;#~_\\-+=@]").matcher(passText).find();
        mBinding.iconPassSpecial.setImageResource(containsSpecial ? green : grey);

        //Set match icon
        boolean passwordsMatch = passText.equals(passReenterText);
        mBinding.iconPassMatch.setImageResource(passwordsMatch ? green : grey);

        return sufficientLength && containsLowercase && containsUppercase && containsNumber
                && containsSpecial && passwordsMatch;
    }
}