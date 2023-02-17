package edu.imse.sql.entity;

import edu.imse.sql.entity.keys.EditorialBoardKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "editorial_board")
public class EditorialBoard extends PanacheEntityBase {

	@EmbeddedId
	public EditorialBoardKey editorialBoardKey;
	public Integer budget;
	public Integer reputation;
}
