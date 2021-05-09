package com.dristi.kharcha;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignupFragment extends Fragment {

    EditText username,password,email,confirmpass;

    RadioGroup gender;

    Button signup;

    SharedPreferences preferences;

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_signup,null);

        databaseHelper = new DatabaseHelper(getActivity());

        preferences = getActivity().getSharedPreferences("Userinfo",0);

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);
        signup = view.findViewById(R.id.signup);
        confirmpass=view.findViewById(R.id.confirmpass);

        (view.findViewById(R.id.signup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isUsernameEmpty(username) && isEmailValid(email)
                        &&isPasswordEmpty(password) && isConfirmPasswordEmpty(confirmpass)) {

                    String usernameVal = username.getText().toString();
                    String passwordVal = password.getText().toString();
                    String emailVal = email.getText().toString();
                    String confirmpassVal = confirmpass.getText().toString();

                    RadioButton checkedbtn = view.findViewById(gender.getCheckedRadioButtonId());
                    String genderVal = checkedbtn.getText().toString();

                    if (passwordVal.equals(confirmpassVal)) {

//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("username", usernameVal);
//                        editor.putString("password", passwordVal);
//                        editor.putString("email", emailVal);
//                        editor.putString("confirmpass", confirmpassVal);
//                        editor.putString("gender", genderVal);
//
//                        editor.apply();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put("username", usernameVal);
                        contentValues.put("password", passwordVal);
                        contentValues.put("email", emailVal);
                        contentValues.put("gender", genderVal);

                        databaseHelper.insertUser(contentValues);

                        Toast.makeText(getActivity(), "username:" + usernameVal, Toast.LENGTH_SHORT).show();

                    }
                    ((TabloginActivity)getActivity()).buttonsignup();
                }
            }
        });
        return view;
    }

    public boolean isUsernameEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            username.setError("This field cannot be empty");
            return false;
        }
    }


    public boolean isEmailValid(EditText view) {
        if (view.getText().toString().length() > 0) {
            String value = view.getText().toString();
            if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                return true;
            } else {
                view.setError("Enter Valid email");
                return false;
            }
        }
        return false;
    }

    public boolean isPasswordEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            password.setError("Enter Password");
            return false;
        }
    }

    public boolean isConfirmPasswordEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("Enter Confirm Password");
            //password.setError("Enter Password");
            return false;
        }
    }

    public boolean isSexChecked(RadioButton button) {
        if (button.isChecked()) {
            return true;
        } else {
            button.setError("Your sex");
            return false;
        }
    }
}

