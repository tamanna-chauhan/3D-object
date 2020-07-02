package com.example.threedactivity.threeD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ThreeDActivity extends AppCompatActivity implements MyGLSurfaceView.Listener {

    private MyGLSurfaceView mGLView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.


        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

    }

    @Override
    public void taskCompleted() {


        try{
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            finish();

        } catch (Exception e) {

            Intent i = new Intent();
            setResult(Activity.RESULT_CANCELED, i);
            finish();

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent();
        setResult(Activity.RESULT_CANCELED,i);
    }
}

