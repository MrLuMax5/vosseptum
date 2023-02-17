package edu.imse.sql.entity.keys;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@SequenceGenerator(name="seq", initialValue=100, allocationSize=2)
public class ArticleKey implements Serializable {
	@Column(name = "number")
	@GeneratedValue(strategy= GenerationType.SEQUENCE) // Leave it null when persisting
	public Integer number;
	@Column(name = "short_description")
	public String shortDescription;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ArticleKey that = (ArticleKey) o;
		return Objects.equals(number, that.number) && Objects.equals(shortDescription, that.shortDescription);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number, shortDescription);
	}

	@Override
	public String toString() {
		return shortDescription + "-" + number;
	}
}
