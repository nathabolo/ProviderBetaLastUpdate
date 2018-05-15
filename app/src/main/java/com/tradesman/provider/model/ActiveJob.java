package com.tradesman.provider.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elluminati Mohit on 1/27/2017.
 */

public class ActiveJob implements Parcelable {

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
    private String userRating;
    private String serviceName;
    private String serviceImageUrl;
    private String requestType;
    private String requestStatus;
    private String currency;
    private String quotation;
    private String adminPrice;



    public ActiveJob(){
        /*constructor*/
    }

    protected ActiveJob(Parcel in) {
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
        userRating = in.readString();
        serviceName = in.readString();
        serviceImageUrl = in.readString();
        requestType = in.readString();
        requestStatus = in.readString();
        quotation = in.readString();
        currency = in.readString();
        adminPrice = in.readString();
    }

    public static final Creator<ActiveJob> CREATOR = new Creator<ActiveJob>() {
        @Override
        public ActiveJob createFromParcel(Parcel in) {
            return new ActiveJob(in);
        }

        @Override
        public ActiveJob[] newArray(int size) {
            return new ActiveJob[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
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
        parcel.writeString(userRating);
        parcel.writeString(serviceName);
        parcel.writeString(serviceImageUrl);
        parcel.writeString(requestType);
        parcel.writeString(requestStatus);
        parcel.writeString(getQuotation());
        parcel.writeString(currency);
        parcel.writeString(adminPrice);
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
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

    public static Creator<ActiveJob> getCREATOR() {
        return CREATOR;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getAdminPrice() {
        return adminPrice;
    }

    public void setAdminPrice(String adminPrice) {
        this.adminPrice = adminPrice;
    }

}
