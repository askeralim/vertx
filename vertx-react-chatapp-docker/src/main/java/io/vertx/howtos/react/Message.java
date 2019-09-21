package io.vertx.howtos.react;

public class Message {
	private String author;

	public Message() {
		// TODO Auto-generated constructor stub
	}
	public Message(String author, String message) {
		this.author = author;
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;
	  @Override
	  public String toString() {
	    final StringBuilder sb = new StringBuilder("Message{");
	    sb.append("author='").append(author);
	    sb.append("', message='").append(message).append('\'');
	    sb.append('}');
	    return sb.toString();
	  }
}
