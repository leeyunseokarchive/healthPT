package com.example.healthpt;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.healthpt.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


//TODO 25/05/04 장윤상
//1. 날려먹은거 복구하기 (난 너를 저주한다 맥북)
//2. 깃에 수정된거 푸쉬 및 커밋하기
//3. 홈화면 최대한 많이 구현해놓기

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // 또는 CalendarFragment()
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new CalendarFragment();
            }
            else if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }
            else if (item.getItemId() == R.id.nav_work){
                selectedFragment = new WorkFragment();
            }

            else if(item.getItemId() == R.id.nav_community){
                selectedFragment = new CommunityFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

    }

    // 화면 전환
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    //현재 인원 설정



    /*
    void loadFragment(HomeFragment());


    bottomNav.setOnItemSelectedListener(item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                selectedFragment = new HomeFragment();
                // 홈 화면 이동
                break;
            case R.id.nav_work:
                selectedFragment = new WorkFragment();
                // 운동 화면 이동
                break;
            case R.id.nav_calendar:
                selectedFragment = new CalendarFragment();
                // 캘린더 이동
                break;
            case R.id.nav_community:
                selectedFragment = new CommunityFragment();
                // 커뮤니티 이동
                break;
        }
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }

        return false;
    });

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


     */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}