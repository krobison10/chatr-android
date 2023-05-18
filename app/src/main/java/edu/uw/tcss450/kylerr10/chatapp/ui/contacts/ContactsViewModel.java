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

    private final MutableLiveData<String> searchText;

    private final MutableLiveData<JSONObject> mGetCurResponse;

    private final MutableLiveData<JSONObject> mGetOutgoingResponse;

    private final MutableLiveData<JSONObject> mGetIncomingResponse;

    private final MutableLiveData<JSONObject> mGetSearchResponse;

    private final MutableLiveData<JSONObject> mCreateContactResponse;

    private final MutableLiveData<JSONObject> mDeleteContactResponse;

    private final MutableLiveData<JSONObject> mAcceptContactResponse;



    public ContactsViewModel(@NonNull Application application) {
        super(application);

        searchText = new MutableLiveData<>();

        mGetCurResponse = new MutableLiveData<>();
        mGetCurResponse.setValue(new JSONObject());

        mGetOutgoingResponse = new MutableLiveData<>();
        mGetOutgoingResponse.setValue(new JSONObject());

        mGetIncomingResponse = new MutableLiveData<>();
        mGetIncomingResponse.setValue(new JSONObject());

        mGetSearchResponse = new MutableLiveData<>();
        mGetSearchResponse.setValue(new JSONObject());

        mCreateContactResponse = new MutableLiveData<>();
        mCreateContactResponse.setValue(new JSONObject());

        mDeleteContactResponse = new MutableLiveData<>();
        mDeleteContactResponse.setValue(new JSONObject());

        mAcceptContactResponse = new MutableLiveData<>();
        mAcceptContactResponse.setValue(new JSONObject());
    }

    public MutableLiveData<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(String text) {
        searchText.setValue(text);
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

    public void addGetSearchResponseObserver(@NonNull LifecycleOwner owner,
                                               @NonNull Observer<? super JSONObject> observer) {
        mGetSearchResponse.observe(owner, observer);
    }

    public void connectGetSearch(String query) {
        connect("/search/" + query, Request.Method.GET, mGetSearchResponse);
    }

    public void addCreateContactResponseObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super JSONObject> observer) {
        mCreateContactResponse.observe(owner, observer);
    }

    public void connectCreateContact(String email) {
        connect("/contacts/" + email, Request.Method.POST, mCreateContactResponse);
    }

    public void addDeleteContactResponseObserver(@NonNull LifecycleOwner owner,
                                                 @NonNull Observer<? super JSONObject> observer) {
        mDeleteContactResponse.observe(owner, observer);
    }

    public void connectDeleteContact(int connId) {
        connect("/contacts/" + connId, Request.Method.DELETE, mDeleteContactResponse);
    }

    public void addAcceptContactResponseObserver(@NonNull LifecycleOwner owner,
                                                 @NonNull Observer<? super JSONObject> observer) {
        mAcceptContactResponse.observe(owner, observer);
    }

    public void connectAcceptContact(int connId) {
        connect("/contacts/accept/" + connId, Request.Method.PUT, mDeleteContactResponse);
    }

    public void updateContacts() {
        connectGetCur();
        connectGetIncoming();
        connectGetOutgoing();
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
