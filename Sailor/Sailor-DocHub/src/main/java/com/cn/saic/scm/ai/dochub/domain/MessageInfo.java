package com.cn.saic.scm.ai.dochub.domain;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "msgInfo")
public class MessageInfo extends AbstractAuditingEntity implements Serializable{

    @Id
    private String id;

    private String title;

    private String msgType;

    private String msgInfo;
    
    private HashMap<String,Object> data=new HashMap<String,Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}
    
	 public String toString() {
	    return "[ id ="+id+", title ="+title+", msgInfo="+msgInfo+" ]";
	}
}
