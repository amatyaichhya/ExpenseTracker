package com.dristi.kharcha;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Navigation_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TimePicker timePicker;

    ViewPager viewPager;

    SharedPreferences preferences, preferences1;

    TextView username,email,ok,cancel;

    ImageView gender;

    String id;

    DatabaseHelper databaseHelper;

    MenuItem menuItem;

    ProgressDialog progressDialog;

    String dateval;

    GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth.AuthStateListener authStateListener;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mAuth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences("Userinfo",0);
        //preferences1 = getSharedPreferences("Date",0);

        //id = getIntent().getStringExtra("id");

        id = preferences.getString("userid","");

        databaseHelper = new DatabaseHelper(this);

        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.containermain);

        viewPager.setAdapter(new Navigation_drawer.mainpageadapter(getSupportFragmentManager()));

        alarmmanager();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(menuItem != null){
                    menuItem.setChecked(false);
                }
                else{
                    navigation.getMenu().getItem(0).setChecked(false);
                }

                Log.d("page",""+position);
                navigation.getMenu().getItem(position).setChecked(true);
                menuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        username = navigationView.getHeaderView(0).findViewById(R.id.username);
        email = navigationView.getHeaderView(0).findViewById(R.id.email);
        gender = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        username.setText(preferences.getString("username", null));
        email.setText(preferences.getString("email", null));

        String url = preferences.getString("gender", null);

        Picasso.with(this).load(url).into(gender);

        /*Userinfo info = databaseHelper.getuserinfo(id);
        username.setText(info.username);
        email.setText(info.email);*/

        /*String sex = preferences.getString("gender", null);

        Userinfo info = databaseHelper.getuserinfo(id);

        if (info.image != null) {
            gender.setImageBitmap(getBitmap(info.image));
        } else if (sex.equals("Male")) {
            gender.setImageResource(R.drawable.);
        } else if (sex.equals("Female")) {
            gender.setImageResource(R.drawable.);
        }
        //   }*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Intent intent = new Intent(Navigation_drawer.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void alarmmanager(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(),Alarmreceiver.class);

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 45);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.accounts:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.records:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.charts:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //displayuser();
    }

    public void displayuser()
    {
        Userinfo info = databaseHelper.getuserinfo(id);
        username.setText(info.username);
        email.setText(info.email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lendborrow){
            startActivity(new Intent(Navigation_drawer.this,Lend_Borrow.class));
        }
        else if (id == R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_drawer.this);

            builder.setTitle("                   Kharcha");
            builder.setMessage("Logout?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();

                    mGoogleSignInClient.signOut();

                    startActivity(new Intent(Navigation_drawer.this,LoginActivity.class));
                    finish();
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
        else if (id == R.id.nav_share){
            Intent myintent = new Intent(Intent.ACTION_SEND);
            myintent.setType("text/plain");
            String sharesub = "Kharcha";
            String sharebody = "Download this app to manage your money efficiently";
            myintent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
            myintent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(myintent,"Share using"));

        }
        else if (id == R.id.about){
            startActivity(new Intent(Navigation_drawer.this,About.class));
        }
        else if (id == R.id.feedback){
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"amatyaichhya@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }
        else if(id == R.id.budget){
            startActivity(new Intent(Navigation_drawer.this,Budget.class));
        }

        else if(id == R.id.predict){

            if(databaseHelper.getTotalPredict()<=0){
                dateval = databaseHelper.getstartdate();
            }

            Toast.makeText(Navigation_drawer.this,dateval,Toast.LENGTH_SHORT).show();
            new LoginAsyncTask().execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class mainpageadapter extends FragmentPagerAdapter
    {

        public mainpageadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            if(position == 0){
                return new Accounts();
            }
            else if(position == 1){
                return new Records();
            }
            return new ChartFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void showCustomdialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Set a Reminder");

        View view = LayoutInflater.from(this).inflate(R.layout.activity_reminder,null);

        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);

        timePicker.setIs24HourView(false); // used to display AM/PM mode

        // perform set on time changed listener event
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minutes = mcurrentTime.get(Calendar.MINUTE);

                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + " - " + minute, Toast.LENGTH_SHORT).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String timevalue = timePicker.getHour()+"-"+timePicker.getMinute();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(Navigation_drawer.this);
            progressDialog.setMessage("Calculating....");
            progressDialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
            try{
                Thread.sleep(4000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing())
                predictedvaluedialog();
                progressDialog.dismiss();

            super.onPostExecute(result);
        }

    }

    public void predictedvaluedialog(){

        final Dialog dialog = new Dialog(Navigation_drawer.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setTitle("Add Expense");

        View view = LayoutInflater.from(Navigation_drawer.this).inflate(R.layout.prediction_dialog,null);

        addData();

        TextView predicted = view.findViewById(R.id.value);

        predicted.setText(String.valueOf(predict()));

        dialog.setContentView(view);
        dialog.show();
    }

    public double predict(){

        int value;

        if(databaseHelper.getTotalPredict()<=0){
            value = (int) ((11.037829 * 1 + 1994.672766));
        }
        else{
            double[] y = new double[52];
            double[] x = new double[52];

            double n_pt = databaseHelper.getTotalPredict();

            for(int a = 0; a<n_pt; a++){
                y[a] = databaseHelper.get_total_train(a+1);
            }

            for(int a = 0; a<n_pt; a++){
                x[a] = a+1;
            }

            double s_slope = 0, s_intercept = 0, int_slope, int_intercept, final_slope = 0, final_intercept = 0;
            double l_rate = 0.001;
            int i,j;

            for(j=0;j<2000;j++)
            {
                int_slope = 0;
                int_intercept = 0;

                for(i=0;i<n_pt;i++)
                {
                    int_intercept += - (2/n_pt) * (y[i] - ((s_slope * x[i]) + s_intercept));
                    int_slope += - (2/n_pt) * x[i] * (y[i] - ((s_slope * x[i]) + s_intercept));
                    final_slope = s_slope - (l_rate * int_slope);
                    final_intercept = s_intercept - (l_rate * int_intercept);
                }
                s_slope = final_slope;
                s_intercept = final_intercept;
            }

            value = (int) ((s_slope * (n_pt + 1)) + s_intercept);
        }

//        double x[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52};
//        double y[] = {2500,3000,2800,1000,2000,5000,1000,1500,1500,2500,1200,4000,3210,2087,2900,6750,756,1156,3150,1100,934,300,756,2500,208,743,1590,2945,950,1007,4000,543,8500,2578,1330,200,2160,1140,650,4200,6500,345,534,1000,2056,1000,2250,860,1000,800,1530,990};

        return value;
    }

    public void addData(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());

        if(date.equals(getnewdate(getDate()))){
            setDate(getnewdate(getDate()));
            int total = databaseHelper.predictiontotal(dateval);

            ContentValues contentValues = new ContentValues();
            contentValues.put("amount",total);

            databaseHelper.insertPredictVal(contentValues);
        }
    }

    public String getnewdate(String date){

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try{
            calendar.setTime(df1.parse(date));
        }
        catch (ParseException e){
            e.printStackTrace();
        }

        calendar.add(Calendar.DATE,7);
        String newdate = df1.format(calendar.getTime());

        return newdate;
    }

    public String getDate() {
        return dateval;
    }

    public void setDate(String datevalue){
        dateval = datevalue;
    }

//    public void refreshlist(){
//
//        Records records = new Records();
//        records.refresh();
//
//    }


}
