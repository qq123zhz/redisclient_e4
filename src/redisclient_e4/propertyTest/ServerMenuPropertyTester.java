package redisclient_e4.propertyTest;

import org.eclipse.core.expressions.PropertyTester;
import redisclient_e4.models.RedisTreeNode;

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Server;

/**
 * @author Administrator
 *
 */
public class ServerMenuPropertyTester extends PropertyTester {

	public ServerMenuPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean f = receiver instanceof RedisTreeNode;
		if(!f)return false;
		RedisTreeNode node = (RedisTreeNode) receiver;
		String expectStr = expectedValue.toString();
		if(expectStr.equals("selectRoot")){
			return rootMenu(node);
		}else if(expectStr.equals("selectServer")){
			return serverMenu(node);
		}else if(expectStr.equals("selectNode")){
			return dataNodeMenu(node);
		}
		return false;
		
	}

	/**
	 * @param node
	 * @return
	 */
	private boolean dataNodeMenu(RedisTreeNode node) {
		boolean f = node.getValue() instanceof DataNode;
		if(f)return true;
		return false;
	}

	/**
	 * @param node
	 * @return
	 */
	private boolean serverMenu(RedisTreeNode node) {
		boolean f = node.getValue() instanceof Server;
		if(f)return true;
		return false;
	}

	/**
	 * @param node
	 * @return
	 */
	private boolean rootMenu(RedisTreeNode node) {
		boolean f = node.getValue() instanceof String;
		if(f)return true;
		
		return false;
	}

}
