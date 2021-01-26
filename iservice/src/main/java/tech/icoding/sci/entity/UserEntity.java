package tech.icoding.sci.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_users")
public class UserEntity extends BaseEntity<Long>{
    private static final long serialVersionUID = -6711913440613658010L;

    @Column(length = 100, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;
}
