package com.jailbird.scorch;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_intro);
        Log.d("intro", "intro");

        addSlide(new SlideFragmentBuilder()
        .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.newfire)

                .title(getString(R.string.welcome))
        .build()
        );
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .image(R.drawable.p2)
                .title(getString(R.string.choose))
                .description(getString(R.string.choose_desc))
                .build()
        );
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.friendshot)
                .title(getString(R.string.check_friend))
                .description(getString(R.string.friend_desc))
                .build()
        );
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimary)
                        .buttonsColor(R.color.colorAccent)
                        .possiblePermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .neededPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .image(R.drawable.p3)
                        .title(getString(R.string.perms))
                        .description(getString(R.string.perms_loc))
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(getString(R.string.hot));
                    }
                }, getString(R.string.grant)));
        setSkipButtonVisible();

    }

    @Override
    public void onFinish() {
        super.onFinish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}
