package tech.icoding.sci.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author : Joe
 * @date : 2021/1/11
 */
@Data
@Entity
@Table(name = "t_products")
public class ProductEntity extends BaseEntity<Long>{

    //private static final long serialVersionUID = -3785545392726922140L;
}
