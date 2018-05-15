package com.tradesman.provider.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elluminati Mohit on 1/23/2017.
 */

public class AvailableJob implements Parcelable {

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
    private String jobType;
    private String adminPrice;
    private String currency;
    private String q;
    private String q1;
    private String symbol;


    public AvailableJob() {
        /*constructor*/
    }

    protected AvailableJob(Parcel in) {
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
        jobType = in.readString();
        adminPrice = in.readString();
        currency = in.readString();
        symbol = in.readString();
    }

    public static final Creator<AvailableJob> CREATOR = new Creator<AvailableJob>() {
        @Override
        public AvailableJob createFromParcel(Parcel in) {
            return new AvailableJob(in);
        }

        @Override
        public AvailableJob[] newArray(int size) {
            return new AvailableJob[size];
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
        parcel.writeString(jobType);
        parcel.writeString(adminPrice);
        parcel.writeString(currency);
        parcel.writeString(symbol);
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAdminPrice() {
        return adminPrice;
    }

    public void setAdminPrice(String adminPrice) {
        this.adminPrice = adminPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setQ(String q1) {
        this.q1 = q1;
    }

    public String getQ() {
        return q1;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
