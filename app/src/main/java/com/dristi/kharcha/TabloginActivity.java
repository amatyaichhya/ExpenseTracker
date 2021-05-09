package com.dristi.kharcha;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TabloginActivity extends AppCompatActivity {

    TextView signin,signup;

    FrameLayout container;

    SigninFragment signinFragment;

    SignupFragment signupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablogin);

        signupFragment= new SignupFragment();
        signinFragment= new SigninFragment();

        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);

        signin.setBackgroundResource(R.drawable.layerlistbg1);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,signinFragment).commit();
    }

    public void tabclick(View view)
    {
        signin.setBackgroundColor(Color.TRANSPARENT);
        signup.setBackgroundColor(Color.TRANSPARENT);
        if(view.getId()==R.id.signin)
        {
            signin.setBackgroundResource(R.drawable.layerlistbg1);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,signinFragment).commit();
        }
        else{
            signup.setBackgroundResource(R.drawable.layerlistbg);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,signupFragment).commit();
        }
    }

    public void buttonsignup(){
        signup.setBackgroundColor(Color.TRANSPARENT);
        signin.setBackgroundResource(R.drawable.layerlistbg1);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,signinFragment).commit();
    }
}
