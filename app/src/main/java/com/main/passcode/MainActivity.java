package com.main.passcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       authenticateApp();
        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals("1234")) {
                        Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this,Success.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);

                    }
                }
            });
        }
    }

    private void authenticateApp() {
        //Get the instance of KeyGuardManager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            //  Intent i = keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock), getResources().getString(R.string.confirm_pattern));
            Intent i = keyguardManager.createConfirmDeviceCredentialIntent("password","1234");
            try {
                //Start activity for result
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {

                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                try {

                    //Start activity for result
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception ex) {

                    //If app is unable to find any Security settings then user has to set screen lock manually

                    Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCK_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //If screen lock authentication is success update text
                    Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_LONG).show();
                } else {
                    //If screen lock authentication is failed update text
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();

                }
                break;
            case SECURITY_SETTING_REQUEST_CODE:
                //When user is enabled Security settings then we don't get any kind of RESULT_OK
                //So we need to check whether device has enabled screen lock or not
                if (isDeviceSecure()) {
                    //If screen lock enabled show toast and start intent to authenticate user
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                    authenticateApp();
                } else {
                    //If screen lock is not enabled just update text
                    Toast.makeText(this, "Not done", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * method to return whether device has screen lock enabled or not
     **/
    private boolean isDeviceSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //this method only work whose api level is greater than or equal to Jelly_Bean (16)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure();

        //You can also use keyguardManager.isDeviceSecure(); but it requires API Level 23

    }

    //On Click of button do authentication again
    public void authenticateAgain(View view) {
        authenticateApp();
    }

}
//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (!isPass()) {
//            Intent intent = new Intent(this, Lockscreen.class);
//            startActivity(intent);
//        } else {
//            textView.setText("The code you typed is right!");
//        }
//    }
//
//    private boolean isPass() {
//        SharedPreferences prefs = getSharedPreferences("PASS_CODE", MODE_PRIVATE);
//        return prefs.getBoolean("is_pass", false);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences.Editor editor = getSharedPreferences("PASS_CODE", MODE_PRIVATE).edit();
//        editor.putBoolean("is_pass", false);
//        editor.apply();
//    }
