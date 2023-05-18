package edu.uw.tcss450.kylerr10.chatapp.ui.contacts;

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
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;

public class ContactsViewModel extends AndroidViewModel {
    //TODO: Encapsulate
    public JWT mJWT;

    private final MutableLiveData<JSONObject> mGetCurResponse;

    private final MutableLiveData<JSONObject> mGetOutgoingResponse;

    private final MutableLiveData<JSONObject> mGetIncomingResponse;



    public ContactsViewModel(@NonNull Application application) {
        super(application);

        mGetCurResponse = new MutableLiveData<>();
        mGetCurResponse.setValue(new JSONObject());

        mGetOutgoingResponse = new MutableLiveData<>();
        mGetOutgoingResponse.setValue(new JSONObject());

        mGetIncomingResponse = new MutableLiveData<>();
        mGetIncomingResponse.setValue(new JSONObject());
    }

    public void addGetCurResponseObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super JSONObject> observer) {
        mGetCurResponse.observe(owner, observer);
    }

    public void connectGetCur() {
        connect("/contacts/current", Request.Method.GET, mGetCurResponse);
    }

    public void addGetOutgoingResponseObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super JSONObject> observer) {
        mGetOutgoingResponse.observe(owner, observer);
    }

    public void connectGetOutgoing() {
        connect("/contacts/outgoing", Request.Method.GET, mGetOutgoingResponse);
    }

    public void addGetIncomingResponseObserver(@NonNull LifecycleOwner owner,
                                               @NonNull Observer<? super JSONObject> observer) {
        mGetIncomingResponse.observe(owner, observer);
    }

    public void connectGetIncoming() {
        connect("/contacts/incoming", Request.Method.GET, mGetIncomingResponse);
    }


    public void connect(String endpoint, int method, MutableLiveData<JSONObject> responseDestination) {
        String url = "http://10.0.2.2:5000" + endpoint;
        Request request = new JsonObjectRequest(
                method,
                url,
                null, //no body for this request
                responseDestination::setValue,
                error -> handleError(error, responseDestination)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", mJWT.toString());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleError(final VolleyError error, MutableLiveData<JSONObject> responseData) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                responseData.setValue(new JSONObject("{" +
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
                responseData.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
