/**
 *@Generated by sagacity-quickvo 4.13
 */
package com.sagframe.sqltoy.showcase.vo.base;

import java.io.Serializable;
import org.sagacity.sqltoy.config.annotation.Entity;
import org.sagacity.sqltoy.config.annotation.Id;
import org.sagacity.sqltoy.config.annotation.Column;
import org.sagacity.sqltoy.config.annotation.BusinessId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @project sqltoy-showcase
 * @version 1.0.0
 * Table: sqltoy_device_order_info,Remark:硬件购销定单表(演示有规则单号) 
 * pk_constraint only for postgresql  
 */
@Entity(tableName="sqltoy_device_order_info",pk_constraint="PRIMARY")
public abstract class AbstractDeviceOrderInfoVO implements Serializable,
	java.lang.Cloneable {
	 /*--------------- properties string,handier to copy ---------------------*/
	 //full properties 
	 //orderId,deviceType,psType,totalCnt,totalAmt,buyer,saler,transDate,deliveryTerm,staffId,organId,createBy,createTime,updateBy,updateTime,status
	 
	 //not null properties
	 //orderId,deviceType,psType,transDate,createBy,createTime,updateBy,updateTime,status

	/**
	 * 
	 */
	private static final long serialVersionUID = 5826907742519022861L;
	
	/**
	 * 订单ID
	 */
	@Id
	@BusinessId(generator="org.sagacity.sqltoy.plugins.id.RedisIdGenerator",signature="${psType}@case(${deviceType},PC,PC,NET,NT,OFFICE,OF,SOFTWARE,SF,OT)@day(yyMMdd)",relatedColumns={"psType","deviceType"},length=12,sequenceSize=-1)
	@Column(name="ORDER_ID",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String orderId;
	
	/**
	 * 设备类型
	 */
	@Column(name="DEVICE_TYPE",length=10L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String deviceType;
	
	/**
	 * 购销标志
	 */
	@Column(name="PS_TYPE",length=10L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String psType;
	
	/**
	 * 商品总量
	 */
	@Column(name="TOTAL_CNT",length=12L,type=java.sql.Types.DECIMAL,nullable=true)
	protected BigDecimal totalCnt;
	
	/**
	 * 总金额
	 */
	@Column(name="TOTAL_AMT",length=12L,type=java.sql.Types.DECIMAL,nullable=true)
	protected BigDecimal totalAmt;
	
	/**
	 * 购买方
	 */
	@Column(name="BUYER",length=22L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String buyer;
	
	/**
	 * 销售方
	 */
	@Column(name="SALER",length=22L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String saler;
	
	/**
	 * 成交日期
	 */
	@Column(name="TRANS_DATE",length=10L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDate transDate;
	
	/**
	 * 交货期限
	 */
	@Column(name="DELIVERY_TERM",length=10L,type=java.sql.Types.DATE,nullable=true)
	protected LocalDate deliveryTerm;
	
	/**
	 * 业务员
	 */
	@Column(name="STAFF_ID",length=22L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String staffId;
	
	/**
	 * 业务机构
	 */
	@Column(name="ORGAN_ID",length=22L,type=java.sql.Types.VARCHAR,nullable=true)
	protected String organId;
	
	/**
	 * 创建人
	 */
	@Column(name="CREATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String createBy;
	
	/**
	 * 创建时间
	 */
	@Column(name="CREATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime createTime;
	
	/**
	 * 最后修改人
	 */
	@Column(name="UPDATE_BY",length=22L,type=java.sql.Types.VARCHAR,nullable=false)
	protected String updateBy;
	
	/**
	 * 最后修改时间
	 */
	@Column(name="UPDATE_TIME",length=19L,type=java.sql.Types.DATE,nullable=false)
	protected LocalDateTime updateTime;
	
	/**
	 * 状态
	 */
	@Column(name="STATUS",length=10L,type=java.sql.Types.INTEGER,nullable=false)
	protected Integer status;
	


	/** default constructor */
	public AbstractDeviceOrderInfoVO() {
	}
	
	/** pk constructor */
	public AbstractDeviceOrderInfoVO(String orderId)
	{
		this.orderId=orderId;
	}

	/** minimal constructor */
	public AbstractDeviceOrderInfoVO(String orderId,String deviceType,String psType,LocalDate transDate,String createBy,LocalDateTime createTime,String updateBy,LocalDateTime updateTime,Integer status)
	{
		this.orderId=orderId;
		this.deviceType=deviceType;
		this.psType=psType;
		this.transDate=transDate;
		this.createBy=createBy;
		this.createTime=createTime;
		this.updateBy=updateBy;
		this.updateTime=updateTime;
		this.status=status;
	}

	/** full constructor */
	public AbstractDeviceOrderInfoVO(String orderId,String deviceType,String psType,BigDecimal totalCnt,BigDecimal totalAmt,String buyer,String saler,LocalDate transDate,LocalDate deliveryTerm,String staffId,String organId,String createBy,LocalDateTime createTime,String updateBy,LocalDateTime updateTime,Integer status)
	{
		this.orderId=orderId;
		this.deviceType=deviceType;
		this.psType=psType;
		this.totalCnt=totalCnt;
		this.totalAmt=totalAmt;
		this.buyer=buyer;
		this.saler=saler;
		this.transDate=transDate;
		this.deliveryTerm=deliveryTerm;
		this.staffId=staffId;
		this.organId=organId;
		this.createBy=createBy;
		this.createTime=createTime;
		this.updateBy=updateBy;
		this.updateTime=updateTime;
		this.status=status;
	}
	
	/**
	 *@param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId=orderId;
	}
		
	/**
	 *@return the OrderId
	 */
	public String getOrderId() {
	    return this.orderId;
	}
	
	/**
	 *@param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType=deviceType;
	}
		
	/**
	 *@return the DeviceType
	 */
	public String getDeviceType() {
	    return this.deviceType;
	}
	
	/**
	 *@param psType the psType to set
	 */
	public void setPsType(String psType) {
		this.psType=psType;
	}
		
	/**
	 *@return the PsType
	 */
	public String getPsType() {
	    return this.psType;
	}
	
	/**
	 *@param totalCnt the totalCnt to set
	 */
	public void setTotalCnt(BigDecimal totalCnt) {
		this.totalCnt=totalCnt;
	}
		
	/**
	 *@return the TotalCnt
	 */
	public BigDecimal getTotalCnt() {
	    return this.totalCnt;
	}
	
	/**
	 *@param totalAmt the totalAmt to set
	 */
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt=totalAmt;
	}
		
	/**
	 *@return the TotalAmt
	 */
	public BigDecimal getTotalAmt() {
	    return this.totalAmt;
	}
	
	/**
	 *@param buyer the buyer to set
	 */
	public void setBuyer(String buyer) {
		this.buyer=buyer;
	}
		
	/**
	 *@return the Buyer
	 */
	public String getBuyer() {
	    return this.buyer;
	}
	
	/**
	 *@param saler the saler to set
	 */
	public void setSaler(String saler) {
		this.saler=saler;
	}
		
	/**
	 *@return the Saler
	 */
	public String getSaler() {
	    return this.saler;
	}
	
	/**
	 *@param transDate the transDate to set
	 */
	public void setTransDate(LocalDate transDate) {
		this.transDate=transDate;
	}
		
	/**
	 *@return the TransDate
	 */
	public LocalDate getTransDate() {
	    return this.transDate;
	}
	
	/**
	 *@param deliveryTerm the deliveryTerm to set
	 */
	public void setDeliveryTerm(LocalDate deliveryTerm) {
		this.deliveryTerm=deliveryTerm;
	}
		
	/**
	 *@return the DeliveryTerm
	 */
	public LocalDate getDeliveryTerm() {
	    return this.deliveryTerm;
	}
	
	/**
	 *@param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId=staffId;
	}
		
	/**
	 *@return the StaffId
	 */
	public String getStaffId() {
	    return this.staffId;
	}
	
	/**
	 *@param organId the organId to set
	 */
	public void setOrganId(String organId) {
		this.organId=organId;
	}
		
	/**
	 *@return the OrganId
	 */
	public String getOrganId() {
	    return this.organId;
	}
	
	/**
	 *@param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy=createBy;
	}
		
	/**
	 *@return the CreateBy
	 */
	public String getCreateBy() {
	    return this.createBy;
	}
	
	/**
	 *@param createTime the createTime to set
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime=createTime;
	}
		
	/**
	 *@return the CreateTime
	 */
	public LocalDateTime getCreateTime() {
	    return this.createTime;
	}
	
	/**
	 *@param updateBy the updateBy to set
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy=updateBy;
	}
		
	/**
	 *@return the UpdateBy
	 */
	public String getUpdateBy() {
	    return this.updateBy;
	}
	
	/**
	 *@param updateTime the updateTime to set
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime=updateTime;
	}
		
	/**
	 *@return the UpdateTime
	 */
	public LocalDateTime getUpdateTime() {
	    return this.updateTime;
	}
	
	/**
	 *@param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status=status;
	}
		
	/**
	 *@return the Status
	 */
	public Integer getStatus() {
	    return this.status;
	}



	/**
     * @todo vo columns to String
     */
    @Override
	public String toString() {
		StringBuilder columnsBuffer=new StringBuilder();
		columnsBuffer.append("orderId=").append(getOrderId()).append("\n");
		columnsBuffer.append("deviceType=").append(getDeviceType()).append("\n");
		columnsBuffer.append("psType=").append(getPsType()).append("\n");
		columnsBuffer.append("totalCnt=").append(getTotalCnt()).append("\n");
		columnsBuffer.append("totalAmt=").append(getTotalAmt()).append("\n");
		columnsBuffer.append("buyer=").append(getBuyer()).append("\n");
		columnsBuffer.append("saler=").append(getSaler()).append("\n");
		columnsBuffer.append("transDate=").append(getTransDate()).append("\n");
		columnsBuffer.append("deliveryTerm=").append(getDeliveryTerm()).append("\n");
		columnsBuffer.append("staffId=").append(getStaffId()).append("\n");
		columnsBuffer.append("organId=").append(getOrganId()).append("\n");
		columnsBuffer.append("createBy=").append(getCreateBy()).append("\n");
		columnsBuffer.append("createTime=").append(getCreateTime()).append("\n");
		columnsBuffer.append("updateBy=").append(getUpdateBy()).append("\n");
		columnsBuffer.append("updateTime=").append(getUpdateTime()).append("\n");
		columnsBuffer.append("status=").append(getStatus()).append("\n");
		return columnsBuffer.toString();
	}
}
