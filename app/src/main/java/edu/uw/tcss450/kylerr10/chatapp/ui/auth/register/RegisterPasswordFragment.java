package edu.uw.tcss450.kylerr10.chatapp.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }
}