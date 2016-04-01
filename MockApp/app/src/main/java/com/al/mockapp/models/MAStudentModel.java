package com.al.mockapp.models;

import java.util.ArrayList;

/**
 * Created by vineeth on 31/03/16
 */
public class MAStudentModel {
    private String fName;
    private String lName;
    private String rollNo;
    private ArrayList<MAMarkModel> marks;
    private String profilePic;

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public ArrayList<MAMarkModel> getMarks() {
        return marks;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
