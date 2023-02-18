package de.vosseptum.sql.entity.keys;

import de.vosseptum.sql.entity.EditorialBoard;
import de.vosseptum.sql.entity.Journalist;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class JournalistInBoardKey implements Serializable {

	@ManyToOne
	public Journalist journalist;
	@ManyToOne
	public EditorialBoard editorialBoard;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JournalistInBoardKey that = (JournalistInBoardKey) o;
		return Objects.equals(journalist, that.journalist) && Objects.equals(editorialBoard, that.editorialBoard);
	}

	@Override
	public int hashCode() {
		return Objects.hash(journalist, editorialBoard);
	}
}
