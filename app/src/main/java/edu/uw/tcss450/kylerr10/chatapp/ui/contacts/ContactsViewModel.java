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

import edu.uw.tcss450.kylerr10.chatapp.BuildConfig;
import edu.uw.tcss450.kylerr10.chatapp.io.RequestQueueSingleton;

/**
 * ViewModel for all contacts functionality
 *
 * @author Kyler Robison
 */
public class ContactsViewModel extends AndroidViewModel {
    //TODO: Encapsulate
    /**
     * JWT for the logged in user
     */
    public JWT mJWT;

    /**
     * Represents the text currently entered in the search bar of the parent contacts fragment.
     */
    private final MutableLiveData<String> searchText;
    /**
     * The response from the endpoint to get all current contacts.
     */
    private final MutableLiveData<JSONObject> mGetCurResponse;
    /**
     * The response from the endpoint to get all outgoing contact requests.
     */
    private final MutableLiveData<JSONObject> mGetOutgoingResponse;
    /**
     * The response from the endpoint to get all incoming contact requests.
     */
    private final MutableLiveData<JSONObject> mGetIncomingResponse;
    /**
     * The response from the endpoint to get all users given a search query.
     */
    private final MutableLiveData<JSONObject> mGetSearchResponse;
    /**
     * The response from the endpoint to create a contact request.
     */
    private final MutableLiveData<JSONObject> mCreateContactResponse;
    /**
     * The response from the endpoint to delete a contact/request.
     */
    private final MutableLiveData<JSONObject> mDeleteContactResponse;
    /**
     * The response from the endpoint to accept a contact request.
     */
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

    /**
     * @return the current text in the search bar.
     */
    public MutableLiveData<String> getSearchText() {
        return searchText;
    }

    /**
     * Sets the value of the search text for the ViewModel.
     * @param text
     */
    public void setSearchText(String text) {
        searchText.setValue(text);
    }

    public void addGetCurResponseObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super JSONObject> observer) {
        mGetCurResponse.observe(owner, observer);
    }

    /**
     * Makes api call to get current contacts.
     */
    public void connectGetCur() {
        connect("/contacts/current", Request.Method.GET, mGetCurResponse);
    }

    public void addGetOutgoingResponseObserver(@NonNull LifecycleOwner owner,
                                          @NonNull Observer<? super JSONObject> observer) {
        mGetOutgoingResponse.observe(owner, observer);
    }

    /**
     * Makes api call to get outgoing contact requests.
     */
    public void connectGetOutgoing() {
        connect("/contacts/outgoing", Request.Method.GET, mGetOutgoingResponse);
    }

    public void addGetIncomingResponseObserver(@NonNull LifecycleOwner owner,
                                               @NonNull Observer<? super JSONObject> observer) {
        mGetIncomingResponse.observe(owner, observer);
    }

    /**
     * Makes the api call to get outgoing contact requests.
     */
    public void connectGetIncoming() {
        connect("/contacts/incoming", Request.Method.GET, mGetIncomingResponse);
    }

    public void addGetSearchResponseObserver(@NonNull LifecycleOwner owner,
                                               @NonNull Observer<? super JSONObject> observer) {
        mGetSearchResponse.observe(owner, observer);
    }

    /**
     * Makes the api call to search for users given a query.
     * @param query the string used to search with.
     */
    public void connectGetSearch(String query) {
        connect("/search/" + query, Request.Method.GET, mGetSearchResponse);
    }

    public void addCreateContactResponseObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super JSONObject> observer) {
        mCreateContactResponse.observe(owner, observer);
    }

    /**
     * Makes the api call to request to create a contact request.
     * @param email the email of the user to send the request to.
     */
    public void connectCreateContact(String email) {
        connect("/contacts/" + email, Request.Method.POST, mCreateContactResponse);
    }

    public void addDeleteContactResponseObserver(@NonNull LifecycleOwner owner,
                                                 @NonNull Observer<? super JSONObject> observer) {
        mDeleteContactResponse.observe(owner, observer);
    }

    /**
     * Makes the api call to request to remove a contact or request.
     * @param connId the id of the connection to delete.
     */
    public void connectDeleteContact(int connId) {
        connect("/contacts/" + connId, Request.Method.DELETE, mDeleteContactResponse);
    }

    public void addAcceptContactResponseObserver(@NonNull LifecycleOwner owner,
                                                 @NonNull Observer<? super JSONObject> observer) {
        mAcceptContactResponse.observe(owner, observer);
    }

    /**
     * Makes the api call to accept a contact request.
     * @param connId the id of the request to accept.
     */
    public void connectAcceptContact(int connId) {
        connect("/contacts/accept/" + connId, Request.Method.PUT, mDeleteContactResponse);
    }

    /**
     * Makes get api calls to update the lists in all tabs of contacts
     */
    public void updateContacts() {
        connectGetCur();
        connectGetSearch(searchText.getValue());
        connectGetIncoming();
        connectGetOutgoing();
    }

    /**
     * Sends a body-less request to base url of the web service.
     * @param endpoint the endpoint of the destination. Should start with a "/".
     * @param method the http method to use.
     * @param responseDestination the MutableLiveData to store the response in.
     */
    public void connect(String endpoint, int method, MutableLiveData<JSONObject> responseDestination) {
        String url = BuildConfig.BASE_URL + endpoint;
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

    /**
     * Handles request error
     * @param error error.
     * @param responseDestination destination of the response.
     */
    private void handleError(final VolleyError error, MutableLiveData<JSONObject> responseDestination) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                responseDestination.setValue(new JSONObject("{" +
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
                responseDestination.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
