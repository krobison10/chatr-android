package edu.uw.tcss450.kylerr10.chatapp.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentRegisterBinding;

/**
 * @author Betlhem
 */
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding mbinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mbinding = FragmentRegisterBinding.inflate(inflater);
        return  mbinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mbinding.buttonRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_verifyEmailFragment);

        });

        mbinding.buttonToLogin.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
        });

    }
}
