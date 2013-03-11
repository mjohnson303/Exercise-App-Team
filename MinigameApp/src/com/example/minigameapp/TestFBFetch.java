
package com.example.minigameapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.Session.StatusCallback;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class TestFBFetch extends Activity {
    public static final String APP_ID = "475224292514016";

    static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";
    final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
    Button buttonRequest;
    TextView results;
    EditText edit;
    Session session;
    boolean pendingRequest;
    private LoginButton mLoginButton;
    private TextView mText;
    private ImageView mUserPic;
    private Handler mHandler;
    String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins",
    "photo_upload" };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_fetch);
        this.buttonRequest = (Button) findViewById(R.id.buttonRequest);
        this.buttonRequest.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onClickRequest();
            }
        });
        this.results = (TextView) findViewById(R.id.result);
        this.edit = (EditText) findViewById(R.id.edit);
        
        mHandler = new Handler();

        mText = (TextView) TestFBFetch.this.findViewById(R.id.txt);
        mUserPic = (ImageView) TestFBFetch.this.findViewById(R.id.user_pic);
     // Create the Facebook Object using the app id.
        Utility.mFacebook = new Facebook(APP_ID);
        // Instantiate the asynrunner object for asynchronous api calls.
        Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

        mLoginButton = (LoginButton) findViewById(R.id.login);
        
        this.session = createSession();
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.session.onActivityResult(this, requestCode, resultCode, data) &&
                pendingRequest &&
                this.session.getState().isOpened()) {
            sendRequests();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        pendingRequest = savedInstanceState.getBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
    }

    private void onClickRequest() {
        if (this.session.isOpened()) {
            sendRequests();
        } else {
            StatusCallback callback = new StatusCallback() {
                public void call(Session session, SessionState state, Exception exception) {
                    if (exception != null) {
                        new AlertDialog.Builder(TestFBFetch.this)
                                .setTitle("Fail")
                                .setMessage(exception.getMessage())
                                .setPositiveButton("ok", null)
                                .show();
                        TestFBFetch.this.session = createSession();
                    }
                }
            };
            pendingRequest = true;
            this.session.openForRead(new Session.OpenRequest(this).setCallback(callback));
        }
    }

    private void sendRequests() {
        results.setText("");

        String requestIdsText = edit.getText().toString();
        String[] requestIds = requestIdsText.split(",");

        List<Request> requests = new ArrayList<Request>();
        for (final String requestId : requestIds) {
            requests.add(new Request(session, requestId, null, null, new Request.Callback() {
                public void onCompleted(Response response) {
                    GraphObject graphObject = response.getGraphObject();
                    FacebookRequestError error = response.getError();
                    String s = results.getText().toString();
                    if (graphObject != null) {
                        if (graphObject.getProperty("id") != null) {
                            s = s + String.format("%s: %s\n", graphObject.getProperty("id"), graphObject.getProperty(
                                    "name"));
                        } else {
                            s = s + String.format("%s: <no such id>\n", requestId);
                        }
                    } else if (error != null) {
                        s = s + String.format("Error: %s", error.getErrorMessage());
                    }
                    results.setText(s);
                }
            }));
        }
        pendingRequest = false;
        Request.executeBatchAndWait(requests);
    }

    private Session createSession() {
        Session activeSession = Session.getActiveSession();
        if (activeSession == null || activeSession.getState().isClosed()) {
            activeSession = new Session.Builder(this).setApplicationId(APP_ID).build();
            Session.setActiveSession(activeSession);
        }
        return activeSession;
    }
    
    /*
     * Request user name, and picture to show on the main screen.
     */
    public void requestUserData() {
        mText.setText("Fetching user name, profile pic...");
        Bundle params = new Bundle();
        params.putString("fields", "name, picture");
        Utility.mAsyncRunner.request("me", params, new UserRequestListener());
    }
    
    /*
     * Callback for fetching current user's name, picture, uid.
     */
    public class UserRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);

                final String picURL = jsonObject.getJSONObject("picture")
                        .getJSONObject("data").getString("url");
                final String name = jsonObject.getString("name");
                Utility.userUID = jsonObject.getString("id");

                mHandler.post(new Runnable() {
                    public void run() {
                        mText.setText("Welcome " + name + "!");
                        mUserPic.setImageBitmap(Utility.getBitmap(picURL));
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub
			
		}

    }
}
