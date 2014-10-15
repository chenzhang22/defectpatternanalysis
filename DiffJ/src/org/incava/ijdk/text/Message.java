package org.incava.ijdk.text;

import java.text.MessageFormat;

import org.incava.diffj.util.ChangeType;

public class Message {
	private final String pattern;
	private ChangeType changeType;

	public Message(String pattern, ChangeType codeChanged) {
		this.pattern = pattern;
		this.changeType = codeChanged;
	}

	public String format(Object... args) {
		return MessageFormat.format(pattern, args);
	}

	public String getPattern() {
		return pattern;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public boolean equals(Message other) {
		return pattern.equals(other.pattern);
	}
}
