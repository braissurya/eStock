package com.jjtech.estock.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 4, 2013 9:51:51 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class HakAkses implements Serializable {

	private static final long serialVersionUID = 4122666837941916538L;

	@NotNull
    public Integer group_user_id;

	@NotNull
    public Integer menu_id;

    public Integer active;

	public Integer getGroup_user_id() {
		return group_user_id;
	}

	public void setGroup_user_id(Integer group_user_id) {
		this.group_user_id = group_user_id;
	}

	public Integer getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}
    
    

}
