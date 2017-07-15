package redisclient_e4.models;

import com.cxy.redisclient.domain.DataNode;
import com.google.gson.annotations.Expose;

/**
 * @author Administrator
 *
 */
public class KeysElement extends Element{
	@Expose
	private int serverId;
	@Expose
	private int dbId;
	
	private DataNode dataNode;
	@Expose
	private DbElement dbElement;
	 
	public KeysElement(DataNode node) {
		 this.name = node.getKey();
		 this.serverId = node.getId();
		 this.dbId = node.getDb();
		 this.type = node.getType();
		 this.size = node.getSize();
		 this.dataNode = node;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getDbId() {
		return dbId;
	}
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
	public DataNode getDataNode() {
		return dataNode;
	}
	public void setDataNode(DataNode dataNode) {
		this.dataNode = dataNode;
	}
	public DbElement getDbElement() {
		return dbElement;
	}
	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}

}
