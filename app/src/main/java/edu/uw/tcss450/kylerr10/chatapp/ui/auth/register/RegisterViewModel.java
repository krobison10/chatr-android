package edu.uw.tcss450.kylerr10.chatapp.ui.auth.register;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author Kyler Robison, Betelhem Bada
 */

public class RegisterViewModel extends AndroidViewModel {
    /**
     * Response from the call to the web service to register.
     */
    private MutableLiveData<JSONObject> mResponse;
    /**
     * First name of the user to be stored after moving past the first step of register.
     */
    private String mUserFName;
    /**
     * Last name of the user to be stored after moving past the first step of register.
     */
    private String mUserLName;
    /**
     * Username of the user to be stored after moving past the first step of register.
     */
    private String mUserUsername;
    /**
     * Email of the user to be stored after moving past the first step of register.
     */
    private String mUserEmail;


    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Sets the value of the user's first name to register with.
     * @param str the value to set it as.
     */
    public void setUserFName(String str) {
        mUserFName = str;
    }

    /**
     * Sets the value of the user's last name to register with.
     * @param str the value to set it as.
     */
    public void setUserLName(String str) {
        mUserLName = str;
    }

    /**
     * Sets the value of the user's username to register with.
     * @param str the value to set it as.
     */
    public void setUserUsername(String str) {
        mUserUsername = str;
    }

    /**
     * Sets the value of the user's email to register with.
     * @param str the value to set it as.
     */
    public void setUserEmail(String str) {
        mUserEmail = str;
    }

    /**
     * @return the entered email address of the user.
     */
    public String getUserEmail() {
        return mUserEmail;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Connects and sends the request. Uses the saved first name, last name, username, and email,
     * only requires the password to be sent in.
     * @param password the valid password to be used for the request.
     */
    public void connect(final String password) {
        String url = "http://10.0.2.2:5000/auth";
        JSONObject body = new JSONObject();
        try {
            body.put("first", mUserFName);
            body.put("last", mUserLName);
            body.put("username", mUserUsername);
            body.put("email", mUserEmail);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

}
