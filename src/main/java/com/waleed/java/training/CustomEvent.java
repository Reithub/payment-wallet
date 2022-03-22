package com.waleed.java.training;

import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {

	public CustomEvent(Object source) {
		super(source);
	}
	
	@Override
	public String toString() {
		return "New learner added...";
	}
	
}
