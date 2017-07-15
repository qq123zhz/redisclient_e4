package redisclient_e4.models;

import org.eclipse.jface.util.Util;

import com.google.gson.Gson;

public class RedisTreeNode {

	/**
	 * The array of child tree nodes for this tree node. If there are no
	 * children, then this value may either by an empty array or
	 * <code>null</code>. There should be no <code>null</code> children in
	 * the array.
	 */
	private RedisTreeNode[] children;

	/**
	 * The parent tree node for this tree node. This value may be
	 * <code>null</code> if there is no parent.
	 */
	private RedisTreeNode parent;

	/**
	 * The value contained in this node. This value may be anything.
	 */
	protected Object value;

	/**
	 * Constructs a new instance of <code>TreeNode</code>.
	 *
	 * @param value
	 *            The value held by this node; may be anything.
	 */
	public RedisTreeNode(final Object value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof RedisTreeNode) {
			return Util.equals(this.value, ((RedisTreeNode) object).value);
		}

		return false;
	}

	/**
	 * Returns the child nodes. Empty arrays are converted to <code>null</code>
	 * before being returned.
	 *
	 * @return The child nodes; may be <code>null</code>, but never empty.
	 *         There should be no <code>null</code> children in the array.
	 */
	public RedisTreeNode[] getChildren() {
		if (children != null && children.length == 0) {
			return null;
		}
		return children;
	}

	/**
	 * Returns the parent node.
	 *
	 * @return The parent node; may be <code>null</code> if there are no
	 *         parent nodes.
	 */
	public RedisTreeNode getParent() {
		return parent;
	}

	/**
	 * Returns the value held by this node.
	 *
	 * @return The value; may be anything.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns whether the tree has any children.
	 *
	 * @return <code>true</code> if its array of children is not
	 *         <code>null</code> and is non-empty; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasChildren() {
		return children != null && children.length > 0;
	}

	@Override
	public int hashCode() {
		return Util.hashCode(value);
	}

	/**
	 * Sets the children for this node.
	 *
	 * @param children
	 *            The child nodes; may be <code>null</code> or empty. There
	 *            should be no <code>null</code> children in the array.
	 */
	public void setChildren(final RedisTreeNode[] children) {
		this.children = children;
	}

	/**
	 * Sets the parent for this node.
	 *
	 * @param parent
	 *            The parent node; may be <code>null</code>.
	 */
	public void setParent(final RedisTreeNode parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}

