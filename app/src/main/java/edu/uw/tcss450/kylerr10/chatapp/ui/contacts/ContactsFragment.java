package edu.uw.tcss450.kylerr10.chatapp.ui.contacts;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.auth0.android.jwt.JWT;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentContactsBinding;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * Represents the main root fragment of the contacts page, contains the tab layout.
 *
 * @author Kyler Robison
 */
public class ContactsFragment extends Fragment {

    private ContactsViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);

        JWT jwt = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class).getJWT();
        mViewModel.mJWT = jwt;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentContactsBinding binding = FragmentContactsBinding.bind(getView());

        boolean containsText = binding.searchText.getText().toString().length() != 0;
        binding.iconSearch.setVisibility(containsText ? View.GONE : View.VISIBLE);
        binding.btnClear.setVisibility(containsText ? View.VISIBLE : View.GONE);

        binding.overlay.setClickable(!containsText);
        binding.overlay.setFocusable(!containsText);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        ContactsPagerAdapter pagerAdapter = new ContactsPagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(
                tabLayout,
                viewPager,
                true,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Current");
                            break;
                        case 1:
                            tab.setText("Add");
                            break;
                        case 2:
                            tab.setText("Incoming");
                            break;
                        case 3:
                            tab.setText("Outgoing");
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid position: " + position);
                    }
                }
        ).attach();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        FragmentContactsBinding binding = FragmentContactsBinding.bind(v);
        binding.searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setSearchText(binding.searchText.getText().toString());
            }
        });

        binding.overlay.setOnClickListener(view -> {
            binding.searchText.requestFocus();
            binding.searchText.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchText, InputMethodManager.SHOW_IMPLICIT);

        });

        binding.searchText.setOnFocusChangeListener((view, focus) -> {
            if(binding.searchText.getText().toString().length() == 0) {
                if(focus) { //No text and gaining focus
                    binding.iconSearch.setVisibility(View.GONE);
                    binding.overlay.setClickable(false);
                    binding.overlay.setFocusable(false);
                    binding.btnClear.setVisibility(View.VISIBLE);
                }
                else { //No text and loosing focus
                    binding.iconSearch.setVisibility(View.VISIBLE);
                    binding.overlay.setClickable(true);
                    binding.overlay.setFocusable(true);
                    binding.btnClear.setVisibility(View.GONE);
                }
            }
            else {
                if(focus) { //Text and gaining focus
                    binding.overlay.setClickable(false);
                    binding.overlay.setFocusable(false);
                }
            }

            if(!focus) { //Close keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            binding.searchText.setHint(focus ? "Search" : "");

        });

        binding.searchText.setOnEditorActionListener((view, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                binding.searchText.clearFocus();

                getActivity().findViewById(R.id.navigation_contacts).requestFocus();
                return true;
            }
            return false;
        });

        binding.btnClear.setOnClickListener(view -> {
            if(binding.searchText.getText().toString().length() != 0) {
                binding.searchText.setText("");
            }
            else {
                binding.searchText.clearFocus();
            }

            if(!binding.searchText.hasFocus()) {
                binding.searchText.requestFocus();
                binding.searchText.clearFocus();
            }
        });
    }
}