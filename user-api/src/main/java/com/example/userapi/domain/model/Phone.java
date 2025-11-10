package com.example.userapi.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private String citycode;
    private String countrycode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Phone() {
    }

    public Phone(Long id, String number, String citycode, String countrycode, User user) {
        this.id = id;
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
        this.user = user;
    }

    // getters / setters (nombres exactos para evitar problemas)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getCitycode() { return citycode; }
    public void setCitycode(String citycode) { this.citycode = citycode; }

    public String getCountrycode() { return countrycode; }
    public void setCountrycode(String countrycode) { this.countrycode = countrycode; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}