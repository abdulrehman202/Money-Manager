package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class FirstScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer_Layout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView textView;
    int userid;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        try {
            Intent i = getIntent();
            String name = i.getStringExtra("Name");
            String email = i.getStringExtra("Email");
            userid = i.getIntExtra("ID",-1);

            final Fragment fragment = new main();
            bundle = new Bundle();
            bundle.putInt("ID",userid);
            fragment.setArguments(bundle);

            switch_fragment(fragment);

            navigationView = findViewById(R.id.nv);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.txtName);
            TextView navEmail = headerView.findViewById(R.id.txt_nav_email);
            navUsername.setText(name);
            navEmail.setText(email);

        bottomNavigationView = findViewById(R.id.bottom_navigationView);
        textView = findViewById(R.id.textview1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                textView.setText(menuItem.getTitle());

                bundle = new Bundle();
                bundle.putInt("ID",userid);

                switch (menuItem.getItemId()) {

                    case R.id.action_add: {
                        Fragment fragment = new add_expense();
                        fragment.setArguments(bundle);
                        switch_fragment(fragment);
                        break;
                    }

                    case R.id.action_home:{
                        try {
                            Fragment fragment1 = new main();
                            fragment1.setArguments(bundle);
                            switch_fragment(fragment1);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }

                    case R.id.action_Recent:{
                        Fragment fragment1 = new ShowAllTransactions();
                        fragment1.setArguments(bundle);
                        switch_fragment(fragment1);
                        break;
                    }
                    default:
                        {

                        }
                }

                return true;
            }
        });

            toolbar = findViewById(R.id.toolbar_action);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            drawer_Layout = findViewById(R.id.drawerlayout);
            mToggle = new ActionBarDrawerToggle(this, drawer_Layout, R.string.Open, R.string.Close);
            drawer_Layout.addDrawerListener(mToggle);
            mToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            navigationView.setNavigationItemSelectedListener(this);

            Menu menu = navigationView.getMenu();

            setColor(menu.findItem(R.id.account));
            setColor(menu.findItem(R.id.contact_us));
            setColor(menu.findItem(R.id.rate_us));
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    void setColor(MenuItem tools)
    {
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer_Layout.isDrawerOpen(navigationView)){
            drawer_Layout.closeDrawer(navigationView);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            final Fragment fragment = new main();
            Bundle bundle = new Bundle();
            bundle.putInt("ID",userid);
            fragment.setArguments(bundle);
            switch_fragment(fragment);
            textView.setText("Home");
        }
        else {
            super.onBackPressed();
        }
    }

    private void showSignoutMessage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstScreen.this);
        builder.setCancelable(true);
        builder.setTitle("Sign Out");
        builder.setMessage("Do you want to Sign Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent();
                intent1.putExtra("do","signout");
                setResult(AppCompatActivity.RESULT_OK,intent1);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        textView.setText(menuItem.getTitle());
        bundle = new Bundle();
        bundle.putInt("ID",userid);
        Fragment fragment;

        switch (menuItem.getItemId()) {
            case R.id.account:
            {
                try {
                    fragment = new AccountInfo();
                    fragment.setArguments(bundle);
                    switch_fragment(fragment);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            break;
            }

            case R.id.change_password:
            {
                fragment = new ChangePassword();
                fragment.setArguments(bundle);
                switch_fragment(fragment);
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawerlayout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void switch_fragment(Fragment fragment)
    {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().addToBackStack(fragment.getClass().getName()).replace(R.id.frame_to_replace,fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId())
        {
        case R.id.signout_icon:{
            showSignoutMessage();
            break;
        }
        }

        return super.onOptionsItemSelected(item);
    }

}
