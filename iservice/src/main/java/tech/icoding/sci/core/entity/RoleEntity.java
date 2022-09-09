package tech.icoding.sci.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import tech.icoding.sci.core.annotation.DataIgnore;
import tech.icoding.sci.core.annotation.FormIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_roles")
public class RoleEntity extends BaseEntity<Long>{

    private static final long serialVersionUID = -7179174601466803958L;
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 64,unique = true)
    private String code;

    /**
     * 角色描述
     */
    @Column(length = 64)
    private String remark;

    private Integer delFlag;

    @ManyToMany
    private Set<PermissionEntity> permissions;

    /**
     * mappedBy = "roles" 表明了关系是交由UserEntity来维护，这里很重要。因为Role是独立User先创建的，而创建User需要选定指定的Role。
     */
    @DataIgnore
    @FormIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserEntity> users;

}
