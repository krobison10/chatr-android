package edu.uw.tcss450.kylerr10.chatapp.ui.auth.login;

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
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentLoginBinding;

/**
 * A simple {@link Fragment} subclass responsible for validating user credentials.
 * @author Jasper Newkirk
 */
public class LoginFragment extends Fragment {

    public FragmentLoginBinding mBinding;

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentLoginBinding.bind(requireView());

        mBinding.buttonLogin.setOnClickListener(button -> {
            Navigation.findNavController(requireView()).navigate(
                    R.id.action_loginFragment_to_homeActivity
            );
            getActivity().finish();
        });

        mBinding.buttonToRegister.setOnClickListener(button -> {
         Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);

        });

        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
        mBinding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

    }

}