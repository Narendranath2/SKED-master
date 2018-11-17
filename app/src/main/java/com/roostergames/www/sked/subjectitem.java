package com.roostergames.www.sked;

public class subjectitem {
    private String subjectName;
    private String subjectTime;
    public subjectitem(String sname,String stime)
    {
        subjectName = sname;
        subjectTime = stime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectTime() {
        return subjectTime;
    }
}
