package com.quickblox.content.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.quickblox.content.R;
import com.quickblox.content.helper.DataHolder;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.auth.result.QBSessionResult;
import com.quickblox.module.content.QBContent;
import com.quickblox.module.content.result.QBFilePagedResult;
import com.quickblox.module.users.model.QBUser;

public class SplashActivity extends Activity {

    private final int APP_ID = 774;
    private final String AUTH_KEY = "aY7WwSRmu2-GbfA";
    private final String AUTH_SECRET = "ah2NKY-YEqkqUkp";
    private final String USER_LOGIN = "test2";
    private final String USER_PASSWORD = "qwerty123";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // ================= QuickBlox ===== Step 1 =================
        // Initialize QuickBlox application with credentials.
        // Getting app credentials -- http://quickblox.com/developers/Getting_application_credentials
        QBSettings.getInstance().fastConfigInit(String.valueOf(APP_ID), AUTH_KEY, AUTH_SECRET);
        authorizeApp();

    }

    private void authorizeApp() {
        QBUser qbUser = new QBUser(USER_LOGIN, USER_PASSWORD);
        // authorize app with default user
        QBAuth.authorizeApp(qbUser, new QBCallback() {
            @Override
            public void onComplete(Result result) {
                if (result.isSuccess()) {
                    // return result from QBAuth.authorizeApp() query
                    QBSessionResult qbSessionResult = (QBSessionResult) result;
                    DataHolder.getDataHolder().setSignInUserId(qbSessionResult.getSession().getUserId());
                    getFileList();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onComplete(Result result, Object o) {
            }
        });
    }

    private void getFileList() {

        // get all files
        QBContent.getFiles(new QBCallback() {
            @Override
            public void onComplete(Result result) {
                QBFilePagedResult qbFilePagedResult = (QBFilePagedResult) result;
                DataHolder.getDataHolder().setQbFileList(qbFilePagedResult.getFiles());
                startGalleryActivity();
            }

            @Override
            public void onComplete(Result result, Object o) {
            }
        });
    }


    private void startGalleryActivity() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }

}