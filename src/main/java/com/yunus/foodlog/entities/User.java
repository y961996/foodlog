package com.yunus.foodlog.entities;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "foodlog_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;
    private int avatar;

    public User(String userName, String password, int avatar) {
        this.userName = userName;
        this.password = password;
        this.avatar = avatar;
    }
}
