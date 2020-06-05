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
 * @author Max Malyshev, Trevor Peters
 * @version 1.1
 * @since 2020-05-27
 */
public class Job implements Serializable {

    private String mJobId;
    private String mCreatorUsername;
    private String mTitle;
    private String mCourseShortDesc;
    private String mCourseLongDesc;
    private String mLocation;
    private String mPrice;
    private int mJobPicture;

    public static final String ID = "jobid";
    public static final String CREATOR_USERNAME = "creatorusername";
    public static final String TITLE = "title";
    public static final String SHORT_DESC = "shortdesc";
    public static final String LONG_DESC = "longdesc";
    public static final String LOCATION = "place";
    public static final String PRICE = "price";
    public static final String PHOTO = "photo";

    /**
     * Constructor for initializing fields
     * @param id SQL primary key
     * @param creatorUsername SQL key of job owner
     * @param title string title of the job
     * @param sDesc string short description of job
     * @param lDesc string long description of job
     * @param loc location of job
     * @param pr price the owner is willing to pay for anyone wanting to complete the job.
     */
    public Job (String id, String creatorUsername, String title, String sDesc, String lDesc, String loc, String pr){
        mJobId = id;
        mCreatorUsername = creatorUsername;
        mTitle = title;
        mCourseShortDesc = sDesc;
        mCourseLongDesc = lDesc;
        mPrice = pr;
        mLocation = loc;
        mJobPicture = 0;
    }

    public String getJobId() { return mJobId; }
    public String getCreatorUsername() { return mCreatorUsername; }
    public String getTitle() { return mTitle; }
    public String getShortDesc() { return mCourseShortDesc; }
    public String getLongDesc() { return mCourseLongDesc; }
    public String getPrice() { return mPrice; }
    public String getLocation() { return mLocation; }
    public int getPicture() { return mJobPicture; }

    public void setJobId(String s) { mJobId = s; }
    public void setCreatorId(String s) { mCreatorUsername = s; }
    public void setTitle(String s) { mTitle = s; }
    public void setShortDesc(String s) { mCourseShortDesc = s; }
    public void setLongDesc(String s) { mCourseLongDesc = s; }
    public void setPrice(String s) { mPrice = s; }
    public void setLocation(String s) { mLocation = s; }
    public void setPicture(int i) { mJobPicture = i; }

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
                Job job = new Job(obj.getString(Job.ID), obj.getString(Job.CREATOR_USERNAME), obj.getString(Job.TITLE),
                        obj.getString(Job.SHORT_DESC), obj.getString(Job.LONG_DESC), obj.getString(Job.LOCATION),
                        obj.getString(Job.PRICE));
                job.setPicture(obj.getInt(Job.PHOTO));
                jobList.add(job);
            }
        }
        return jobList;
    }
}

