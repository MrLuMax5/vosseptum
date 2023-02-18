package de.vosseptum.sql.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "topic")
public class Topic extends PanacheEntityBase {

	@Id
	public String subject;
	public String category;
	@Min(1)
	@Max(6)
	public Integer popularity;
}
