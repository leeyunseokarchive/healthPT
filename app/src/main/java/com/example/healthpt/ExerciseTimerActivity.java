package com.example.healthpt;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseTimerActivity extends AppCompatActivity implements SensorEventListener {

    private TextView roundText, timerText, statusText;
    private Button startButton;
    private ImageButton backButton;
    private View timerLayout;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 30000;

    private int round = 1;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 12.0f;

    // 움직임 없을 때 UI 숨기기 위한 핸들러와 Runnable
    private Handler handler = new Handler();
    private Runnable hideUITask = new Runnable() {
        @Override
        public void run() {
            timerLayout.setVisibility(View.INVISIBLE);
            statusText.setText("대기중");
        }
    };

    private static final long UI_HIDE_DELAY = 3000; // 3초 후 UI 숨김, 이건 좀 수정할 필요 있을듯 by 장윤상

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_timer);

        roundText = findViewById(R.id.roundText);
        timerText = findViewById(R.id.timerText);
        statusText = findViewById(R.id.statusText);
        startButton = findViewById(R.id.startButton);
        backButton = findViewById(R.id.backButton);
        timerLayout = findViewById(R.id.timerLayout);

        startButton.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        backButton.setOnClickListener(v -> finish());

        updateCountDownText();
        updateRoundText();

        // 초기에는 검은 화면(타이머 UI 숨김)
        timerLayout.setVisibility(View.INVISIBLE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(hideUITask);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration = Math.sqrt(x*x + y*y + z*z) - SensorManager.GRAVITY_EARTH;

        if(acceleration > SHAKE_THRESHOLD) {
            // 움직임 감지 → 타이머 UI 보여주기
            timerLayout.setVisibility(View.VISIBLE);

            // UI 숨김 딜레이 리셋 (계속 움직임 감지시 UI 계속 유지)
            handler.removeCallbacks(hideUITask);
            handler.postDelayed(hideUITask, UI_HIDE_DELAY);

            if(!timerRunning) {
                startTimer();
            }
        }
        // 움직임 없으면 바로 UI 숨기면 너무 갑자기 꺼져서
        // 딜레이 후 숨김 처리하는 방식 사용
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("시작");
                round++;
                updateRoundText();
                timeLeftInMillis = 30000;
                statusText.setText("휴식 중!");
            }
        }.start();

        timerRunning = true;
        startButton.setText("일시정지");
        statusText.setText("운동 중!");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("시작");
        statusText.setText("일시정지");
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerText.setText(String.format("%d초", seconds));
    }

    private void updateRoundText() {
        roundText.setText(String.format("%d라운드", round));
    }
}
