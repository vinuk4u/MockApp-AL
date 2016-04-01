package com.al.mockapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.al.mockapp.R;
import com.al.mockapp.adapters.MAMarkAdapter;
import com.al.mockapp.controllers.MAAppController;
import com.al.mockapp.holders.MAStudentHolder;
import com.al.mockapp.models.MAStudentModel;
import com.al.mockapp.utils.MAFormatUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentDetailFragment extends Fragment {

    @Bind(R.id.item_profile_pic_imageview)
    NetworkImageView mProfilePicNetworkImageView;
    @Bind(R.id.item_rollno_textview)
    TextView mRollNoTextView;
    @Bind(R.id.item_name_textview)
    TextView mNameTextView;
    @Bind(R.id.marks_recyclerview)
    RecyclerView mMarksRecyclerView;
    private MAStudentModel mStudentModel = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public MAStudentDetailFragment() {
        mStudentModel = MAStudentHolder.getInstance().getStudentModel();

        mRequestQueue = MAAppController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static MAStudentDetailFragment newInstance() {
        return new MAStudentDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_detail_layout, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadStudentDetail();
    }

    private void loadStudentDetail() {
        if (mStudentModel.getProfilePic() != null) {
            mProfilePicNetworkImageView.setImageUrl(MAFormatUtil.getImageUrl(mStudentModel.getProfilePic()), mImageLoader);
        }

        final String rollNo = getString(R.string.roll_no) + mStudentModel.getRollNo();
        mRollNoTextView.setText(rollNo);

        final String name = mStudentModel.getfName() + " " + mStudentModel.getlName();
        mNameTextView.setText(name);

        mMarksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final MAMarkAdapter mHeaderItemsAdapter = new MAMarkAdapter(mStudentModel.getMarks());
        mMarksRecyclerView.setAdapter(mHeaderItemsAdapter);
    }
}
