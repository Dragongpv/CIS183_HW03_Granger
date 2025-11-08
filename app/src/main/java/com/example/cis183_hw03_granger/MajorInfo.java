package com.example.cis183_hw03_granger;

public class MajorInfo
{
    //this will store all the data about a major
    //data, constructors, getters/setters
    String majorId;
    String majorName;
    String majorPrefix;

    MajorInfo(String majorId, String majorName, String majorPrefix)
    {
        this.majorId = majorId;
        this.majorName = majorName;
        this.majorPrefix = majorPrefix;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorPrefix() {
        return majorPrefix;
    }

    public void setMajorPrefix(String majorPrefix) {
        this.majorPrefix = majorPrefix;
    }

    @Override
    public String toString() {
        return majorName + " (" + majorPrefix + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MajorInfo majorInfo = (MajorInfo) o;
        return majorId != null && majorId.equals(majorInfo.majorId);
    }
}
