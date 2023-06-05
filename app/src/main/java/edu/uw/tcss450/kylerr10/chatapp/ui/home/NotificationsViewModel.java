package edu.uw.tcss450.kylerr10.chatapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.uw.tcss450.kylerr10.chatapp.listdata.Notification;

public class NotificationsViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Notification>> mCurrentNotifications;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        mCurrentNotifications = new MutableLiveData<>();
        mCurrentNotifications.setValue(new ArrayList<>());
    }

    public void addNotificationsObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super List<Notification>> observer) {
        mCurrentNotifications.observe(owner, observer);
    }

    public void clearNotificationsOfType(int type) {
        mCurrentNotifications.setValue(
                Objects.requireNonNull(mCurrentNotifications.getValue()).stream()
                .filter(notification -> notification.getType() != type)
                .collect(Collectors.toList()));
    }

    public void clearNotification(Notification remove) {
        Objects.requireNonNull(mCurrentNotifications.getValue()).remove(remove);
        mCurrentNotifications.setValue(new ArrayList<>(mCurrentNotifications.getValue()));
    }

    public void addNotification(Notification notification) {
        Objects.requireNonNull(mCurrentNotifications.getValue()).add(0, notification);
        mCurrentNotifications.setValue(new ArrayList<>(mCurrentNotifications.getValue()));
    }

    public void clearAll() {
        mCurrentNotifications.setValue(new ArrayList<>());
    }
}
