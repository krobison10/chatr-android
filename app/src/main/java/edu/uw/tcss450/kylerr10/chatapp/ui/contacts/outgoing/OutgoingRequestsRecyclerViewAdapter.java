package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.outgoing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentOutgoingRequestsBinding;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleOutgoingRequestBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Contact;
import edu.uw.tcss450.kylerr10.chatapp.ui.contacts.ContactsViewModel;

/**
 * RecyclerViewAdapter for the outgoing contact requests list.
 *
 * @author Kyler Robison
 */
public class OutgoingRequestsRecyclerViewAdapter
    extends RecyclerView.Adapter<OutgoingRequestsRecyclerViewAdapter.OutgoingRequestViewHolder> {
    private ContactsViewModel mContactsViewModel;
    private final List<Contact> mRequests;

    public OutgoingRequestsRecyclerViewAdapter(ContactsViewModel viewModel, List<Contact> requests) {
        mContactsViewModel = viewModel;
        mRequests = requests;
    }

    @NonNull
    @Override
    public OutgoingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutgoingRequestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_single_outgoing_request, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OutgoingRequestViewHolder holder, int position) {
        holder.setRequest(mRequests.get(position));

        holder.cancelButton.setOnClickListener(view ->
                mContactsViewModel.connectDeleteContact(mRequests.get(position).mConnectionId));
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class OutgoingRequestViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public FragmentSingleOutgoingRequestBinding mBinding;

        public MaterialCardView cancelButton;

        public OutgoingRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentSingleOutgoingRequestBinding.bind(itemView);
            cancelButton = mBinding.btnCancel;
        }

        public void setRequest(final Contact contact) {
            mBinding.textMain.setText(contact.mUsername);
        }
    }
}
