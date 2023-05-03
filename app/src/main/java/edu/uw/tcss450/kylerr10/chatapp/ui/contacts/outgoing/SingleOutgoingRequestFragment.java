package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.outgoing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * Fragment representing an individual outgoing contact request in the RecyclerView list.
 *
 * @author Kyler Robison
 */
public class SingleOutgoingRequestFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_outgoing_request, container, false);
    }
}