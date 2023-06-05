package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.kylerr10.chatapp.listdata.Notification;
import edu.uw.tcss450.kylerr10.chatapp.R;
import edu.uw.tcss450.kylerr10.chatapp.databinding.FragmentNotificationBinding;

/**
 * RecyclerView Adapter for the notifications view on the home page.
 *
 * @author Kyler Robison
 */
public class NotificationsRecyclerViewAdapter extends
         RecyclerView.Adapter<NotificationsRecyclerViewAdapter.NotificationViewHolder> {
    /**
     * List of notifications
     */
    private final List<Notification> mNotifications;

    private final NotificationsViewModel mViewModel;

    private final NavController mNavController;

    public NotificationsRecyclerViewAdapter(
            List<Notification> notifications,
            FragmentActivity fragmentActivity,
            NavController navController) {
        mNotifications = notifications;
        mViewModel = new ViewModelProvider(fragmentActivity).get(NotificationsViewModel.class);
        mNavController = navController;
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

    public Notification getNotificationAtPosition(int position) {
        return mNotifications.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        //Sets the notification for a notification view
        holder.setNotifcation(mNotifications.get(position), mViewModel, mNavController);
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentNotificationBinding mBinding;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBinding = FragmentNotificationBinding.bind(itemView);
        }

        public void setNotifcation(
                final Notification notifcation,
                NotificationsViewModel model,
                NavController navController) {
            mBinding.title.setText(notifcation.getTitle());
            mBinding.content.setText(notifcation.getContent());
            mBinding.timestamp.setText(notifcation.getTimestamp());

            mBinding.notificationCard.setOnClickListener(view -> Log.d("CLICK", "CLICK"));

            int type = notifcation.getType();
            if(type == Notification.Type.CHAT) {
                mBinding.icon.setImageResource(R.drawable.ic_group_onsurface_24dp);
                mBinding.notificationCard.setOnClickListener(view -> {
                    model.clearNotification(notifcation);
                    navController.navigate(R.id.navigation_chat,
                            null, new NavOptions.Builder()
                                    .setPopUpTo(R.id.navigation_home, true)
                                    .build()
                    );
                });
            }
            else if(type == Notification.Type.CONTACT) {
                mBinding.icon.setImageResource(R.drawable.ic_personadd_onsurface_24dp);
                mBinding.notificationCard.setOnClickListener(view -> {
                    model.clearNotification(notifcation);
                    navController.navigate(R.id.navigation_contacts,
                            null, new NavOptions.Builder()
                                    .setPopUpTo(R.id.navigation_home, true)
                                    .build()
                    );
                });
            }
            else if(type == Notification.Type.MESSAGE) {
                mBinding.icon.setImageResource(R.drawable.ic_chat_onsurface_24dp);
                mBinding.notificationCard.setOnClickListener(view -> {
                    model.clearNotification(notifcation);
                    navController.navigate(R.id.navigation_chat,
                            null, new NavOptions.Builder()
                                    .setPopUpTo(R.id.navigation_home, true)
                                    .build()
                    );
                });
            }
        }

    }
}
