package object.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Data
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "is_moderator", nullable = false)
    private Integer isModerator;

    @Column(name = "reg_time", nullable = false)
    private Date regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    private String photo;

}