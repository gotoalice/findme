package com.bilko.findme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth mFirebaseAuth;
    protected DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    protected boolean onCheckTextViewError(final TextView view, final String error) {
        if (!TextUtils.isEmpty(error)) {
            view.setError(error);
            view.requestFocus();
            return false;
        }
        return true;
    }

    protected void onShowProgress(final View mHiddenView, final View mProgressView,
        final boolean show) {
        onShowAnimation(mHiddenView, !show);
        onShowAnimation(mProgressView, show);
    }

    private void onShowAnimation(final View mAnimatedView, final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mAnimatedView.setVisibility(show ? View.VISIBLE : View.GONE);
        mAnimatedView
                .animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimatedView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
    }

    protected void onCloseKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void onStartActivity(final Class activity) {
        startActivity(new Intent(this, activity));
        finish();
    }

    protected String getUserId() {
        final FirebaseUser mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser != null) {
            return mCurrentUser.getUid();
        }
        return "";
    }
}