package com.example.threading;

public class Data {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String gender;
    private String country;
    private String domain_name;
    private String birth_date;
    private int Id_int;
    public Data(String id, String first_name, String last_name, String email, String gender, String country, String domain_name, String birth_date) {
        this.id = id;
        Id_int = Integer.parseInt(id);
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.domain_name = domain_name;
        this.birth_date = birth_date;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public int getId_int() {
        return Id_int;
    }

    public void setId_int(int id_int) {
        Id_int = id_int;
    }
}
