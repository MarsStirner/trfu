package ru.efive.wf.core;

import java.io.Serializable;
import java.util.Set;

import ru.efive.dao.sql.entity.user.Role;
import ru.efive.dao.sql.entity.user.User;

public interface ProcessUser extends Serializable {
	
	public int getId();
	public String getDescription();
	public String getEmail();
	public Set<Role> getRoles();
	public Role getSelectedRole();
	public User getUser();
	
}