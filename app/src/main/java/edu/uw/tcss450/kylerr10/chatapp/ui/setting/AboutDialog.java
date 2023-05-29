package edu.uw.tcss450.kylerr10.chatapp.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.R;

/**
 * A dialog that displays information about the application to the user.
 * @author Jasper Newkirk
 */
public class AboutDialog extends MaterialAlertDialogBuilder {
    /**
     * The about message to display to the user.
     */
    private final String mAboutMessage =
        "This app was created in collaboration by Leyla Ahmed, Betty Abera Bada, Jasper Newkirk, "
        + " and Kyler Robison for the TCSS 450 A class in the Spring of 2023 at the University of"
        + " Washington Tacoma.";
    /**
     * The text view that displays the version number.
     */
    private final TextView mVersionTextView = new TextView(getContext());

    /**
     * Constructs a new instance of the about dialog. Creates an {@link android.app.AlertDialog}
     * to display to the user upon a call to the {@link #show()} method.
     * @param context The context of the application.
     */
    public AboutDialog(@NonNull Context context) {
        super(context);
        initVersionTextView(context);
        this.setTitle("About");
        this.setMessage(mAboutMessage);
        this.setView(mVersionTextView);
        this.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
    }

    /**
     * Initializes the text view responsible for relaying the version number to the user.
     * @param context The context of the application.
     */
    private void initVersionTextView(@NonNull Context context) {
        mVersionTextView.setText(
            String.format(
                "%s %s",
                context.getText(R.string.title_version),
                BuildConfig.VERSION_NAME
            )
        );
        mVersionTextView.setTextAppearance(android.R.style.TextAppearance_Material_Body2);
        mVersionTextView.setPaddingRelative(64, 32, 0, 0);
    }
}
