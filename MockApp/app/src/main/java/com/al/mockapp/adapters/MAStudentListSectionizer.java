package com.al.mockapp.adapters;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vineeth on 31/03/16
 */
/**
 * Interface provides mechanism for supplying titles for instances based on the property they are
 * compared against. The parameterized type of the <b>Sectionizer</b> should be same as that of the
 * {@link MAStudentListSimpleSectionAdapter}.
 */
public interface MAStudentListSectionizer<MAStudentModel> {

    /**
     * Returns the title for the given instance from the data source.
     *
     * @param instance The instance obtained from the data source of the decorated list adapter.
     * @return section title for the given instance.
     */
    String getSectionTitleForItem(MAStudentModel instance);

    View getView(int position, View convertView, ViewGroup parent);
}
