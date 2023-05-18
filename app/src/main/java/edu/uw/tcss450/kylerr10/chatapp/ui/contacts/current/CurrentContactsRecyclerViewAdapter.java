package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.current;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleCurrentContactBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Contact;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.ContactsViewModel;

/**
 * RecyclerView Adapter for the list of current contacts.
 *
 * @author Kyler Robison
 */
public class CurrentContactsRecyclerViewAdapter
        extends RecyclerView.Adapter<CurrentContactsRecyclerViewAdapter.CurrentContactViewHolder> {
    ContactsViewModel mContactsViewModel;
    /**
     * List of CurrentContacts for the RecyclerView
     */
    private final List<Contact> mContacts;

    public CurrentContactsRecyclerViewAdapter(ContactsViewModel viewModel, List<Contact> currentContacts) {
        mContactsViewModel = viewModel;
        mContacts = currentContacts;
    }

    @NonNull
    @Override
    public CurrentContactViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new CurrentContactViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_single_current_contact, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(CurrentContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));

        holder.binding.btnOptions.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(holder.mView.getContext(), holder.binding.btnOptions);
            popup.getMenuInflater().inflate(R.menu.cur_contact_opts_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_remove_contact:
                        mContactsViewModel.connectDeleteContact(
                                mContacts.get(position).mConnectionId);
                        return true;
                    case R.id.action_display_info:
                        return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class CurrentContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public FragmentSingleCurrentContactBinding binding;


        public CurrentContactViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            binding = FragmentSingleCurrentContactBinding.bind(itemView);
        }

        public void setContact(final Contact contact) {
            binding.textMain.setText(contact.mUsername);
        }
    }
}
