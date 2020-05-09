package edu.tacoma.uw.itsdone.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Job implements Serializable {

    private String mJobId;
    private String mCreatorId;
    private String mCourseShortDesc;
    private String mCourseLongDesc;
    private String mPrice;
    private String mLocation;

    public static final String ID = "id";
    public static final String CREATOR_ID = "creator id";
    public static final String SHORT_DESC = "shortdesc";
    public static final String LONG_DESC = "longdesc";
    public static final String PRICE = "price";
    public static final String LOCATION = "location";

    public Job (String id, String cId, String sDesc, String lDesc, String pr, String loc){
        mJobId = id;
        mCreatorId = cId;
        mCourseShortDesc = sDesc;
        mCourseLongDesc = lDesc;
        mPrice = pr;
        mLocation = loc;
    }

    public String getJobId() { return mJobId; }
    public String getCreatorId() { return mCreatorId; }
    public String getShortDesc() { return mCourseShortDesc; }
    public String getLongDesc() { return mCourseLongDesc; }
    public String getPrice() { return mPrice; }
    public String getLocation() { return mLocation; }

    public void setJobId(String s) { mJobId = s; }
    public void setCreatorId(String s) { mCreatorId = s; }
    public void setShortDesc(String s) { mCourseShortDesc = s; }
    public void setLongDesc(String s) { mCourseLongDesc = s; }
    public void setPrice(String s) { mPrice = s; }
    public void setLocation(String s) { mLocation = s; }

    public static List<Job> parseCourseJson(String jobJson) throws JSONException {
        List<Job> jobList = new ArrayList<>();
        if (jobJson != null) {
            JSONArray arr = new JSONArray(jobJson);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Job job = new Job(obj.getString(Job.ID), obj.getString(Job.CREATOR_ID),
                        obj.getString(Job.SHORT_DESC), obj.getString(Job.LONG_DESC),
                        obj.getString(Job.PRICE),  obj.getString(Job.LOCATION));
                jobList.add(job);
            }
        }
        return jobList;
    }
}

