package edu.uw.tcss450.kylerr10.chatapp.ui.contacts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.add.SearchContactsFragment;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.current.CurrentContactsFragment;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.incoming.IncomingRequestsFragment;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.outgoing.OutgoingRequestsFragment;

public class ContactsPagerAdapter extends FragmentStateAdapter {

    public ContactsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CurrentContactsFragment();
            case 1:
                return new SearchContactsFragment();
            case 2:
                return new IncomingRequestsFragment();
            case 3:
                return new OutgoingRequestsFragment();
        }
        throw new RuntimeException("Invalid pager position");
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
