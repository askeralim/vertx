package io.chatapp.dao.model;

public class Status {
	private String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Status(String status) {
		this.status = status;
	}

	public static Status SUCCESS = new Status("success");
	public static Status FAIL = new Status("fail");
}
