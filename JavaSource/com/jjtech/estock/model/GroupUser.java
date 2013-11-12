package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 4, 2013 9:48:45 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */

public class GroupUser implements Serializable {

	private static final long serialVersionUID = 6061252451833188966L;

	
    public Integer id;

    @Size(max=45)
    public String nama;
    
    //tambahan
    public String namamenu;
    public Integer group_user_id;
    public Integer menu_id;
    public Integer active;
    
    public String mode;
    public List<Menu> menu;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNamamenu() {
		return namamenu;
	}

	public void setNamamenu(String namamenu) {
		this.namamenu = namamenu;
	}

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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}
    
    

}