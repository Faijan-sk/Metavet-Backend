package com.example.demo.Exceptions;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

public class SuccessResponse implements StandardResponse{
 
	private int code;
	private Object content;
	private String uid;
 
	@CreationTimestamp
	private LocalDateTime timestamp;
 
	public SuccessResponse(int code, Object content, String uid) {
		super();
		this.code = code;
		this.content = content;
		this.uid = uid;
		this.timestamp = LocalDateTime.now();
	}
 
	public SuccessResponse(int code, Object content) {
		super();
		this.code = code;
		this.content = content;
		this.timestamp = LocalDateTime.now();
	}
}