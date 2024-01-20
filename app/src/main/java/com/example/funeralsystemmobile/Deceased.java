package com.example.funeralsystemmobile;

public class Deceased {
    private int id;
    private String fname;
    private String mname;
    private String lname;
    private String relationship;
    private String causeofdeath;
    private String sex;
    private String religion;
    private String age;
    private String dateofbirth;
    private String dateofdeath;
    private String placeofdeath;
    private String citizenship;
    private String address;
    private String civilstatus;
    private String occupation;
    private String namecemetery;
    private String addresscemetery;
    private String nameFather;
    private String nameMother;

    public Deceased() {
    }

    public Deceased(int id, String fname, String mname, String lname, String relationship, String sex, String religion, String age, String dateofbirth, String dateofdeath, String placeofdeath, String citizenship, String address, String civilstatus, String occupation, String namecemetery, String addresscemetery, String nameFather, String nameMother) {
        this.id = id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.relationship = relationship;
        this.sex = sex;
        this.religion = religion;
        this.age = age;
        this.dateofbirth = dateofbirth;
        this.dateofdeath = dateofdeath;
        this.placeofdeath = placeofdeath;
        this.citizenship = citizenship;
        this.address = address;
        this.civilstatus = civilstatus;
        this.occupation = occupation;
        this.namecemetery = namecemetery;
        this.addresscemetery = addresscemetery;
        this.nameFather = nameFather;
        this.nameMother = nameMother;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getCauseofdeath() {
        return causeofdeath;
    }

    public void setCauseofdeath(String causeofdeath) {
        this.causeofdeath = causeofdeath;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getDateofdeath() {
        return dateofdeath;
    }

    public void setDateofdeath(String dateofdeath) {
        this.dateofdeath = dateofdeath;
    }

    public String getPlaceofdeath() {
        return placeofdeath;
    }

    public void setPlaceofdeath(String placeofdeath) {
        this.placeofdeath = placeofdeath;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCivilstatus() {
        return civilstatus;
    }

    public void setCivilstatus(String civilstatus) {
        this.civilstatus = civilstatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNamecemetery() {
        return namecemetery;
    }

    public void setNamecemetery(String namecemetery) {
        this.namecemetery = namecemetery;
    }

    public String getAddresscemetery() {
        return addresscemetery;
    }

    public void setAddresscemetery(String addresscemetery) {
        this.addresscemetery = addresscemetery;
    }

    public String getNameFather() {
        return nameFather;
    }

    public void setNameFather(String nameFather) {
        this.nameFather = nameFather;
    }

    public String getNameMother() {
        return nameMother;
    }

    public void setNameMother(String nameMother) {
        this.nameMother = nameMother;
    }
}
