package com.example.wecare_doc;

public class Patient {

    String Name,Email,Password,BloodGroup,Gender,ContactNo,ProfileURI,Disease,City,AdmittedDate,Complain;
    String age;
    public Patient(String name, String email, String password, String bloodGroup, String gender, String contactNo, String profileURI, String a,String disease,String city,String AdD,String complain) {
        Name = name;
        Email = email;
        Password = password;
        BloodGroup = bloodGroup;
        Gender = gender;
        ContactNo = contactNo;
        ProfileURI = profileURI;
        age = a;
        Disease=disease;
        City=city;
        AdmittedDate=AdD;
        Complain=complain;
    }
    public String getName() {
        return Name;
    }
    public String getDisease() {
        return Disease;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        Complain = complain;
    }

    public void setName(String name) {
        Name = name;
    }
    public void setAdmittedDate(String date) {
        AdmittedDate = date;
    }
    public String getAdmittedDate( ) {
        return AdmittedDate ;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public void setCity(String city){City=city;}
    public String getCity( ){return City;}

    public String getPassword() {
        return Password;
    }
    public void setDisease(String disease) {
        Disease = disease;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getProfileURI() {
        return ProfileURI;
    }

    public void setProfileURI(String profileURI) {
        ProfileURI = profileURI;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(Patient.class)
    }

}

