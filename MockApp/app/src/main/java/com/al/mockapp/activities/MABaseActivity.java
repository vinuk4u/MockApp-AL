package com.al.mockapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.al.mockapp.R;
import com.al.mockapp.utils.views.MAViewUtil;

/**
 * Created by vineeth on 31/03/16
 */
public class MABaseActivity extends AppCompatActivity {

    public void showIndefiniteSnackBar(View view, String errorMessage, View.OnClickListener onClickListener) {
        if (errorMessage == null) {
            errorMessage = getString(R.string.unknown_error);
        }

        MAViewUtil.showIndefiniteSnackBar(view, errorMessage, onClickListener);
    }
}
