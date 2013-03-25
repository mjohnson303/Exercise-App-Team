package com.example.minigameapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ManageAccount extends FragmentActivity implements OnClickListener, OnItemSelectedListener{
	private Account a;
	String name;
	private TextView score,nameView;
	private LoginButton loginButton;
	private ProfilePictureView profilePictureView;
	private GraphUser user;
	private Button statusButton;
    private Button friendButton;
    private ViewGroup controlsContainer;


	private PendingAction pendingAction = PendingAction.NONE;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private enum PendingAction {
		NONE,
		POST_PHOTO,
		POST_STATUS_UPDATE
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.manage_account);

		a = Account.getInstance();
		name=a.getName();
		nameView = (TextView)findViewById(R.id.name);
		score = (TextView)findViewById(R.id.score);
		score.setText("Score: "+a.getScore());

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			public void onUserInfoFetched(GraphUser user) {
				ManageAccount.this.user = user;
				updateUI();
				// It's possible that we were waiting for this.user to be populated in order to post a
				// status update.
				handlePendingAction();
			}
		});
		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
		statusButton = (Button) findViewById(R.id.postStatusUpdateButton);
		statusButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				onClickStatusUpdate();
			}
		});
		friendButton = (Button) findViewById(R.id.pickFriendsButton);
        friendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPickFriends();
            }
        });
        
        controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		statusButton.setEnabled(enableButtons);
		//postPhotoButton.setEnabled(enableButtons);
		friendButton.setEnabled(enableButtons);
		//pickPlaceButton.setEnabled(enableButtons);

		if (enableButtons && user != null) {
			profilePictureView.setProfileId(user.getId());
			nameView.setText("Greetings "+ user.getFirstName());
			a.setName(user.getFirstName());
		} else {
			profilePictureView.setProfileId(null);
			nameView.setText("Greetings");
		}
	}

	public void onClick(View arg0) {
		switch(arg0.getId()){
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		updateUI();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingAction != PendingAction.NONE &&
				(exception instanceof FacebookOperationCanceledException ||
						exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(ManageAccount.this)
			.setTitle("Cancelled")
			.setMessage("Permission not granted")
			.setPositiveButton("ok", null)
			.show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		// These actions may re-set pendingAction if they are still pending, but we assume they
		// will succeed.
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;
		switch (previouslyPendingAction) {
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		//Auto-generated method stub

	}


	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		//Auto-generated method stub

	}

	private void onClickStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE);
	}

	private void postStatusUpdate() {
		if (user != null && hasPublishPermission()) {
			//final String message = getString(R.string.status_update, user.getFirstName(), (new Date().toString()));
			final String message = user.getFirstName() +" has been playing Think Fast! Join the fun to get fit while playing games!";
			Request request = Request
					.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
						public void onCompleted(Response response) {
							Toast.makeText(getApplicationContext(), response.toString(),	Toast.LENGTH_LONG).show();
						}
					});
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}

	
	//TODO CHANGE THIS CODE - status update
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction();
			} else {
				// We need to get new permissions, then complete the action when we get called back.
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, Arrays.asList("publish_actions")));
			}
		}
	}
	
	//TODO change this code - friends
	private void showPickerFragment(PickerFragment<?> fragment) {
        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
        	public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
        		Toast.makeText(getApplicationContext(), error.getMessage(),	Toast.LENGTH_LONG).show();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        controlsContainer.setVisibility(View.GONE);

        // We want the fragment fully created so we can use it immediately.
        fm.executePendingTransactions();

        fragment.loadData(false);
    }

    private void onClickPickFriends() {
        final FriendPickerFragment fragment = new FriendPickerFragment();

        setFriendPickerListeners(fragment);

        showPickerFragment(fragment);
    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });
    }

    private void onFriendPickerDone(FriendPickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String results = "";

        Collection<GraphUser> selection = fragment.getSelection();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = getString(R.string.no_friends_selected);
        }

        Toast.makeText(getApplicationContext(), results,	Toast.LENGTH_LONG).show();
    }

}
