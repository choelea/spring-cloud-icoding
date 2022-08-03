/*
 * Copyright 2008-2018 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 * FileId: TwL4D87a7uiR/X1uIzijRbDoEtfu1iFU
 */
package tech.icoding.sci.core.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import java.io.Serializable;


/**
 * Entity - 排序基类
 * 
 * @author SHOP++ Team
 * @version 6.1
 */
@MappedSuperclass
public abstract class OrderedEntity<ID extends Serializable> extends BaseEntity<ID> implements Comparable<OrderedEntity<ID>> {

	private static final long serialVersionUID = 5995013015967525827L;

	/**
	 * "排序"属性名称
	 */
	public static final String ORDER_PROPERTY_NAME = "order";

	/**
	 * 排序
	 */
	@Min(0)
	@Column(name = "orders")
	private Integer order;

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	@Override
	public int compareTo(OrderedEntity<ID> orderEntity) {
		if (orderEntity == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
	}

}