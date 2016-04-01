package com.al.mockapp.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by vineeth on 31/03/16
 */
/**
 * A very simple adapter that adds sections to adapters written for
 * {@link ListView}s.
 */
public class MAStudentListSimpleSectionAdapter<MAStudentModel> extends BaseAdapter
        implements SectionIndexer {
    // Constants
    private static final int VIEW_TYPE_SECTION_HEADER = 0;

    // Attributes
    private Context mContext;
    private BaseAdapter mListAdapter;
    private int mSectionLayoutId;
    private int mSectionTitleTextViewId;
    private MAStudentListSectionizer<MAStudentModel> mSectionizer;
    private LinkedHashMap<String, Integer> mSectionsHashMap;
    private String mSections = "";
    private final DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

            findSections();
        }
    };

    public MAStudentListSimpleSectionAdapter(Context context,
                                             BaseAdapter listAdapter, int sectionLayoutId,
                                             int sectionTitleTextViewId,
                                             MAStudentListSectionizer<MAStudentModel> sectionizer) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        } else if (listAdapter == null) {
            throw new IllegalArgumentException("listAdapter cannot be null.");
        } else if (sectionizer == null) {
            throw new IllegalArgumentException("sectionizer cannot be null.");
        } else if (!isTextView(context, sectionLayoutId, sectionTitleTextViewId)) {
            throw new IllegalArgumentException(
                    "sectionTitleTextViewId should be a TextView.");
        }

        this.mContext = context;
        this.mListAdapter = listAdapter;
        this.mSectionLayoutId = sectionLayoutId;
        this.mSectionTitleTextViewId = sectionTitleTextViewId;
        this.mSectionizer = sectionizer;
        this.mSectionsHashMap = new LinkedHashMap<String, Integer>();

        registerDataSetObserver(dataSetObserver);

        // Find sections
        findSections();
    }

    private boolean isTextView(Context context, int layoutId, int textViewId) {
        View inflatedView = View.inflate(context, layoutId, null);
        View foundView = inflatedView.findViewById(textViewId);

        return foundView instanceof TextView;
    }

    @Override
    public int getCount() {
        return mListAdapter.getCount() + getSectionCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SectionHolder sectionHolder = null;

        switch (getItemViewType(position)) {
            case VIEW_TYPE_SECTION_HEADER:
                if (view == null) {
                    view = View.inflate(mContext, mSectionLayoutId, null);

                    sectionHolder = new SectionHolder();
                    sectionHolder.titleTextView = (TextView) view
                            .findViewById(mSectionTitleTextViewId);

                    view.setTag(sectionHolder);
                } else {
                    sectionHolder = (SectionHolder) view.getTag();
                }
                break;

            default:
                view = mListAdapter.getView(getIndexForPosition(position),
                        convertView, parent);
                break;
        }

        if (sectionHolder != null) {
            String sectionName = sectionTitleForPosition(position);
            sectionHolder.titleTextView.setText(sectionName);
        }

        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return (mListAdapter.areAllItemsEnabled() && mSectionsHashMap.size() == 0);
    }

    @Override
    public int getItemViewType(int position) {
        int positionInCustomAdapter = getIndexForPosition(position);
        return mSectionsHashMap.values().contains(position) ? VIEW_TYPE_SECTION_HEADER
                : mListAdapter.getItemViewType(positionInCustomAdapter) + 1;
    }

    @Override
    public int getViewTypeCount() {
        return mListAdapter.getViewTypeCount() + 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return !mSectionsHashMap.values().contains(position) && mListAdapter.isEnabled(getIndexForPosition(position));
    }

    @Override
    public Object getItem(int position) {
        return mListAdapter.getItem(getIndexForPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return mListAdapter.getItemId(getIndexForPosition(position));
    }

    @Override
    public void notifyDataSetChanged() {
        mListAdapter.notifyDataSetChanged();
        findSections();
        super.notifyDataSetChanged();
    }

    /**
     * Returns the actual index of the object in the data source linked to the
     * this list item.
     *
     * @param position List item position in the {@link ListView}.
     * @return Index of the item in the wrapped list adapter's data source.
     */
    public int getIndexForPosition(int position) {
        int sections = 0;

        Set<Entry<String, Integer>> entrySet = mSectionsHashMap.entrySet();
        for (Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < position) {
                sections++;
            }
        }

        return position - sections;
    }

    private void findSections() {
        int n = mListAdapter.getCount();
        int nSections = 0;
        mSectionsHashMap.clear();
        mSections = "";

        for (int i = 0; i < n; i++) {
            @SuppressWarnings("unchecked")
            String sectionName = mSectionizer
                    .getSectionTitleForItem(((MAStudentModel) mListAdapter
                            .getItem(i)));

            if (!mSectionsHashMap.containsKey(sectionName)) {
                mSectionsHashMap.put(sectionName, i + nSections);
                nSections++;
            }
        }

        setSections(mSectionsHashMap);
    }

    public int getSectionCount() {
        return mSectionsHashMap.size();
    }

    public LinkedHashMap<String, Integer> getSectionsHashMap() {
        return mSectionsHashMap;
    }

    private String sectionTitleForPosition(int position) {
        String title = null;

        Set<Entry<String, Integer>> entrySet = mSectionsHashMap.entrySet();
        for (Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() == position) {
                title = entry.getKey();
                break;
            }
        }

        return title;
    }

    @Override
    public int getPositionForSection(int section) {
        try {
            if (section < mSections.length()) {
                for (int i = 0; i < this.getCount(); i++) {
                    String item = this.getItem(i).toString().toUpperCase();
                    if (item.charAt(0) == mSections.charAt(section))
                        return i;
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        try {
            String[] sectionsArr = new String[mSections.length()];
            for (int i = 0; i < mSections.length(); i++)
                sectionsArr[i] = "" + mSections.charAt(i);
            return sectionsArr;
        } catch (Exception e) {
        }

        return new String[1];
    }

    private void setSections(LinkedHashMap<String, Integer> sections) {
        Set<Entry<String, Integer>> entrySet = sections.entrySet();
        for (Entry<String, Integer> entry : entrySet) {
            mSections += entry.getKey();
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mListAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mListAdapter.unregisterDataSetObserver(observer);
    }

    static class SectionHolder {
        public TextView titleTextView;
    }

}
