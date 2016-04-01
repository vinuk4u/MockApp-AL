package com.al.mockapp.models.response;

import com.al.mockapp.models.MAStudentModel;

import java.util.ArrayList;

/**
 * Created by vineeth on 31/03/16
 */
public class MAResponseArrayModel {
    private ArrayList<MAStudentModel> responseModels;

    public ArrayList<MAStudentModel> getResponseModels() {
        return responseModels;
    }

    public void setResponseModels(ArrayList<MAStudentModel> responseModels) {
        this.responseModels = responseModels;
    }
}
