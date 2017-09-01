package com.pmp.read;

public class message {
	private header header;
	private serviceMeter serMeter;
	private location location;
	
	public header getHeader() {
		return header;
	}
	public void setHeader(header header) {
		this.header = header;
	}
	public serviceMeter getSerMeter() {
		return serMeter;
	}
	public void setSerMeter(serviceMeter serMeter) {
		this.serMeter = serMeter;
	}
	public location getLocation() {
		return location;
	}
	public void setLocation(location location) {
		this.location = location;
	}

}
