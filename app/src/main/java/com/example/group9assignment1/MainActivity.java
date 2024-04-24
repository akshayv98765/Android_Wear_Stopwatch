package com.example.group9assignment1;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.group9assignment1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // ViewBinding object

    private boolean isTimerRunning = false;
    private long startTime = 0L;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide(); // Hide the action bar

        binding.elapsedTimeTextView.setText("00:00:00"); // Set initial text in the TextView

        // Set background and initial state for buttons
        binding.startButton.setBackgroundResource(R.drawable.enabled_rounded_button);
        binding.stopButton.setBackgroundResource(R.drawable.disabled_rounded_button);
        binding.resetButton.setBackgroundResource(R.drawable.disabled_rounded_button);

        // Set click listeners for buttons
        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        resetTimer(); // Set the initial state
    }

    private void startTimer() {
        if (!isTimerRunning) {
            if (startTime == 0L) {
                // If it's the first start, set the initial startTime
                startTime = SystemClock.elapsedRealtime();
            } else {
                // If resuming from pause, add the paused time to the start time
                long pausedTime = SystemClock.elapsedRealtime() - pausedTimeMillis;
                startTime += pausedTime;
            }

            isTimerRunning = true;
            updateTimer();

            // Enable / disable and change appearance of buttons
            binding.startButton.setEnabled(false);
            binding.startButton.setBackgroundResource(R.drawable.disabled_rounded_button);
            binding.stopButton.setEnabled(true);
            binding.stopButton.setBackgroundResource(R.drawable.enabled_rounded_button);
            binding.resetButton.setEnabled(false);
            binding.resetButton.setBackgroundResource(R.drawable.disabled_rounded_button);
        }
    }

    private long pausedTimeMillis = 0L;

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            handler.removeCallbacks(runnable);
            pausedTimeMillis = SystemClock.elapsedRealtime();

            // Enable / disable and change appearance of buttons
            binding.startButton.setEnabled(true);
            binding.startButton.setBackgroundResource(R.drawable.enabled_rounded_button);
            binding.stopButton.setEnabled(false);
            binding.stopButton.setBackgroundResource(R.drawable.disabled_rounded_button);
            binding.resetButton.setEnabled(true);
            binding.resetButton.setBackgroundResource(R.drawable.enabled_rounded_button);
        }
    }

    private void resetTimer() {
        stopTimer();
        startTime = 0L;
        binding.elapsedTimeTextView.setText("00:00:00");

        // Enable / disable and change appearance of buttons
        binding.startButton.setEnabled(true);
        binding.startButton.setBackgroundResource(R.drawable.enabled_rounded_button);
        binding.stopButton.setEnabled(false);
        binding.stopButton.setBackgroundResource(R.drawable.disabled_rounded_button);
        binding.resetButton.setEnabled(false);
        binding.resetButton.setBackgroundResource(R.drawable.disabled_rounded_button);
    }

    private void updateTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = SystemClock.elapsedRealtime() - startTime;

                int seconds = (int) (elapsedTime / 1000);
                int minutes = seconds / 60;
                int hours = minutes / 60;

                seconds = seconds % 60;
                minutes = minutes % 60;

                // Format the time as a string and set it in the TextView
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                binding.elapsedTimeTextView.setText(time);

                if (isTimerRunning) {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(runnable, 0);
    }
}