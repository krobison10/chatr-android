package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.Notification;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentNotificationBinding;

/**
 * RecyclerView Adapter for the notifications on the home page.
 *
 * @author Kyler Robison
 */
public class NotificationsRecyclerViewAdapter extends
         RecyclerView.Adapter<NotificationsRecyclerViewAdapter.NotificationViewHolder> {
    private List<Notification> mNotifications;

    public NotificationsRecyclerViewAdapter(List<Notification> notifications) {
        mNotifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        return new NotificationViewHolder(
                LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.setNotifcation(mNotifications.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentNotificationBinding mBinding;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentNotificationBinding.bind(itemView);
        }

        public void setNotifcation(final Notification notifcation) {

        }

    }
}
