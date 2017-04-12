package models;

public class Search {
	private long start;
	private long end;
	private int bandwidth;
	private boolean success;
	
	public Search(long start) {
		this.start = start;
		this.success = false;
	}
	
	public void setEnd(long end) {
		this.end = end;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public void addBandwidth() {
		bandwidth++;
	}
}
