package br.com.kosawalabs.apprecommendation.presentation.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.presentation.list.AppListActivity;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginPresenter;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String EXTRA_LOGOUT = "br.com.kosawalabs.apprecommendation.EXTRA_LOGOUT";
    private LoginPresenter.LoginPresenterFromView presenter;

    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;

    public static void startWithLogout(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(EXTRA_LOGOUT, true);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        presenter = LoginInjector.inject(this, getApplicationContext());
        presenter.init(getIsLogoutFromBundle());
    }

    private boolean getIsLogoutFromBundle() {
        return getIntent() != null && getIntent().hasExtra(EXTRA_LOGOUT);
    }

    private void bindView() {
        setContentView(R.layout.activity_login);
        usernameView = (AutoCompleteTextView) findViewById(R.id.username);

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    presenter.onEditorAction(
                            usernameView.getText().toString(),
                            passwordView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onLoginButtonClicked(
                        usernameView.getText().toString(),
                        passwordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void showLoading() {
        hideSoftKeyBoard();
        showProgress(true);
    }

    @Override
    public void clearEditTextErrors() {
        usernameView.setError(null);
        passwordView.setError(null);
    }

    @Override
    public void showErrorUsernameFieldEmpty() {
        usernameView.setError(getString(R.string.error_field_required));
        usernameView.requestFocus();
    }

    @Override
    public void showErrorPasswordFieldEmpty() {
        passwordView.setError(getString(R.string.error_field_required));
        passwordView.requestFocus();
    }

    @Override
    public void dismissProgress() {
        showProgress(false);
    }

    @Override
    public void showAppList() {
        AppListActivity.start(LoginActivity.this);
        finish();
    }

    @Override
    public void showIncorrectPasswordError() {
        passwordView.setError(getString(R.string.error_incorrect_password));
        passwordView.requestFocus();
    }

    @Override
    public void showGenericError(String cause) {
        passwordView.setError(cause);
        passwordView.requestFocus();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

