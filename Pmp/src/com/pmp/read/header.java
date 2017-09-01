package com.pmp.read;

public class header {
    private String messageId;
    private String masterMsgId;
    private String moduleCode;
    private String moduleTime;
    private String receivedTime;
    private String owner;    
	private equipament equipament;


	public equipament getEquipament() {
		return equipament;
	}

	public void setEquipament(equipament equipament) {
		this.equipament = equipament;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMasterMsgId() {
		return masterMsgId;
	}

	public void setMasterMsgId(String masterMsgId) {
		this.masterMsgId = masterMsgId;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleTime() {
		return moduleTime;
	}

	public void setModuleTime(String moduleTime) {
		this.moduleTime = moduleTime;
	}

	public String getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
