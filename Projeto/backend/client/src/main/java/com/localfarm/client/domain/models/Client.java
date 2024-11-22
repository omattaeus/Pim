package com.localfarm.client.domain.models;

import com.localfarm.client.domain.models.enums.ClientType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "CNPJ/CPF is mandatory")
    private String cnpjCpf;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is mandatory")
    private ClientType type; // CLIENT or SUPPLIER

    @NotNull(message = "Code is mandatory")
    private String code;

    @NotNull(message = "Address is mandatory")
    private String address;

    @NotNull(message = "Number is mandatory")
    private String number;

    private String complement;

    @NotNull(message = "Neighborhood is mandatory")
    private String neighborhood;

    @NotNull(message = "City is mandatory")
    private String city;

    @NotNull(message = "State is mandatory")
    private String state;

    @NotNull(message = "Country is mandatory")
    private String country;

    @NotNull(message = "Postal code is mandatory")
    private String postalCode;

    @NotNull(message = "Email is mandatory")
    @Email
    private String email;

    @NotNull(message = "Phone is mandatory")
    private String phone;

    @NotNull(message = "Consent is mandatory")
    private Boolean consent;
}