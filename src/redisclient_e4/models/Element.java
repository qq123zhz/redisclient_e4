package redisclient_e4.models;

import com.cxy.redisclient.domain.NodeType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Element {
	@Expose
	protected String name;
	@Expose
	protected NodeType type;
	@Expose
	protected long size;
	
	protected Gson gson ; 

	public Element() {
		gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); 
	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public NodeType getType() {
		return type;
	}



	public void setType(NodeType type) {
		this.type = type;
	}



	public long getSize() {
		return size;
	}



	public void setSize(long size) {
		this.size = size;
	}



	@Override
	public String toString() {
		return gson.toJson(this);
	}
}