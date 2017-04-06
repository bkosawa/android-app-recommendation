package br.com.kosawalabs.apprecommendation.presentation.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.LoginDataRepository;
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;
import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.LoginNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;
import br.com.kosawalabs.apprecommendation.presentation.list.AppListActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String EXTRA_LOGOUT = "br.com.kosawalabs.apprecommendation.EXTRA_LOGOUT";
    private TokenDataRepository tokenRepository;
    private LoginDataRepository loginRepository;

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
        tokenRepository = new TokenDiskRepository(this);
        loginRepository = new LoginNetworkRepository();

        if (getIntent() != null && getIntent().hasExtra(EXTRA_LOGOUT)) {
            tokenRepository.removeToken();
        }
    }

    private void bindView() {
        setContentView(R.layout.activity_login);
        usernameView = (AutoCompleteTextView) findViewById(R.id.username);

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void login() {
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        if (areFieldsValid(username, password)) {
            performLogin(username, password);
        }
    }

    private boolean areFieldsValid(String username, String password) {
        usernameView.setError(null);
        passwordView.setError(null);

        boolean areValid = true;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            areValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            focusView = passwordView;
            areValid = false;
        }

        if (!areValid && focusView != null) {
            focusView.requestFocus();
        }
        return areValid;
    }

    private void performLogin(String username, String password) {
        showProgress(true);
        loginRepository.login(username, password, new DataCallback<SessionToken>() {
            @Override
            public void onSuccess(SessionToken result) {
                showProgress(false);
                if (!TextUtils.isEmpty(result.getToken())) {
                    tokenRepository.putToken(result.getToken());
                    AppListActivity.start(LoginActivity.this);
                    finish();
                } else {
                    onError(new DataError("Empty Token"));
                }
            }

            @Override
            public void onError(DataError error) {
                showProgress(false);
                if (error.getCause().equals("Error Status: 400")) {
                    passwordView.setError(getString(R.string.error_incorrect_password));
                    passwordView.requestFocus();
                } else {
                    passwordView.setError(error.getCause());
                    passwordView.requestFocus();
                }
            }
        });
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
}

