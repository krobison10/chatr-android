package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleAddContactBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Contact;

/**
 * RecyclerView Adapter for the add contacts list of the add contacts page in contacts.
 *
 * @author Kyler Robison
 */
public class AddContactsRecyclerViewAdapter
        extends RecyclerView.Adapter<AddContactsRecyclerViewAdapter.AddContactViewHolder> {
    /**
     * List of AddContacts for the RecyclerView
     */
    private final List<Contact> mContacts;

    public AddContactsRecyclerViewAdapter(List<Contact> addContacts) {
        mContacts = addContacts;
    }

    @NonNull
    @Override
    public AddContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new AddContactViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_single_add_contact, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(AddContactViewHolder holder, int position) {
        //Sets the notification for a notification view
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class AddContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentSingleAddContactBinding mBinding;

        public AddContactViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentSingleAddContactBinding.bind(itemView);
        }

        public void setContact(final Contact contact) {
            mBinding.textMain.setText(contact.mUsername);
        }
    }
}

