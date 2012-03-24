package hello.persistence.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.DataFormatType;
import org.eclipse.persistence.annotations.Field;
import org.eclipse.persistence.annotations.NoSql;

/**
 * Sample entity with JPA + NoSQL annotations.
 */
@Entity
@NoSql(dataFormat = DataFormatType.MAPPED)
public class GreetingEntity {

	@Id
	@GeneratedValue
	@Field
	String id;

	@Field
	private long created;

	@Field
	private String text;

	@Field
	private String submittedBy;

	@Field
	private long processedOn;

	@Field
	private String processedBy;

	/**
	 * Returns the created.
	 * 
	 * @return the created
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the processedBy.
	 * 
	 * @return the processedBy
	 */
	public String getProcessedBy() {
		return processedBy;
	}

	/**
	 * Returns the processedOn.
	 * 
	 * @return the processedOn
	 */
	public long getProcessedOn() {
		return processedOn;
	}

	/**
	 * Returns the submittedBy.
	 * 
	 * @return the submittedBy
	 */
	public String getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the created.
	 * 
	 * @param created
	 *            the created to set
	 */
	public void setCreated(final long created) {
		this.created = created;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Sets the processedBy.
	 * 
	 * @param processedBy
	 *            the processedBy to set
	 */
	public void setProcessedBy(final String processedBy) {
		this.processedBy = processedBy;
	}

	/**
	 * Sets the processedOn.
	 * 
	 * @param processedOn
	 *            the processedOn to set
	 */
	public void setProcessedOn(final long processedOn) {
		this.processedOn = processedOn;
	}

	/**
	 * Sets the submittedBy.
	 * 
	 * @param submittedBy
	 *            the submittedBy to set
	 */
	public void setSubmittedBy(final String submittedBy) {
		this.submittedBy = submittedBy;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(final String text) {
		this.text = text;
	}
}
