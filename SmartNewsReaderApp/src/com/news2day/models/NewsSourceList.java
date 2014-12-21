package com.news2day.models;

public class NewsSourceList {
	int sourceId;
	String sourceTitle;
	int sourceImage;
	private String count = "0";
	private boolean isCounterVisible = false;

	public NewsSourceList() {
	}

	public NewsSourceList(String sourceTitle, int sourceImage){
	this.sourceTitle = sourceTitle;
	this.sourceImage = sourceImage;
	}

	public NewsSourceList(String sourceTitle, int sourceImage, boolean isCounterVisible, String count){
			this.sourceTitle = sourceTitle;
			this.sourceImage = sourceImage;
			this.count = count;;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	public void setCounterVisible(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
	public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }
	public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public int getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(int sourceImage) {
		this.sourceImage = sourceImage;
	}
}
