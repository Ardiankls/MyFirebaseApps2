package com.uc.myfirebaseapss.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private String uid;
    private String name;
    private String password;
    private String email;
    private String nim;
    private String gender;
    private String age;
    private String address;

    public Student() {

    }

    public Student(String uid, String name, String password, String email, String nim, String gender, String age, String address) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.email = email;
        this.nim = nim;
        this.gender = gender;
        this.age = age;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getGender() {
        return gender;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.nim);
        dest.writeString(this.gender);
        dest.writeString(this.age);
        dest.writeString(this.address);
    }

    protected Student(Parcel in) {
        this.uid = in.readString();
        this.name = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.nim = in.readString();
        this.gender = in.readString();
        this.age = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
