package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.incoming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleIncomingRequestBinding;
import edu.uw.tcss450.kylerr10.chatapp.listdata.Contact;

/**
 * RecyclerViewAdapter for the incoming contact requests list.
 *
 * @author Kyler Robison
 */
public class IncomingRequestsRecyclerViewAdapter
    extends RecyclerView.Adapter<IncomingRequestsRecyclerViewAdapter.IncomingRequestViewHolder> {

    private final List<Contact> mRequests;

    public IncomingRequestsRecyclerViewAdapter(List<Contact> requests) {
        mRequests = requests;
    }

    @NonNull
    @Override
    public IncomingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IncomingRequestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_single_incoming_request, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingRequestViewHolder holder, int position) {
        holder.setRequest(mRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class IncomingRequestViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentSingleIncomingRequestBinding mBinding;

        public IncomingRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentSingleIncomingRequestBinding.bind(itemView);
        }

        public void setRequest(final Contact contact) {
            mBinding.textMain.setText(contact.mUsername);
        }

    }
}
