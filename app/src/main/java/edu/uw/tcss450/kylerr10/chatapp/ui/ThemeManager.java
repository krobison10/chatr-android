package edu.uw.tcss450.kylerr10.chatapp.ui;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import edu.uw.tcss450.kylerr10.chatapp.R;

public class ThemeManager {
    private static int theme = R.style.Theme_Blue;
    
    public static void applyTheme(Activity activity) {
        activity.setTheme(theme); // Set the activity theme
        
        // Status bar color needs to be set separately for API 21 and above, because it's not
        // controlled by the theme
        TypedValue typedValue = new TypedValue();
        if (isDarkModeEnabled(activity)) {
            activity.getTheme().resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true);
        } else {
            activity.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        }
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(typedValue.data);
    }

    private static boolean isDarkModeEnabled(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager != null) {
            return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
        }
        return false;
    }

    public static int getTheme() {
        return theme;
    }

    public static void setTheme(int theme) {
        ThemeManager.theme = theme;
    }
}
