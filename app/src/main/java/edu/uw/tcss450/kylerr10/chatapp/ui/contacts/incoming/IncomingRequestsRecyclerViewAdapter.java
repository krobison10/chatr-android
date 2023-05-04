package edu.uw.tcss450.kylerr10.chatapp.ui.contacts.incoming;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.IncomingContactRequest;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentSingleIncomingRequestBinding;

/**
 * RecyclerViewAdapter for the incoming contact requests list.
 *
 * @author Kyler Robison
 */
public class IncomingRequestsRecyclerViewAdapter
    extends RecyclerView.Adapter<IncomingRequestsRecyclerViewAdapter.IncomingRequestViewHolder> {

    private final List<IncomingContactRequest> mRequests;

    public IncomingRequestsRecyclerViewAdapter(List<IncomingContactRequest> requests) {
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

        public void setRequest(final IncomingContactRequest contact) {
            // TODO: implement code to actually set values
        }

    }
}
