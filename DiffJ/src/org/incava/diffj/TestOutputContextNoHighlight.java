package org.incava.diffj;

public class TestOutputContextNoHighlight extends OutputContextTest {
	public TestOutputContextNoHighlight(String name) {
		super(name);
	}

	public boolean highlight() {
		return false;
	}

	public String adorn(String str, boolean isDelete) {
		return str;
	}
}
