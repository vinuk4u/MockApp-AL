package com.al.mockapp.holders;

import com.al.mockapp.models.MAStudentModel;

/**
 * Created by vineeth on 01/04/16.
 */
public class MAStudentHolder {
    private static MAStudentHolder singletonInstance = null;

    private MAStudentModel mStudentModel = null;

    public static MAStudentHolder getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new MAStudentHolder();
        }
        return singletonInstance;
    }

    public void setStudentModel(MAStudentModel studentModel) {
        this.mStudentModel = studentModel;
    }

    public MAStudentModel getStudentModel() {
        return mStudentModel;
    }
}
