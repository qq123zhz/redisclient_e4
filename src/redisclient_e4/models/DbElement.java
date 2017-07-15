package redisclient_e4.models;

import com.cxy.redisclient.domain.Server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * @author Administrator
 *
 */
public class DbElement extends Element{
	@Expose
	private int serverId;
	@Expose
	private int dbId;
	@Expose
	private Server server;

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
	
	
	
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}

}
