package edu.uw.tcss450.kylerr10.chatapp.ui.setting;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSettingsBinding;

/**
 * A simple {@link Fragment} subclass responsible for displaying the settings page to the user.
 * @author Jasper Newkirk
 */
public class SettingsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideNavigationBar();
        FragmentSettingsBinding mBinding = FragmentSettingsBinding.bind(requireView());

    }

    @Override
    public void onPause() {
        super.onPause();
        showNavigationBar();
        Log.d("SETTINGS", "onPause: ");
    }

    private void hideNavigationBar() {
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
    }

    private void showNavigationBar() {
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}