package com.example.dmikhov.systemalertwindowtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int WINDOW_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("madlog", "Settings.canDrawOverlays(this): " + Settings.canDrawOverlays(this));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                startGarland();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, WINDOW_PERMISSION_REQUEST_CODE);
            }
        }

    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        Log.d("madlog", "onActivityResult");
        if (requestCode == WINDOW_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Log.d("madlog", "StartService...");
                    startGarland();
                } else {
                    Log.d("madlog", "Permission is not granted!");
                    Toast.makeText(MainActivity.this, "Permission is not granted!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void startGarland() {
        startService(new Intent(this, HeaderService.class));
        finish();
    }
}
