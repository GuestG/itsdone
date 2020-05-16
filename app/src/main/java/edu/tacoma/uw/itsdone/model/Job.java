package edu.tacoma.uw.itsdone.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a job. Users can create and accept jobs.
 * Job has fields such as title, price, location, and others.
 *
 * @author max
 * @since 5/8/2020
 *
 */
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
    public static final String LOCATION = "place";
    public static final String PRICE = "price";

    /**
     * Constructor for initializing fields
     * @param id SQL primary key
     * @param cId SQL key of job owner
     * @param title string title of the job
     * @param sDesc string short description of job
     * @param lDesc string long description of job
     * @param loc location of job
     * @param pr price the owner is willing to pay for anyone wanting to complete the job.
     */
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

    /**
     * method for parsing a json file from database into a list of job objects.
     * @param jobJson the json file
     * @return returns a new list of jobs.
     * @throws JSONException exception for if json parsing failed.
     */
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

