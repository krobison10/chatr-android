package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.incoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * Fragment representing an individual incoming contact request in the RecyclerView list.
 *
 * @author Kyler Robison
 */
public class SingleIncomingRequestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_incoming_request, container, false);
    }
}