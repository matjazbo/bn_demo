package com.demo.movies.api.jaxrs.providers.exception;

import javax.ws.rs.core.Response.Status;

public class ErrorPresenter {

	private Status status;
	private String msg;

	public ErrorPresenter(Throwable error) {
		this.msg = error.getMessage();
		this.status = Status.INTERNAL_SERVER_ERROR;
	}

	public ErrorPresenter(String msg, Status status) {
		this.msg = msg;
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
