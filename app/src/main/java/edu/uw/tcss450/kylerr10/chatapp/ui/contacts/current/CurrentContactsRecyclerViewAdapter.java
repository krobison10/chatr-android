package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.current;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.listdata.CurrentContact;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleCurrentContactBinding;

/**
 * RecyclerView Adapter for the list of current contacts.
 *
 * @author Kyler Robison
 */
public class CurrentContactsRecyclerViewAdapter
        extends RecyclerView.Adapter<CurrentContactsRecyclerViewAdapter.CurrentContactViewHolder> {
    /**
     * List of CurrentContacts for the RecyclerView
     */
    private final List<CurrentContact> mContacts;

    public CurrentContactsRecyclerViewAdapter(List<CurrentContact> currentContacts) {
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
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class CurrentContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentSingleCurrentContactBinding mBinding;

        public CurrentContactViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentSingleCurrentContactBinding.bind(itemView);
        }

        public void setContact(final CurrentContact contact) {
            mBinding.textMain.setText(contact.mUsername);
        }
    }
}
