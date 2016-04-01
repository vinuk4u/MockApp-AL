package com.al.mockapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.al.mockapp.fragments.MAStudentListFragment;
import com.al.mockapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentListActivity extends MABaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_layout);

        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(mToolbar);
    }
}
