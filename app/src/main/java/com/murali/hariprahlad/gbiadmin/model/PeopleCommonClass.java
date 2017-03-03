package com.murali.hariprahlad.gbiadmin.model;

/**
 * Created by Hari Prahlad on 15-05-2016.
 */
public class PeopleCommonClass {


    private String Name;
    private String Image;
    private String IdeaName;
    private String IdeaDescription;
    private String PhoneNumber;

    private String Email;
    public PeopleCommonClass(){

    }
    public PeopleCommonClass(String Name, String Image, String Ideaname, String IdeaDesc, String Phonenumber, String Email) {
        this.Name = Name;
        this.Image = Image;
        this.IdeaName = Ideaname;
        this.IdeaDescription = IdeaDesc;
        this.PhoneNumber = Phonenumber;
        this.Email = Email;
    }
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
    public String getIdeaName() {
        return IdeaName;
    }

    public void setIdeaName(String IdeaName) {
        this.IdeaName = IdeaName;
    }

    public String getIdeaDescription() {
        return IdeaDescription;
    }

    public void setIdeaDescription(String IdeaDescription) {
        this.IdeaDescription = IdeaDescription;
    }
    public String getPhoneNumber(){
        return PhoneNumber;
    }
    public void setPhoneNumber(String PhoneNumber){
        this.PhoneNumber = PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
