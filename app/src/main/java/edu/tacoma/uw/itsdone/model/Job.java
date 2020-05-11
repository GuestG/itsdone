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
    private String mTitle;
    private String mCourseShortDesc;
    private String mCourseLongDesc;
    private String mLocation;
    private String mPrice;

    public static final String ID = "jobid";
    public static final String CREATOR_ID = "jobcreatorid";
    public static final String TITLE = "title";
    public static final String SHORT_DESC = "shortdesc";
    public static final String LONG_DESC = "longdesc";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";

    public Job (String id, String cId, String title, String sDesc, String lDesc, String loc, String pr){
        mJobId = id;
        mCreatorId = cId;
        mTitle = title;
        mCourseShortDesc = sDesc;
        mCourseLongDesc = lDesc;
        mPrice = pr;
        mLocation = loc;
    }

    public String getJobId() { return mJobId; }
    public String getCreatorId() { return mCreatorId; }
    public String getTitle() { return mTitle; }
    public String getShortDesc() { return mCourseShortDesc; }
    public String getLongDesc() { return mCourseLongDesc; }
    public String getPrice() { return mPrice; }
    public String getLocation() { return mLocation; }

    public void setJobId(String s) { mJobId = s; }
    public void setCreatorId(String s) { mCreatorId = s; }
    public void setTitle(String s) { mTitle = s; }
    public void setShortDesc(String s) { mCourseShortDesc = s; }
    public void setLongDesc(String s) { mCourseLongDesc = s; }
    public void setPrice(String s) { mPrice = s; }
    public void setLocation(String s) { mLocation = s; }

    public static List<Job> parseJobJson(String jobJson) throws JSONException {
        List<Job> jobList = new ArrayList<>();
        if (jobJson != null) {
            JSONArray arr = new JSONArray(jobJson);
            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Job job = new Job(obj.getString(Job.ID), obj.getString(Job.CREATOR_ID), obj.getString(Job.TITLE),
                        obj.getString(Job.SHORT_DESC), obj.getString(Job.LONG_DESC), obj.getString(Job.LOCATION),
                        obj.getString(Job.PRICE));
                jobList.add(job);
            }
        }
        return jobList;
    }
}

