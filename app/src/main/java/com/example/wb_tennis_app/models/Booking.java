package com.example.wb_tennis_app.models;

import java.io.Serializable;

public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    private int bookingNo;
    private String memberName;
    private String accountNo;
    private String email;
    private String phoneNumber;
    private String courtType;
    private String courtNo;
    private String date;
    private int dayOfWeek;
    private String duration;

    // Constructor for creating a new booking
    public Booking(int bookingNo, String memberName, String accountNo, String email, String phoneNumber,
                   String courtType, String courtNo, String date, int dayOfWeek, String duration) {
        this.bookingNo = bookingNo;
        this.memberName = memberName;
        this.accountNo = accountNo;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.courtType = courtType;
        this.courtNo = courtNo;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.duration = duration;
    }

    // Constructor for parsing API responses
    public Booking() {
        // Default constructor required for API parsing
    }

    // Getters and setters for each field
    public int getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(int bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) { // Added setter
        this.accountNo = accountNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public String getCourtNo() {
        return courtNo;
    }

    public void setCourtNo(String courtNo) {
        this.courtNo = courtNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingNo=" + bookingNo +
                ", memberName='" + memberName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", courtType='" + courtType + '\'' +
                ", courtNo='" + courtNo + '\'' +
                ", date='" + date + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", duration='" + duration + '\'' +
                '}';
    }
}