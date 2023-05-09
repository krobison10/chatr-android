package edu.uw.tcss450.kylerr10.chatapp.ui.auth.register;

import android.os.Bundle;
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
    private FragmentRegisterBinding mbinding;

    private RegisterViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mbinding = FragmentRegisterBinding.inflate(inflater);
        return  mbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mbinding.buttonNext.setOnClickListener(button -> {

            //Store current values in viewModel for use in the next step
            mViewModel.setUserFName(mbinding.editFirst.getText().toString());
            mViewModel.setUserLName(mbinding.editLast.getText().toString());
            mViewModel.setUserEmail(mbinding.editEmail.getText().toString());
            mViewModel.setUserUsername(mbinding.editUsername.getText().toString());

            Navigation.findNavController(getView())
                    .navigate(R.id.action_registerFragment_to_registerPasswordFragment);

        });

        mbinding.buttonToLogin.setOnClickListener(button -> {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_registerFragment_to_loginFragment);
        });
    }
}
