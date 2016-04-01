package com.al.mockapp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.al.mockapp.R;
import com.al.mockapp.holders.MAStudentHolder;
import com.al.mockapp.models.MAStudentModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentDetailActivity extends MABaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_layout);

        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(mToolbar);

        final MAStudentModel studentModel = MAStudentHolder.getInstance().getStudentModel();
        if (studentModel != null) {
            final String name = studentModel.getfName() + " " + studentModel.getlName();
            getSupportActionBar().setTitle(name);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Handle Back Navigation
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MAStudentDetailActivity.this.onBackPressed();
            }
        });
    }
}
