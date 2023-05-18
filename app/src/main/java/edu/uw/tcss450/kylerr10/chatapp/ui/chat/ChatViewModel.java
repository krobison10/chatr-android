package edu.uw.tcss450.kylerr10.chatapp.ui.chat;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;


public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private String mChatName;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }
    private UserInfoViewModel mUserInfoViewModel;
    String mJwt = mUserInfoViewModel.getJWT().toString();

    public String getChatName() {
        return mChatName;
    }

    public LiveData<JSONObject> getResponse() {
        return mResponse;
    }

    private void handleError(final VolleyError error) {
        if (error instanceof TimeoutError) {
            try {
                mResponse.setValue(new JSONObject("{\"error\":\"Request timeout\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{\"error\":\"" + error.getMessage() + "\"}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset()).replace('\"', '\'');
            try {
                JSONObject response = new JSONObject(data);
                mResponse.setValue(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createChatRoom(String chatName) {
        String url = "http://10.0.2.2:5000/chats";
        JSONObject body = new JSONObject();
        try {
            body.put("name", chatName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> mResponse.setValue(response),
                this::handleError
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void deleteChatRoom(String chatId) {
        String url = "http://10.0.2.2:5000/chats";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> mResponse.setValue(response),
                this::handleError
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mJwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

//Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public String getJWT() {
        return mJwt;
    }

    public void setJWT(String jwt) {
        this.mJwt = jwt;
    }

}
