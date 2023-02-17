package edu.imse.sql.entity.bridges;

import edu.imse.sql.entity.EditorialBoard;
import edu.imse.sql.entity.keys.JournalistInBoardKey;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.sql.Date;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "journalist_in_board")
@NamedQuery(name = "JournalistInBoard.getByJournalistId", query = "select journalistInBoardKey.editorialBoard from JournalistInBoard where journalist_id = ?1")
public class JournalistInBoard extends PanacheEntityBase {
	@EmbeddedId
	public JournalistInBoardKey journalistInBoardKey;
	public Date date;

	public static List<EditorialBoard> findByJournalistId(long id) {
		return find("#JournalistInBoard.getByJournalistId", id).list();
	}
}
