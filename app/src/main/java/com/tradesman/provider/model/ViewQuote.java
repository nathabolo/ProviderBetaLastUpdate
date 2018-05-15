package com.tradesman.provider.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elluminati Mohit on 1/25/2017.
 */

public class ViewQuote implements Parcelable {

    private String id;
    private String startTime;
    private String description;
    private String currency;
    private String distance;
    private String distanceUnit;
    private String issueImageUrl;
    private String jobTitle;
    private String quotation;
    private String quotationDate;
    private String requestType;
    private String address;
    private String userId;
    private String userName;
    private String userImageUrl;
    private String userRating;
    private String serviceId;
    private String serviceName;
    private String serviceImageUrl;
    private String latitude;
    private String longitude;
    private String symbol = "symbol";

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public ViewQuote(){

        /*constructor*/
    }

    private ViewQuote(Parcel in) {
        id = in.readString();
        startTime = in.readString();
        description = in.readString();
        currency = in.readString();
        distance = in.readString();
        distanceUnit = in.readString();
        issueImageUrl = in.readString();
        jobTitle = in.readString();
        quotation = in.readString();
        quotationDate = in.readString();
        requestType = in.readString();
        address = in.readString();
        userId = in.readString();
        userName = in.readString();
        userImageUrl = in.readString();
        userRating = in.readString();
        serviceId = in.readString();
        serviceName = in.readString();
        serviceImageUrl = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        symbol = in.readString();
    }

    public static final Creator<ViewQuote> CREATOR = new Creator<ViewQuote>() {
        @Override
        public ViewQuote createFromParcel(Parcel in) {
            return new ViewQuote(in);
        }

        @Override
        public ViewQuote[] newArray(int size) {
            return new ViewQuote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(startTime);
        parcel.writeString(description);
        parcel.writeString(currency);
        parcel.writeString(distance);
        parcel.writeString(distanceUnit);
        parcel.writeString(issueImageUrl);
        parcel.writeString(jobTitle);
        parcel.writeString(quotation);
        parcel.writeString(quotationDate);
        parcel.writeString(requestType);
        parcel.writeString(address);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(userImageUrl);
        parcel.writeString(userRating);
        parcel.writeString(serviceId);
        parcel.writeString(serviceName);
        parcel.writeString(serviceImageUrl);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(symbol);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getIssueImageUrl() {
        return issueImageUrl;
    }

    public void setIssueImageUrl(String issueImageUrl) {
        this.issueImageUrl = issueImageUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getQuotation() {
        return quotation;
    }

    public void setQuotation(String quotation) {
        this.quotation = quotation;
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

