package edu.uw.tcss450.kylerr10.chatapp.ui.setting;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.MainActivity;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSettingsBinding;
import edu.uw.tcss450.kylerr10.chatapp.ui.ThemeManager;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.ForecastViewModel;
import edu.uw.tcss450.kylerr10.chatapp.ui.weather.UserLocationViewModel;

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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        view.addOnLayoutChangeListener( // Hides the navigation bar when fragment is created
            (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> hideNavigationBar()
        );
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentSettingsBinding mBinding = FragmentSettingsBinding.bind(requireView());

        // THEME SETTINGS
        switch (ThemeManager.getTheme(requireContext())) { // Set the correct theme button to checked
            case R.style.Theme_Blue:
                mBinding.buttonSkyTheme.setChecked(true);
                break;
            case R.style.Theme_Red:
                mBinding.buttonEveningTheme.setChecked(true);
                break;
        }
        mBinding.buttonSkyTheme.setOnClickListener(button -> {
            ThemeManager.setTheme(requireContext(), R.style.Theme_Blue);
            requireActivity().recreate();

        });
        mBinding.buttonEveningTheme.setOnClickListener(button -> {
            ThemeManager.setTheme(requireContext(), R.style.Theme_Red);
            requireActivity().recreate();
        });

        // ACCOUNT SETTINGS
        mBinding.buttonClearLocations.setOnClickListener(button -> {
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Clear Saved Locations")
                .setMessage("Are you sure you want to clear all saved locations? This action cannot be undone.")
                .setPositiveButton("Clear", (dialog, which) -> {
                    new ViewModelProvider(requireActivity()).get(UserLocationViewModel.class).connectDelete(requireActivity());
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
        });
        mBinding.buttonChangePassword.setOnClickListener(button -> {
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Change Password")
                .setMessage("Are you sure you want to change your password? This will log you out of all devices.")
                .setPositiveButton("Change", (dialog, which) -> {
                    // TODO: Navigate to fragment to change user's password
                    // TODO: Log user out of all devices (expire all tokens)
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        showNavigationBar();
    }

    /**
     * Hides the navigation bar, usually only called when the fragment is created.
     */
    private void hideNavigationBar() {
        if (requireActivity().findViewById(R.id.nav_view) != null)
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the navigation bar, usually only called when the fragment is paused (navigated away from).
     */
    private void showNavigationBar() {
        if (requireActivity().findViewById(R.id.nav_view) != null)
            requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}