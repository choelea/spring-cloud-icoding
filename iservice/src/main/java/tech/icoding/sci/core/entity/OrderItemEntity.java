package tech.icoding.sci.core.entity;

/**
 * @author : Joe
 * @date : 2022/7/18
 */

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_order_items")
public class OrderItemEntity extends BaseEntity<Long>{
    private static final long serialVersionUID = 6713602109653849136L;
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String productName;

    /**
     * 数量
     */
    private Integer quantity;

    private Long unitPrice;

    /**
     * 总金额
     */
    private Long amount;

    @ManyToOne
    private OrderEntity order;
}
