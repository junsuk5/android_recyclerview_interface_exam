package com.example.recyclerviewexam.countdown;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.recyclerviewexam.R;

public class CountDownActivity extends AppCompatActivity
        implements CountDownFragment.OnCountDownFragmentListener {

    private TextView mCountTextView;
    private CountDownFragment mCountDownFragment;
    private CountDownTask mCountDownTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        mCountTextView = findViewById(R.id.count_text_view);

        mCountDownFragment = new CountDownFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_countdown, mCountDownFragment)
                    .commit();
        }
    }

    @Override
    public void onStartButtonClicked() {
        mCountDownTask = new CountDownTask(new CountDownTask.CountTickListener() {
            @Override
            public void onTick(int count) {
                mCountDownFragment.setCount(count);
                mCountTextView.setText(count + "");
            }
        });
        mCountDownTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onInitButtonClicked() {
        mCountDownTask.cancel(true);
        mCountDownTask = null;
        mCountTextView.setText("0");
        mCountDownFragment.setCount(0);
    }

    static class CountDownTask extends AsyncTask<Void, Integer, Void> {

        interface CountTickListener {
            void onTick(int count);
        }

        private CountTickListener mListener;

        public CountDownTask(CountTickListener listener) {
            mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mListener.onTick(values[0]);
        }
    }
}
