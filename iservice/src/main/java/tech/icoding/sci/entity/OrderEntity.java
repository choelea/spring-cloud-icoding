package tech.icoding.sci.entity;

/**
 * @author : Joe
 * @date : 2022/7/18
 */

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_orders")
public class OrderEntity extends BaseEntity<Long>{
    private static final long serialVersionUID = 6713602109653849136L;
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 总金额
     */
    private Long amount;


    @OneToMany
    private List<OrderItemEntity> items;
}
