package edu.uw.tcss450.kylerr10.chatapp.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.auth0.android.jwt.JWT;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * Represents the main root fragment of the contacts page, contains the tab layout.
 *
 * @author Kyler Robison, Betty Abera
 */
public class ContactsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsViewModel viewModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);

        JWT jwt = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class).getJWT();
        viewModel.mJWT = jwt;
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
}