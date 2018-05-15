package com.tradesman.provider.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elluminati Mohit on 1/31/2017.
 */

public class PreviousJob implements Parcelable {

    private int totalPage;
    private int pageNumber;

    private String requestId;
    private String jobTitle;
    private String description;
    private String startTime;
    private String address;
    private String latitude;
    private String longitude;
    private String issueImgeUrl;
    private String distance;
    private String distanceUnit;
    private String userName;
    private String userImageUrl;
    private String serviceName;
    private String serviceImageUrl;
    private String requestType;
    private String requestStatus;
    private String currency;
    private String quotation;
    private double userGivenRating;
    private String userComment;
    private String adminPrice;

    public PreviousJob(){

    }

    protected PreviousJob(Parcel in) {
        totalPage = in.readInt();
        pageNumber = in.readInt();
        requestId = in.readString();
        jobTitle = in.readString();
        description = in.readString();
        startTime = in.readString();
        address = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        issueImgeUrl = in.readString();
        distance = in.readString();
        distanceUnit = in.readString();
        userName = in.readString();
        userImageUrl = in.readString();
        serviceName = in.readString();
        serviceImageUrl = in.readString();
        requestType = in.readString();
        requestStatus = in.readString();
        currency = in.readString();
        quotation = in.readString();
        userGivenRating = in.readDouble();
        userComment = in.readString();
        adminPrice = in.readString();
    }

    public static final Creator<PreviousJob> CREATOR = new Creator<PreviousJob>() {
        @Override
        public PreviousJob createFromParcel(Parcel in) {
            return new PreviousJob(in);
        }

        @Override
        public PreviousJob[] newArray(int size) {
            return new PreviousJob[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totalPage);
        parcel.writeInt(pageNumber);
        parcel.writeString(requestId);
        parcel.writeString(jobTitle);
        parcel.writeString(description);
        parcel.writeString(startTime);
        parcel.writeString(address);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(issueImgeUrl);
        parcel.writeString(distance);
        parcel.writeString(distanceUnit);
        parcel.writeString(userName);
        parcel.writeString(userImageUrl);
        parcel.writeString(serviceName);
        parcel.writeString(serviceImageUrl);
        parcel.writeString(requestType);
        parcel.writeString(requestStatus);
        parcel.writeString(currency);
        parcel.writeString(quotation);
        parcel.writeDouble(userGivenRating);
        parcel.writeString(userComment);
        parcel.writeString(adminPrice);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIssueImgeUrl() {
        return issueImgeUrl;
    }

    public void setIssueImgeUrl(String issueImgeUrl) {
        this.issueImgeUrl = issueImgeUrl;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }

    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImageUrl = serviceImageUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuotation() {
        return quotation;
    }

    public void setQuotation(String quotation) {
        this.quotation = quotation;
    }


    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public static Creator<PreviousJob> getCREATOR() {
        return CREATOR;
    }

    public double getUserGivenRating() {
        return userGivenRating;
    }

    public void setUserGivenRating(double userGivenRating) {
        this.userGivenRating = userGivenRating;
    }

    public String getAdminPrice() {
        return adminPrice;
    }

    public void setAdminPrice(String adminPrice) {
        this.adminPrice = adminPrice;
    }

}
