package me.jaks.jdns.console;

public class Record {
	private int id;
	private int domainId;
	private String name;
	private String type;
	private String content;
	private int ttl;
	private int priority;
	private int changeDate;
	private int disabled;
	private String orderName;
	private int auth;
	
	public Record() {
		
	}
	
	public Record(int id,int domainId,String name,String type,String content,int ttl,int priority,int changeDate,int disabled,String orderName,int auth) {
		this.id = id;
		this.domainId = domainId;
		this.name = name;
		this.type = type;
		this.content = content;
		this.ttl = ttl;
		this.priority = priority;
		this.changeDate = changeDate;
		this.disabled = disabled;
		this.orderName = orderName;
		this.auth = auth;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(int changeDate) {
		this.changeDate = changeDate;
	}
	public int getDisabled() {
		return disabled;
	}
	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	
	
}
