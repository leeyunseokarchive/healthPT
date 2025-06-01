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

    private static final float MOVEMENT_THRESHOLD = 1.0f; // 작게 설정해도 손 움직임 감지 가능
    private static final long HOLD_TIMEOUT = 10000; // 10초 지나면 UI 숨김
    private long lastMovementTime = 0;

    private Handler handler = new Handler();
    private Runnable hideUITask = () -> {
        if (System.currentTimeMillis() - lastMovementTime > HOLD_TIMEOUT) {
            timerLayout.setVisibility(View.INVISIBLE);
            statusText.setText("대기중");
        }
    };

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

        // 처음엔 UI 숨김
        timerLayout.setVisibility(View.INVISIBLE);

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
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

        double acceleration = Math.sqrt(x * x + y * y + z * z);
        double delta = Math.abs(acceleration - SensorManager.GRAVITY_EARTH);

        if (delta > MOVEMENT_THRESHOLD) {
            lastMovementTime = System.currentTimeMillis();

            // UI 켜기
            timerLayout.setVisibility(View.VISIBLE);
            statusText.setText(timerRunning ? "운동 중!" : "대기중");

            // UI 숨김 예약 갱신
            handler.removeCallbacks(hideUITask);
            handler.postDelayed(hideUITask, HOLD_TIMEOUT);

            // 타이머 자동 시작
            if (!timerRunning) {
                startTimer();
            }
        }
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
