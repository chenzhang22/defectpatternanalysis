package org.incava.diffj.type;

import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.ast.SimpleNode;

import org.incava.diffj.util.ChangeType;
import org.incava.diffj.util.Messages;
import org.incava.ijdk.text.Message;

/**
 * Compares implements.
 */
public class Implements extends Supers {
	public static final Message IMPLEMENTED_TYPE_REMOVED = new Message(
			"implemented type removed: {0}",
			ChangeType.IMPLEMENTED_TYPE_REMOVED);
	public static final Message IMPLEMENTED_TYPE_ADDED = new Message(
			"implemented type added: {0}", ChangeType.IMPLEMENTED_TYPE_ADDED);
	public static final Message IMPLEMENTED_TYPE_CHANGED = new Message(
			"implemented type changed from {0} to {1}",
			ChangeType.IMPLEMENTED_TYPE_CHANGED);
	public static final Messages IMPLEMENTED_TYPE_MSGS = new Messages(
			IMPLEMENTED_TYPE_ADDED, IMPLEMENTED_TYPE_CHANGED,
			IMPLEMENTED_TYPE_REMOVED);

	public Implements(ASTClassOrInterfaceDeclaration decl) {
		super(decl);
	}

	protected Class<? extends SimpleNode> getPmdClass() {
		return net.sourceforge.pmd.ast.ASTImplementsList.class;
	}

	protected Message getAddedMessage() {
		return IMPLEMENTED_TYPE_MSGS.getAdded();
	}

	protected Message getChangedMessage() {
		return IMPLEMENTED_TYPE_MSGS.getChanged();
	}

	protected Message getRemovedMessage() {
		return IMPLEMENTED_TYPE_MSGS.getDeleted();
	}
}
