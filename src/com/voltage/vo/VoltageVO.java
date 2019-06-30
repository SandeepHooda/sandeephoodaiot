package com.voltage.vo;

import java.util.ArrayList;
import java.util.List;

public class VoltageVO {
	private String _id;
	private List<VoltageData>  voltageData = new ArrayList<VoltageData>();
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public List<VoltageData> getVoltageData() {
		return voltageData;
	}
	public void setVoltageData(List<VoltageData> voltageData) {
		this.voltageData = voltageData;
	}

}
