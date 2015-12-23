package cc.easyandroid.easysimple;

import java.io.Serializable;


public class UserInfo implements Serializable{
	private String sessionId;
	private String deptName;
	private String brokerPhone;
	private int cityId;
	private String brokerIcon;// 经济人图像
	private String brokerName;// 经纪人name
	private String companyName;// 公司名称
	private int brokerId;
	private String city;
	private String idCard;
	private String brokerLoginName;

	private int guidNum;// ”带看数
	private int reserveNum;// 报备数
	private int dealNum;// 成交数

	private String nid;//  
	private String companyBrokerPhone;// =13530145721;
	private String companyBroker;//=刘娟;// =13530145721;

	
	public String getCompanyBroker() {
		return companyBroker;
	}

	public void setCompanyBroker(String companyBroker) {
		this.companyBroker = companyBroker;
	}

	public String getCompanyBrokerPhone() {
		return companyBrokerPhone;
	}

	public void setCompanyBrokerPhone(String companyBrokerPhone) {
		this.companyBrokerPhone = companyBrokerPhone;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBrokerLoginName() {
		return brokerLoginName;
	}

	public void setBrokerLoginName(String brokerLoginName) {
		this.brokerLoginName = brokerLoginName;
	}

	public int getGuidNum() {
		return guidNum;
	}

	public void setGuidNum(int guidNum) {
		this.guidNum = guidNum;
	}

	public int getReserveNum() {
		return reserveNum;
	}

	public void setReserveNum(int reserveNum) {
		this.reserveNum = reserveNum;
	}

	public int getDealNum() {
		return dealNum;
	}

	public void setDealNum(int dealNum) {
		this.dealNum = dealNum;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBrokerPhone() {
		return brokerPhone;
	}

	public void setBrokerPhone(String brokerPhone) {
		this.brokerPhone = brokerPhone;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getBrokerIcon() {
		return brokerIcon;
	}

	public void setBrokerIcon(String brokerIcon) {
		this.brokerIcon = brokerIcon;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
