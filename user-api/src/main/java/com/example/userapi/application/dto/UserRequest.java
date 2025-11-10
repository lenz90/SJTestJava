package com.example.userapi.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class UserRequest {

    @NotBlank(message = "name es obligatorio")
    private String name;

    @NotBlank(message = "email es obligatorio")
    @Email(message = "email inválido")
    private String email;

    @NotBlank(message = "password es obligatorio")
    // Al menos 8 caracteres, una minúscula, una mayúscula y un dígito
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
             message = "password debe tener mínimo 8 caracteres, una mayúscula, una minúscula y un dígito")
    private String password;

    private List<PhoneRequest> phones;

    public UserRequest() {}

    // getters / setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<PhoneRequest> getPhones() { return phones; }
    public void setPhones(List<PhoneRequest> phones) { this.phones = phones; }

    // PhoneRequest anidado (si no existe aún en el proyecto)
    public static class PhoneRequest {
        private String number;
        private String citycode;
        private String countrycode;

        public PhoneRequest() {}

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }

        public String getCitycode() { return citycode; }
        public void setCitycode(String citycode) { this.citycode = citycode; }

        public String getCountrycode() { return countrycode; }
        public void setCountrycode(String countrycode) { this.countrycode = countrycode; }
    }
}