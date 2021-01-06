package tech.icoding.sci.service.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_users")
public class User extends BaseEntity<Long>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 393459296660019909L;
	 
	@Column(length = 100, nullable = false)
	private String username;
	@Column(length = 100, nullable = false)
    private String name;
	
	@Column(length = 100, nullable = false)
    private String password;

}
