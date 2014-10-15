package org.incava.diffj.element;

import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.ast.Token;

import org.incava.diffj.util.ChangeType;
import org.incava.ijdk.text.Message;
import org.incava.pmdx.ItemUtil;

public class Access {
	public static final Message ACCESS_REMOVED = new Message(
			"access removed: {0}", ChangeType.ACCESS_REMOVED);
	public static final Message ACCESS_ADDED = new Message("access added: {0}",
			ChangeType.ACCESS_ADDED);
	public static final Message ACCESS_CHANGED = new Message(
			"access changed from {0} to {1}", ChangeType.ACCESS_CHANGED);

	private final SimpleNode node;

	public Access(SimpleNode node) {
		this.node = node;
	}

	public void diff(SimpleNode other, Differences differences) {
		Token fromAccess = ItemUtil.getAccess(node);
		Token toAccess = ItemUtil.getAccess(other);

		if (fromAccess == null) {
			if (toAccess != null) {
				differences.changed(node.getFirstToken(), toAccess,
						ACCESS_ADDED, toAccess.image);
			}
		} else if (toAccess == null) {
			differences.changed(fromAccess, other.getFirstToken(),
					ACCESS_REMOVED, fromAccess.image);
		} else if (!fromAccess.image.equals(toAccess.image)) {
			differences.changed(fromAccess, toAccess, ACCESS_CHANGED,
					fromAccess.image, toAccess.image);
		}
	}
}
