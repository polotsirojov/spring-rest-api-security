package com.polot.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "trainee")
@Builder
@AllArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dob;

    private String address;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Training> trainings;

    public Trainee() {
    }

    public Trainee(LocalDate dob, String address, User user) {
        this.dob = dob;
        this.address = address;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "id=" + id +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
