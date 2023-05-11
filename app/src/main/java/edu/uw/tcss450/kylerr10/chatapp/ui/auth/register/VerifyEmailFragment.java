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
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentVerifyEmailBinding;

/**
 * Fragment for user email verification.
 *
 * @author Kyler Robison
 */
public class VerifyEmailFragment extends Fragment {

    private FragmentVerifyEmailBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mBinding.buttonVerify.setOnClickListener(button -> Navigation.findNavController(
                        getView()).navigate(R.id.action_verifyEmailFragment_to_homeActivity));
    }
}