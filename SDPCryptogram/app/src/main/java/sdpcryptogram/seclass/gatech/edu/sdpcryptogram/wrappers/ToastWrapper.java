package sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers;

import android.content.Context;
import android.widget.Toast;

public class ToastWrapper {
    /**
     * Sends a Toast message to the application for 3.5 seconds
     * @param context Application context
     * @param message Message to display
     */
    public static void longToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Sends a Toast message to the application for 2 seconds
     * @param context Application context
     * @param message Message to display
     */
    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}