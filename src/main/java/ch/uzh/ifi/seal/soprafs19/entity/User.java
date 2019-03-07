package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Entity
// JPA annotation to make object ready for storage ina JPA-based data store
public class User implements Serializable {

	private static final long serialVersionUID = 1L;



	@Id  // instance variable annotated by @Id will be marked as entity identifier/ primary key in DB
	@GeneratedValue  // specifies which strategy is used for identifier generation for an instance variable marked by @Id
	private Long id;

	// @Column JPA annotation to specify the mapped column for a persistent property/field. -> marks annotated
	// instance variable as persistent.

	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true) 
	private String username;

	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	@Column  // might need to actually be nullable
	private String birthdayDate;

	@Column(nullable = false)
	private String creationDate;

	@Column
	private Integer games;

	@Column
	private Integer moves;



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}


	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}


	public String getBirthdayDate() {
		return birthdayDate;
	}
	public void setBirthdayDate(String birthdayDate) {
		this.birthdayDate = birthdayDate;
	}


	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getGames() {
		return games;
	}
	public void setGames(Integer games) {
		this.games = games;
	}

	public Integer getMoves() {
		return moves;
	}
	public void setMoves(Integer moves) {
		this.moves = moves;
	}


	@Override  // marks a method explicitly as "overriding", in this case for 'equals'.
	// Here used to check if two User objects are identical via the comparison of their user Id.
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
