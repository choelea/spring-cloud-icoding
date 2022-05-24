package tech.icoding.sci.entity;
 

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

	protected static final int  MAX_LANGUAGE_NUM = 10;

	/**
	 * "ID"属性名称
	 */
	public static final String ID_PROPERTY_NAME = "id";

	/**
	 * "创建日期"属性名称
	 */
	public static final String CREATED_DATE_PROPERTY_NAME = "createdDate";

	/**
	 * "最后修改日期"属性名称
	 */
	public static final String LAST_MODIFIED_DATE_PROPERTY_NAME = "lastModifiedDate";

	/**
	 * "版本"属性名称
	 */
	public static final String VERSION_PROPERTY_NAME = "version";
	private static final long serialVersionUID = 4714864631962932481L;

	/**
	 * 获取主键
	 * @return
	 */
	abstract public ID getId();

	/**
	 * 创建日期
	 */
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;

	/**
	 * 最后修改日期
	 */
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime lastModifiedDate;

	/**
	 * 版本
	 */
	@Version
	@Column(nullable = false)
	private Long version;

	@PrePersist
	public void preSave() {
		this.createdDate = LocalDateTime.now();
		this.lastModifiedDate = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.lastModifiedDate = LocalDateTime.now();
	}

	/**
	 * 重写toString方法
	 * 
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", getClass().getName(), getId());
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity<?> other = (BaseEntity<?>) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return HashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += getId() != null ? getId().hashCode() * 31 : 0;
		return hashCode;
	}

}