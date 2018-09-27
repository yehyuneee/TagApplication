package com.example.yehyunee.tagapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{


    RelativeLayout mImageWholeLayout;
    RelativeLayout mTextWholeLayout;
    RelativeLayout mSnsLoginWholeLayout;
    RelativeLayout mGoogleLoginLayout;
    RelativeLayout mFacebookLoginLayout;

    ImageView mLeftArrow;
    ImageView mRightArrow;

    TextView mEmailLogin;


    GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    Handler mhandler;

    int RC_SIGN_IN = 1000;
    String TAG = "TAG";

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeftArrow = (ImageView)findViewById(R.id.left_arrow_img);
        mRightArrow = (ImageView)findViewById(R.id.right_arrow_img);
        mImageWholeLayout = (RelativeLayout)findViewById(R.id.image_whole_layout);
        mTextWholeLayout = (RelativeLayout)findViewById(R.id.text_whole_layout);
        mSnsLoginWholeLayout = (RelativeLayout)findViewById(R.id.sns_login_whole_layout);
        mGoogleLoginLayout = (RelativeLayout)findViewById(R.id.google_login_btn);
        mFacebookLoginLayout = (RelativeLayout)findViewById(R.id.facebook_login_btn);
        mEmailLogin = (TextView) findViewById(R.id.email_login_layout);

        mGoogleLoginLayout.setClickable(false);
        mFacebookLoginLayout.setClickable(false);

        mEmailLogin.setOnClickListener(this);
        mRightArrow.setOnClickListener(this);
        mLeftArrow.setOnClickListener(this);
        mGoogleLoginLayout.setOnClickListener(this);
        mFacebookLoginLayout.setOnClickListener(this);

        mContext = this;

        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_img));
                mTextWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_text));
                mTextWholeLayout.setVisibility(View.VISIBLE);
            }
        }, 2000);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void facebookLoginOnClick(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.i("TAG", "user: " + user.toString());
                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                            setResult(RESULT_OK);

                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{

        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), ""+connectionResult, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_login_btn:
                signIn();
                break;
            case R.id.facebook_login_btn:
                facebookLoginOnClick();
                break;
            case R.id.right_arrow_img:
                mTextWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_left));
                mTextWholeLayout.setVisibility(View.GONE);
                mSnsLoginWholeLayout.setVisibility(View.VISIBLE);
                mFacebookLoginLayout.setClickable(true);
                mGoogleLoginLayout.setClickable(true);
                mSnsLoginWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_right_left));
                break;
            case R.id.left_arrow_img:
                mSnsLoginWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_right));
                mSnsLoginWholeLayout.setVisibility(View.GONE);
                mTextWholeLayout.setVisibility(View.VISIBLE);
                mFacebookLoginLayout.setClickable(false);
                mGoogleLoginLayout.setClickable(false);
                mTextWholeLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.ani_left_right));
                break;
            case R.id.email_login_layout:
                mFacebookLoginLayout.setClickable(false);
                mGoogleLoginLayout.setClickable(false);
                Intent intent = new Intent(MainActivity.this, EmailLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_slide_in_right, R.anim.ani_slide_out_left);
                break;
        }


    }
}
