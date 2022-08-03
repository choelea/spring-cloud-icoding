package tech.icoding.sci.core.entity;

import lombok.Getter;
import lombok.Setter;
import tech.icoding.sci.core.annotation.DataIgnore;
import tech.icoding.sci.core.annotation.FormIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Joe
 * @date : 2022/8/2
 */
@Getter
@Setter
@Entity
public class PermissionEntity extends BaseEntity<Long>{
    private static final long serialVersionUID = 3549495420717094299L;
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 64,unique = true)
    private String code;

    @Column(length = 64,unique = true)
    private String path;

    @Column(length = 64,unique = true)
    private String icon;

    private Integer sort;

    private Integer enable;

    /**
     * 菜单类型 （0目录 1菜单 2按钮）
     */
    private Integer type;

    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private PermissionEntity parent;

    /**
     * 下级分类
     */
    @FormIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("sort asc")
    private Set<PermissionEntity> children = new HashSet<>();

    @FormIgnore
    @DataIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles;
}
