package com.example.term_project.activity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.term_project.R;
import com.google.android.material.button.MaterialButton;

public class AboutActivity extends BaseActivity {

    private static final String TAG = "AboutActivity";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView ivAboutLogo = findViewById(R.id.ivAboutLogo);
        MaterialButton btnBackToMain = findViewById(R.id.btnBackToMain);

        ivAboutLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        btnBackToMain.setOnClickListener(v -> finish());
    }
}
