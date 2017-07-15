package redisclient_e4.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import redisclient_e4.models.RedisTreeNode;

/**
 * @author Administrator
 *
 */
public class NodeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// Do nothing
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		final RedisTreeNode node = (RedisTreeNode) parentElement;
		return node.getChildren();
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof RedisTreeNode[]) {
			return (RedisTreeNode[]) inputElement;
		}
		return new Object[0];
	}

	@Override
	public Object getParent(final Object element) {
		final RedisTreeNode node = (RedisTreeNode) element;
		return node.getParent();
	}

	@Override
	public boolean hasChildren(final Object element) {
		final RedisTreeNode node = (RedisTreeNode) element;
		return node.hasChildren();
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
		// Do nothing
	}
}

