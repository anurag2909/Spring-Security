package com.anurag.springsecdemo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="users")

public class User {

    @Id
    private int id;
    private String username;
    private String password;
}
