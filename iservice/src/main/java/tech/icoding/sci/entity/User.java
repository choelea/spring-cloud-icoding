package tech.icoding.sci.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import tech.icoding.sci.sdk.common.UserType;

import javax.persistence.*;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_users")
public class User extends BaseEntity<Long>{
    private static final long serialVersionUID = -6711913440613658010L;

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UserType userType;

    @Column(length = 100, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;
}
