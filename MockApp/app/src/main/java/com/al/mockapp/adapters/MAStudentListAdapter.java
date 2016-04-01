package com.al.mockapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.al.mockapp.R;
import com.al.mockapp.controllers.MAAppController;
import com.al.mockapp.models.MAStudentModel;
import com.al.mockapp.utils.MAFormatUtil;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentListAdapter extends ArrayAdapter<MAStudentModel> implements Filterable {

    private List<MAStudentModel> studentsSource;
    private Context mContext;
    private List<MAStudentModel> mStudents;
    private int mSelectedPosition = -1;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public MAStudentListAdapter(Context context, int nameTextViewResourceId,
                                List<MAStudentModel> students) {
        super(context, nameTextViewResourceId, students);

        this.mContext = context;
        this.mStudents = students;

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

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) { // if it's not recycled, initialize some
            // attributes
            view = View.inflate(mContext, R.layout.item_student_layout, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mProfilePicNetworkImageView.setTag(position);
        viewHolder.mRollNoTextView.setTag(position);
        viewHolder.mNameTextView.setTag(position);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.mProfilePicNetworkImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_contact_picture));
        } else {
            viewHolder.mProfilePicNetworkImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_contact_picture));
        }
        if (mStudents != null && position < mStudents.size()) {
            if (mStudents.get(position).getProfilePic() != null) {
                viewHolder.mProfilePicNetworkImageView.setImageUrl(
                        MAFormatUtil.getImageUrl(
                                mStudents.get((Integer) viewHolder.mProfilePicNetworkImageView.getTag()).getProfilePic()), mImageLoader);
            }

            viewHolder.mRollNoTextView.setText(mStudents.get(position).getRollNo());

            final String name = mStudents.get(position).getfName() + " " + mStudents.get(position).getlName();
            viewHolder.mNameTextView.setText(name);
        }

        return view;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<MAStudentModel> results = new ArrayList<MAStudentModel>();
                if (studentsSource == null)
                    studentsSource = mStudents;
                if (constraint != null) {
                    if (studentsSource != null && studentsSource.size() > 0) {
                        for (final MAStudentModel g : studentsSource) {
                            if (g.getfName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mStudents = (ArrayList<MAStudentModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStudents.size();
    }

    @Override
    public MAStudentModel getItem(int position) {
        return mStudents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

class ViewHolder {
    protected NetworkImageView mProfilePicNetworkImageView;
    protected TextView mRollNoTextView;
    protected TextView mNameTextView;

    ViewHolder(View view) {
        mProfilePicNetworkImageView = (NetworkImageView) view.findViewById(R.id.item_profile_pic_imageview);
        mRollNoTextView = (TextView) view.findViewById(R.id.item_rollno_textview);
        mNameTextView = (TextView) view.findViewById(R.id.item_name_textview);
    }
}