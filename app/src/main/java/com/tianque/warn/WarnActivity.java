package com.tianque.warn;

import android.content.Context;
import android.content.Intent;

import com.tianque.warn.common.base.BaseActivity;

/**
 * @author ctrun on 2019/5/9.
 */

public class WarnActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, WarnActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.warn_activity_warn;
    }

    @Override
    protected void setupViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        
        setActionBarTitle("预警处置");
    }
}
