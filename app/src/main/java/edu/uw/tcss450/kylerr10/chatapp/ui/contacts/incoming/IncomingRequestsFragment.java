package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.incoming;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.uw.tcss450.kylerr10.chatapp.CurrentContact;
import edu.uw.tcss450.kylerr10.chatapp.IncomingContactRequest;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentCurrentContactsBinding;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentIncomingRequestsBinding;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.current.CurrentContactsRecyclerViewAdapter;

/**
 * create an instance of this fragment.
 */
public class IncomingRequestsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incoming_requests, container, false);
    }

    /**
     * Events for when the fragment and its views are created.
     * Also handles building of the RecyclerView that displays current contacts.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentIncomingRequestsBinding binding = FragmentIncomingRequestsBinding.bind(getView());

        // Create a list of dummy notifications
        ArrayList<IncomingContactRequest> contacts = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            contacts.add(new IncomingContactRequest());
        }
        binding.recyclerViewIncomingRequests.setAdapter(
                new IncomingRequestsRecyclerViewAdapter(contacts)
        );
    }
}