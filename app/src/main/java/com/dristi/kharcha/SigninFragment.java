package com.dristi.kharcha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SigninFragment extends Fragment {

    EditText username,password;

    Button login,google;

    CheckBox rememberme;

    SharedPreferences preferences, preferences1;

    DatabaseHelper databaseHelper;

    FirebaseAuth mAuth;

    private final static int RC_SIGN_IN = 2;

    GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth.AuthStateListener authStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_login,null);

        mAuth = FirebaseAuth.getInstance();

        preferences = getActivity().getSharedPreferences("Userinfo",0);
        preferences1 = getActivity().getSharedPreferences("Date",0);

        databaseHelper=new DatabaseHelper(getActivity());

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        google = view.findViewById(R.id.google);
        rememberme = view.findViewById(R.id.rememberme);

        if (preferences.getBoolean("rememberme",false))
        {
            Intent intent = new Intent(getActivity(),Navigation_drawer.class);
            startActivity(intent);
            getActivity().finish();
        }

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameVal = username.getText().toString();
                String passwordVal = password.getText().toString();

                Log.i("check","username:" + usernameVal);

                if(databaseHelper.isloginsuccessful(usernameVal,passwordVal))
                {
                    Intent intent = new Intent(getActivity(),Navigation_drawer.class);
                    startActivity(intent);

                    if (rememberme.isChecked()) {
                        preferences.edit().putBoolean("rememberme", true).apply();
                    }
                    preferences.edit().putString("userid", "" +databaseHelper.getUserId(usernameVal, passwordVal)).apply();

                    Toast.makeText(getActivity(), "successfully logged in!!", Toast.LENGTH_SHORT).show();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String date = df.format(Calendar.getInstance().getTime());

                    preferences1.edit().putString("date",date).apply();

                    getActivity().finish();

                }
                else
                {
                    Toast.makeText(getActivity(), "login failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent intent = new Intent(getActivity(),Navigation_drawer.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        };

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(authStateListener);
        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account!=null){
                    firebaseAuthWithGoogle(account);
                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getActivity(), "Google sign in failed", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(getActivity(),Navigation_drawer.class);
                            startActivity(intent);
//
//                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                            String date = df.format(Calendar.getInstance().getTime());
//
//                            preferences1.edit().putString("date",date).apply();

                            getActivity().finish();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authenticaation Failed", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if(user!=null){

            String photo = String.valueOf(user.getPhotoUrl());

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", user.getDisplayName());
            editor.putString("email", user.getEmail());
            editor.putString("gender", photo);

            editor.apply();

        }
    }

}
