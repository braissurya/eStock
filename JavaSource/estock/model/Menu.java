package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 4, 2013 9:44:01 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = 5771218726903002258L;

	
    public Integer id;

    public Integer parent;

    @Size(max=50)
    public String nama;

    @Size(max=100)
    public String link;

    public Integer urut;

    public Integer level;
    
    public String path;

    public Integer active;

    public Integer createby;

    public Date createdate;

    public Integer modifyby;

    public Date modifydate;
    
    //tambahan
    public String parent_nama;
    
    public String mode;
    public Boolean akses;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getUrut() {
		return urut;
	}

	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getModifyby() {
		return modifyby;
	}

	public void setModifyby(Integer modifyby) {
		this.modifyby = modifyby;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getParent_nama() {
		return parent_nama;
	}

	public void setParent_nama(String parent_nama) {
		this.parent_nama = parent_nama;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Boolean getAkses() {
		return akses;
	}

	public void setAkses(Boolean akses) {
		this.akses = akses;
	}
    
    
	
}
