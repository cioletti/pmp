package com.pmp.read;

public class NextBuffer {
	private String url;
	private boolean moreData;
	
	public NextBuffer() {
		super();
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isMoreData() {
		return moreData;
	}
	public void setMoreData(boolean moreData) {
		this.moreData = moreData;
	}
}
