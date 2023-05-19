package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentAddContactsBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Contact;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.ContactsViewModel;

/**
 * Fragment where the user can add contacts.
 *
 * @author Kyler Robison
 */
public class AddContactsFragment extends Fragment {
    /**
     * Flag used to prevent the response handler from being called the first time it is set to
     * observe the response.
     */
    private boolean observerCreated = false;

    private ContactsViewModel mContactsViewModel;

    private FragmentAddContactsBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsViewModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentAddContactsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    /**
     * Events for when the fragment and its views are created.
     * Also handles building of the RecyclerView that displays contacts.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContactsViewModel.addCreateContactResponseObserver(getViewLifecycleOwner(),
                response -> mContactsViewModel.updateContacts());

        mContactsViewModel.addGetSearchResponseObserver(getViewLifecycleOwner(), this::observeResponse);

        mContactsViewModel.getSearchText().observe(getViewLifecycleOwner(), query -> {
            mContactsViewModel.connectGetSearch(query);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mContactsViewModel.connectGetSearch(mContactsViewModel.getSearchText().getValue());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to the ViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if(!observerCreated) { //Prevents method from executing just because the observer is added
            observerCreated = true;
            return;
        }
        if (response.length() > 0) {
            if (response.has("code")) {
                showErrorNotification("An error occurred");
            } else {
                try {
                    processResponse(response);
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void processResponse(final JSONObject response) throws JSONException {
        ArrayList<Contact> contactsList = new ArrayList<>();

        JSONArray contactsArray = response.getJSONArray("users");
        for(int i = 0; i < contactsArray.length(); i++) {
            JSONObject contactObject = contactsArray.getJSONObject(i);

            String username = contactObject.getString("username");
            String email = contactObject.getString("email");
            String firstName = contactObject.getString("firstname");
            String lastName = contactObject.getString("lastname");

            Contact c = new Contact(0, username, email, firstName, lastName);
            contactsList.add(c);
        }

        mBinding.recyclerViewAddContacts.setAdapter(
                new AddContactsRecyclerViewAdapter(mContactsViewModel, contactsList)
        );
    }

    /**
     * Displays an error notification to the user.
     *
     * @param message message to show.
     */
    private void showErrorNotification(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }
}