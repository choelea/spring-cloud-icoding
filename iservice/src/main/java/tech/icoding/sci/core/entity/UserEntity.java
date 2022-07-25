package tech.icoding.sci.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_users")
public class UserEntity extends BaseEntity<Long>{
    private static final long serialVersionUID = -6711913440613658010L;

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String userName;

    @Column(length = 64)
    private String nickName;

    @Column(length = 64)
    private String password;

    @Column(length = 64)
    private String salt;

    private Integer enableFlag;

    @Column(length = 64)
    private String phone;

    @Column(length = 64)
    private String email;

    @Column(length = 64)
    private String remark;

    private Integer delFlag;

    @ManyToOne
    private RoleEntity mainRole;

    @ManyToMany
    @JoinTable(
            name = "t_role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

}
