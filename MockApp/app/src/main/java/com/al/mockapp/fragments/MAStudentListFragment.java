package com.al.mockapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.al.mockapp.R;
import com.al.mockapp.activities.MAStudentDetailActivity;
import com.al.mockapp.activities.MAStudentListActivity;
import com.al.mockapp.adapters.MAStudentListAdapter;
import com.al.mockapp.adapters.MAStudentListSimpleSectionAdapter;
import com.al.mockapp.controllers.MAAppController;
import com.al.mockapp.holders.MAStudentHolder;
import com.al.mockapp.models.MAStudentModel;
import com.al.mockapp.models.response.MAResponseArrayModel;
import com.al.mockapp.utils.MANetworkUtil;
import com.al.mockapp.utils.helpers.MAVolleyErrorHelper;
import com.al.mockapp.utils.views.MAViewUtil;
import com.al.mockapp.webservice.MAGsonRequest;
import com.al.mockapp.webservice.constants.MANetworkConstants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentListFragment extends Fragment implements Response.ErrorListener,
        Response.Listener<MAResponseArrayModel>, SearchView.OnQueryTextListener {

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.students_searchview)
    SearchView mStudentsSearchView;

    @Bind(android.R.id.list)
    ListView mStudentListView;

    @Bind(android.R.id.empty)
    TextView mEmptyView;

    private RequestQueue mRequestQueue;
    private MAGsonRequest<MAResponseArrayModel> mGsonRequest;

    private MAStudentListAdapter mStudentsAdapter;
    private MAStudentListSimpleSectionAdapter<MAStudentModel> mStudentsSectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_list_layout, container, false);

        ButterKnife.bind(this, rootView);

        setupSwipeRefreshLayout();

        mStudentListView.setEmptyView(mEmptyView);
        mStudentListView.setTextFilterEnabled(true);

        setupSearchView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fetchStudentList();
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setRefreshing(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchStudentList();
            }
        });
    }

    private void setupSearchView() {
        mStudentsSearchView.setIconifiedByDefault(false);
        mStudentsSearchView.setOnQueryTextListener(this);
        mStudentsSearchView.setSubmitButtonEnabled(true);
        mStudentsSearchView.setQueryHint(getString(R.string.search_student));

        hideKeyboard();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mStudentsSearchView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void fetchStudentList() {
        mSwipeRefreshLayout.setRefreshing(false);
        MAViewUtil.dismissSnackBar();

        if (!MANetworkUtil.hasInternetAccess(getActivity())) {
            showJsonResponseErrorSnackbar(null, getString(R.string.no_internet_connection_available));
            return;
        }

        mRequestQueue = MAAppController.getInstance().getRequestQueue();

        MAViewUtil.showProgressDialog(getActivity(),
                null, getString(R.string.downloading), new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mGsonRequest != null) {
                            mRequestQueue.cancelAll(mGsonRequest);
                            mGsonRequest.cancel();
                            dialog.dismiss();
                        }
                    }
                });

        final String url = MANetworkConstants.STUDENT_DATA_URL;
        mGsonRequest = new MAGsonRequest<>(Request.Method.GET, url, this, this);

        mRequestQueue.add(mGsonRequest);
    }

    private void showJsonResponseErrorSnackbar(final View view, final String message) {
        ((MAStudentListActivity) getActivity()).showIndefiniteSnackBar(
                view != null ? view : mStudentListView, message,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchStudentList();
                    }
                });
    }

    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        MAViewUtil.dismissProgressDialog();
        showJsonResponseErrorSnackbar(null, MAVolleyErrorHelper.getMessage(error, getActivity()));
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(MAResponseArrayModel response) {
        MAViewUtil.dismissProgressDialog();

        if (response != null) {
            setStudentList(response.getResponseModels());
        } else {
            showJsonResponseErrorSnackbar(null, getString(R.string.unknown_error));
        }
    }

    private void setStudentList(final ArrayList<MAStudentModel> studentList) {
        if (studentList != null && studentList.size() > 0) {
            mStudentListView.setFastScrollEnabled(true);
            mStudentListView.setFastScrollAlwaysVisible(true);

            // Initialize Custom Adapter
            mStudentsAdapter = new MAStudentListAdapter(this.getActivity(),
                    R.layout.fragment_student_list_layout, studentList);

            // Initialize SimpleSectionAdapter and add custom adapter and
            // sectionizer class
            //MAStudentListSectionizer sectionizer = new MAStudentListSectionizer();
            mStudentsSectionAdapter = new MAStudentListSimpleSectionAdapter<>(
                    getActivity(), mStudentsAdapter, R.layout.student_list_section_layout,
                    R.id.item_student_section_title, new MAStudentListSectionizer());

            // set that mStudentsSectionAdapter to listview
            mStudentListView.setAdapter(mStudentsSectionAdapter);

            mStudentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int index = mStudentsSectionAdapter.getIndexForPosition(position);
                    MAStudentHolder.getInstance().setStudentModel(mStudentsAdapter.getItem(index));
                    //studentList.get(index));

                    Intent intent = new Intent(getActivity(), MAStudentDetailActivity.class);
                    Bundle translateBundle = ActivityOptionsCompat
                            .makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight())
                            .toBundle();

                    ActivityCompat.startActivity(getActivity(), intent, translateBundle);
                }
            });
        } else {
            showJsonResponseErrorSnackbar(null, getString(R.string.no_results));
        }
        hideKeyboard();
    }

    @Override
    public boolean onQueryTextSubmit(String queryText) {
        if (TextUtils.isEmpty(queryText)) {
            mStudentListView.clearTextFilter();
        } else {
            mStudentListView.setFilterText(queryText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        if (TextUtils.isEmpty(queryText)) {
            mStudentListView.clearTextFilter();
        } else {
            mStudentListView.setFilterText(queryText);
        }
        return true;
    }

    /*
     * Holder for the custom adapter.
	 */
    class ViewHolder {
        protected TextView studentsSectionTextView;

        ViewHolder(View view) {
            studentsSectionTextView = (TextView) view.findViewById(R.id.item_student_section_title);
        }
    }

    // Defining Custom Sectionizer class which can added to the mStudentsSectionAdapter
    class MAStudentListSectionizer implements com.al.mockapp.adapters.MAStudentListSectionizer<MAStudentModel> {
        private int mSelectedPosition = 0;

        @Override
        public String getSectionTitleForItem(MAStudentModel student) {
            if (student.getfName().toString().length() > 0) {
                return student.getfName().substring(0, 1).toUpperCase();
            } else {
                return "";
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder viewHolder = null;

            if (view == null) {
                view = View.inflate(getActivity(),
                        R.layout.student_list_section_layout, null);

                viewHolder = new ViewHolder(view);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view
                        .getTag();
            }

            if (mSelectedPosition == position) {
                view.setBackgroundColor(getActivity().getResources().getColor(
                        R.color.colorAccent));
            } else {
                view.setBackgroundColor(getActivity().getResources().getColor(
                        android.R.color.white));
            }

            return view;
        }
    }

}
