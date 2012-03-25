package hello.jaxrs.json.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Greeting {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
