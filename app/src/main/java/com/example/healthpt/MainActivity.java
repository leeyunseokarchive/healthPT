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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // 현재 헬스장 인원 표시
    private TextView peopleCountText;
    private ImageView gymcomplexImageView;
    // 실시간 인원 현황(데이터 연동 필요)
    private int currentCount = 70;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        peopleCountText = findViewById(R.id.peopleCountText);
        gymcomplexImageView = findViewById(R.id.gymcomplexView);

        setPeopleCount(currentCount);
        updateUIWithCount(currentCount);

        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // 또는 CalendarFragment()
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new CalendarFragment();
            } else if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
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
    private void setPeopleCount(int count) {
        currentCount = count;
        peopleCountText.setText(currentCount + "/150명");
    }

    private void updateUIWithCount(int count) {

        if (count <= 33) {
            gymcomplexImageView.setImageResource(R.drawable.free);
        } else if (count <= 66) {
            gymcomplexImageView.setImageResource(R.drawable.common);
        } else if (count <= 100) {
            gymcomplexImageView.setImageResource(R.drawable.complex);
        }
    }



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