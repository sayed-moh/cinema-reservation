package murraco.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "event")
public class filmRes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ID;
    Long movie_ID;
    Integer user_ID;
    String Seats;
}
