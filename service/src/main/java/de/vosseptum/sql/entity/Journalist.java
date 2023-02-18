package de.vosseptum.sql.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@Table(name = "journalist")
@NamedQuery(name = "Journalist.getByEmail", query = "from Journalist where email = ?1")
public class Journalist extends PanacheEntity {

	@Column(nullable = false)
	public String password;

	public String location;
	@Column(unique = true)
	public String email;

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "connection",
			joinColumns = {@JoinColumn(name = "parent_id")},
			inverseJoinColumns = {@JoinColumn(name = "child_id")}
	)
	public Set<Journalist> connections = new HashSet<>();

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "connection",
			joinColumns = {@JoinColumn(name = "child_id")},
			inverseJoinColumns = {@JoinColumn(name = "parent_id")}
	)
	public Set<Journalist> connectionOf = new HashSet<>();

	public static List<Journalist> findByEmail(String email) {
		return find("#Journalist.getByEmail", email).list();
	}

}
