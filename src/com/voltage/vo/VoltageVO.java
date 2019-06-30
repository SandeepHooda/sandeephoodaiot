package com.voltage.vo;

import java.util.ArrayList;
import java.util.List;

public class VoltageVO {
	private String _id;
	private float voltage;
	private String voltageDate;
	private List<VoltageData>  voltageData = new ArrayList<VoltageData>();
	private String recordingStartTime;
	private String recordingEndTime;
	public int data150below ;
	public int data150_160 ;
	public int data160_170 ;
	public int data170_180 ;
	public int data180_190 ;
	public int data190_200 ;
	public int data200Above ;
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
	public float getVoltage() {
		return voltage;
	}
	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}
	public String getRecordingStartTime() {
		return recordingStartTime;
	}
	public void setRecordingStartTime(String recordingStartTime) {
		this.recordingStartTime = recordingStartTime;
	}
	public String getRecordingEndTime() {
		return recordingEndTime;
	}
	public void setRecordingEndTime(String recordingEndTime) {
		this.recordingEndTime = recordingEndTime;
	}
	public int getData150below() {
		return data150below;
	}
	public void setData150below(int data150below) {
		this.data150below = data150below;
	}
	public int getData150_160() {
		return data150_160;
	}
	public void setData150_160(int data150_160) {
		this.data150_160 = data150_160;
	}
	public int getData160_170() {
		return data160_170;
	}
	public void setData160_170(int data160_170) {
		this.data160_170 = data160_170;
	}
	public int getData170_180() {
		return data170_180;
	}
	public void setData170_180(int data170_180) {
		this.data170_180 = data170_180;
	}
	public int getData180_190() {
		return data180_190;
	}
	public void setData180_190(int data180_190) {
		this.data180_190 = data180_190;
	}
	public int getData190_200() {
		return data190_200;
	}
	public void setData190_200(int data190_200) {
		this.data190_200 = data190_200;
	}
	public int getData200Above() {
		return data200Above;
	}
	public void setData200Above(int data200Above) {
		this.data200Above = data200Above;
	}
	public String getVoltageDate() {
		return voltageDate;
	}
	public void setVoltageDate(String voltageDate) {
		this.voltageDate = voltageDate;
	}
	
}
