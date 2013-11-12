package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Rudy Hermawan Jelek
 * @since		: May 14, 2013 9:55:00 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class COA implements Serializable {

	private static final long serialVersionUID = -10470152516632726L;

    @NotEmpty
    @Size(max=18)
    public String id;

    @Size(max=80)
    public String nama;

    public Integer level;

    public Integer post;

    @Size(max=1)
    public String dk;

    @Size(max=18)
    public String parent;

    public Integer pl;

    public Integer active;

    public Integer createby;

    public Date createdate;

    public Integer urut;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getPost() {
		return post;
	}
	public void setPost(Integer post) {
		this.post = post;
	}
	public String getDk() {
		return dk;
	}
	public void setDk(String dk) {
		this.dk = dk;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Integer getPl() {
		return pl;
	}
	public void setPl(Integer pl) {
		this.pl = pl;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Boolean getAkses() {
		return akses;
	}
	public void setAkses(Boolean akses) {
		this.akses = akses;
	}
	
	public Integer getUrut() {
		return urut;
	}
	public void setUrut(Integer urut) {
		this.urut = urut;
	}

}
