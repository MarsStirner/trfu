package ru.efive.medicine.niidg.trfu.data.entity;

import ru.efive.dao.sql.entity.user.User;
import ru.efive.wf.core.ProcessUser;

public class Person extends User implements ProcessUser {
	private User _base;
	
	public Person(User user) {
		this._base = user;
		setCreated(user.getCreated());
		setDeleted(user.isDeleted());
		setEmail(user.getEmail());
		setFirstName(user.getFirstName());
		setId(user.getId());
		setLastName(user.getLastName());
		setLogin(user.getLogin());
		setMiddleName(user.getMiddleName());
		setPassword(user.getPassword());
		setRoles(user.getRoles());
		setSelectedRole(user.getSelectedRole());
	}
	
	private static final long serialVersionUID = -6200882661231446726L;

	@Override
	public User getUser() {
		return _base;
	}
}