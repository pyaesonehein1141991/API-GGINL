/***************************************************************************************
 * @author <<Your Name>>
 * @Date 2013-02-11
 * @Version 1.0
 * @Purpose <<You have to write the comment the main purpose of this class>>
 * 
 *    
 ***************************************************************************************/
package org.tat.gginl.api.domains;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.tat.gginl.api.common.CommonCreateAndUpateMarks;
import org.tat.gginl.api.common.FormatID;
import org.tat.gginl.api.common.ReferenceType;
import org.tat.gginl.api.common.TableName;
import org.tat.gginl.api.common.Utils;


@Entity
@Table(name = TableName.ACCEPTEDINFO)
@TableGenerator(name = "ACCEPTEDINFO_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "ACCEPTEDINFO_GEN", allocationSize = 10)
@NamedQueries(value = { @NamedQuery(name = "AcceptedInfo.findByReferenceNo", query = "SELECT a FROM AcceptedInfo a WHERE a.referenceNo = :referenceNo AND a.referenceType = :referenceType") })
@Access(value = AccessType.FIELD)
public class AcceptedInfo implements Serializable {
	private static final long serialVersionUID = -7883787646075278917L;

	@Transient
	private String id;
	@Transient
	private String prefix;

	@Column(name = "REFERENCENO")
	private String referenceNo;

	@Column(name = "BASICPREMIUM")
	private double basicPremium;

	@Column(name = "ADDONPREMIUM")
	private double addOnPremium;

	@Column(name = "HOMEADDONPREMIUM")
	private double homeAddOnPremium;

	@Column(name = "HOMEBASICPREMIUM")
	private double homeBasicPremium;

	@Column(name = "RATE")
	private double rate;

	@Column(name = "ENDORSEMENTNETPREMIUM")
	private double endorsementNetPremium;

	@Column(name = "HOMEENDORSEMENTNETPREMIUM")
	private double homeEndorsementNetPremium;

	@Column(name = "ENDORSEMENTADDONPREMIUM")
	private double endorsementAddOnPremium;

	@Column(name = "HOMEENDORSEMENTADDONPREMIUM")
	private double homeEndorsementAddOnPremium;

	@Column(name = "DISCOUNTPERCENT")
	private double discountPercent;

	@Column(name = "STAMPFEES")
	private double stampFees;

	@Column(name = "HOMESTAMPFEES")
	private double homeStampFees;

	@Column(name = "ADMINISTRATIONFEES")
	private double administrationFees;

	@Column(name = "HOMEADMINISTRATIONFEES")
	private double homeAdministrationFees;

	@Column(name = "NCBPREMIUM")
	private Double ncbPremium;

	@Column(name = "HOMENCBPREMIUM")
	private Double homeNcbPremium;

	@Column(name = "PENALTYPREMIUM")
	private Double penaltyPremium;

	@Column(name = "HOMEPENALTYPREMIUM")
	private Double homePenaltyPremium;

	@Column(name = "SERVICECHARGES")
	private Double servicesCharges;

	@Column(name = "HOMESERVICECHARGES")
	private Double homeServicesCharges;

	@Column(name = "INFORMDATE")
	@Temporal(TemporalType.DATE)
	private Date informDate;

	@Column(name = "ENDORSEMENTINFORMDATE")
	@Temporal(TemporalType.DATE)
	private Date endorsementInformDate;

	@Column(name = "PREMIUMRATE")
	private double premiumRate;

	@Column(name = "REFERENCETYPE")
	@Enumerated(value = EnumType.STRING)
	private ReferenceType referenceType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENTTYPEID", referencedColumnName = "ID")
	private PaymentType paymentType;
	
	@Embedded
	private CommonCreateAndUpateMarks recorder;

	@Version
	private int version;

	public AcceptedInfo() {
		this.informDate = new Date();
		this.endorsementInformDate = new Date();
	}

	public double getHomeAddOnPremium() {
		return homeAddOnPremium;
	}

	public void setHomeAddOnPremium(double homeAddOnPremium) {
		this.homeAddOnPremium = homeAddOnPremium;
	}

	public double getHomeBasicPremium() {
		return homeBasicPremium;
	}

	public void setHomeBasicPremium(double homeBasicPremium) {
		this.homeBasicPremium = homeBasicPremium;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getHomeEndorsementNetPremium() {
		return homeEndorsementNetPremium;
	}

	public void setHomeEndorsementNetPremium(double homeEndorsementNetPremium) {
		this.homeEndorsementNetPremium = homeEndorsementNetPremium;
	}

	public double getHomeEndorsementAddOnPremium() {
		return homeEndorsementAddOnPremium;
	}

	public void setHomeEndorsementAddOnPremium(double homeEndorsementAddOnPremium) {
		this.homeEndorsementAddOnPremium = homeEndorsementAddOnPremium;
	}

	public double getHomeStampFees() {
		return homeStampFees;
	}

	public void setHomeStampFees(double homeStampFees) {
		this.homeStampFees = homeStampFees;
	}

	public double getHomeAdministrationFees() {
		return homeAdministrationFees;
	}

	public void setHomeAdministrationFees(double homeAdministrationFees) {
		this.homeAdministrationFees = homeAdministrationFees;
	}

	public Double getHomeNcbPremium() {
		return homeNcbPremium;
	}

	public void setHomeNcbPremium(Double homeNcbPremium) {
		this.homeNcbPremium = homeNcbPremium;
	}

	public Double getHomePenaltyPremium() {
		return homePenaltyPremium;
	}

	public void setHomePenaltyPremium(Double homePenaltyPremium) {
		this.homePenaltyPremium = homePenaltyPremium;
	}

	public double getPremiumRate() {
		return premiumRate;
	}

	public void setPremiumRate(double premiumRate) {
		this.premiumRate = premiumRate;
	}

	public Double getHomeServicesCharges() {
		return homeServicesCharges;
	}

	public void setHomeServicesCharges(Double homeServicesCharges) {
		this.homeServicesCharges = homeServicesCharges;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setNcbPremium(Double ncbPremium) {
		this.ncbPremium = ncbPremium;
	}

	public void setPenaltyPremium(Double penaltyPremium) {
		this.penaltyPremium = penaltyPremium;
	}

	/**
	 * Return discount amount on basic premium.
	 * 
	 * @return discount amount on basic premium
	 */
	public double getDiscountAmount() {
		return (discountPercent > 0.0) ? Utils.getPercentOf(discountPercent, basicPremium) : 0.0;
	}

	/**
	 * Return discount amount of on total endorsement premium.
	 * 
	 * @return discount amount of on total endorsement premium
	 */
	public double getEndorseDiscountAmount() {
		return (discountPercent > 0.0) ? Utils.getPercentOf(discountPercent, getTotalEndorsementPremium()) : 0.0;
	}

	/**
	 * Return proposal total premium.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * total premium = basic premium + addOn premium.
	 * 
	 * @return proposal total premium
	 */
	public double getTotalPremium() {
		return getBasicPremium() + getAddOnPremium() + getPenaltyPremium();
	}

	/**
	 * Return proposal total renewal premium.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * total renewal premium = (basic premium - ncb premium) + addOn premium.
	 * 
	 * @return proposal total renewal premium
	 */
	public double getTotalRenewalPremium() {
		double totalPremium = Utils.getTwoDecimalPoint((getBasicPremium() - getNcbPremium()) + getPenaltyPremium() + getAddOnPremium());
		return (getBasicPremium() - getNcbPremium()) + getPenaltyPremium() + getAddOnPremium();
	}

	/**
	 * Return proposal total endorsement premium.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is <br/>
	 * total endorsement premium = endorsement of basic premium + endorsement of
	 * addOn premium
	 * 
	 * @return proposal total endorsement premium
	 */
	public double getTotalEndorsementPremium() {
		double totalEndorsementPremium = Utils.getTwoDecimalPoint(getEndorsementNetPremium() + getEndorsementAddOnPremium());
		return Utils.getTwoDecimalPoint(totalEndorsementPremium);
	}

	/**
	 * Return proposal net premium, total premium exclude discount.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * net premium = total premium - discount amount
	 * 
	 * @return proposal net premium
	 */
	public double getNetPremium() {
		double netPremium = Utils.getTwoDecimalPoint(getTotalPremium() - getDiscountAmount() - getNcbPremium());
		return Utils.getTwoDecimalPoint(netPremium);
	}

	/**
	 * Return proposal renewal net premium, total premium exclude discount.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * net premium = total renewal premium - discount amount
	 * 
	 * @return proposal renewal net premium
	 */
	public double getRenewalNetPremium() {
		double netPremium = Utils.getTwoDecimalPoint(getTotalRenewalPremium() - getDiscountAmount());
		return Utils.getTwoDecimalPoint(netPremium);
	}

	/**
	 * Return proposal net endorsement premium, total endorsement premium
	 * exclude discount.<br/>
	 * Return value is two decimal point with round mode. <br/>
	 * Formula is<br/>
	 * net endorsement premium = total endorsement premium - discount amount
	 * 
	 * @return proposal net endorsement premium
	 */
	public double getNetEndorsementPremium() {
		double netEndorsementPremium = Utils.getTwoDecimalPoint(getTotalEndorsementPremium() - getEndorseDiscountAmount());
		return Utils.getTwoDecimalPoint(netEndorsementPremium);
	}

	/**
	 * Return proposal total amount to be paid.<br/>
	 * Calculate with exclude discount amount, include service charges and stamp
	 * fee.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * total amount = net premium + service charges + stamp fee
	 * 
	 * @return proposal total amount to be paid
	 */
	public double getTotalAmount() {
		double totalAmount = getNetPremium() + servicesCharges.doubleValue() + stampFees;
		return Utils.getTwoDecimalPoint(totalAmount);
	}

	/**
	 * Return proposal renewal total amount to be paid.<br/>
	 * Calculate with exclude discount amount, include service charges and stamp
	 * fee.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * total amount = renewal net premium + service charges + stamp fee
	 * 
	 * @return proposal renewal total amount to be paid
	 */
	public double getTotalRenewalAmount() {
		double totalAmount = getRenewalNetPremium() + servicesCharges.doubleValue() + stampFees;
		return Utils.getTwoDecimalPoint(totalAmount);
	}

	/**
	 * Return proposal total amount to be paid for endorsement.<br/>
	 * Calculate with exclude discount amount, include service charges, stamp
	 * fee and administration fees.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is<br/>
	 * total amount = net premium + service charges + stamp fee + administration
	 * fee
	 * 
	 * @return proposal total amount to be paid for endorsement
	 */
	public double getTotalEndorsementAmount() {
		double totalEndorsementAmount = getNetEndorsementPremium() + servicesCharges.doubleValue() + stampFees + administrationFees;
		return Utils.getTwoDecimalPoint(totalEndorsementAmount);
	}

	/**
	 * Return premium to be paid for each payment.
	 * 
	 * @return
	 */
	public double getNetTermPremium() {
		int monthly = paymentType.getMonth();
		if (monthly > 0 && !referenceType.equals(ReferenceType.MEDICAL_PROPOSAL)) {
			return Utils.divide(monthly * getNetPremium(), 12);
		} else {
			return getNetPremium();
		}
	}

	/**
	 * Return premium to be paid for each payment with service charges and stamp
	 * fee.<br/>
	 * Return value is two decimal point with round mode.<br/>
	 * Formula is <br/>
	 * net term amount = premium to be paid each payment + service charges +
	 * stamp fee
	 * 
	 * @return premium to be paid for each payment with service charges and
	 *         stamp fee
	 */
	public double getNetTermAmount() {
		double netTermPremium = getNetTermPremium() + servicesCharges.doubleValue() + stampFees;
		return Utils.getTwoDecimalPoint(netTermPremium);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ACCEPTEDINFO_GEN")
	@Access(value = AccessType.PROPERTY)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null) {
			this.id = FormatID.formatId(id, getPrefix(), 10);
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public ReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(ReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public double getBasicPremium() {
		return basicPremium;
	}

	public void setBasicPremium(double basicPremium) {
		this.basicPremium = basicPremium;
	}

	public double getAddOnPremium() {
		return addOnPremium;
	}

	public void setAddOnPremium(double addOnPremium) {
		this.addOnPremium = addOnPremium;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Double getServicesCharges() {
		if (servicesCharges == null) {
			servicesCharges = new Double(0.0);
		}
		return servicesCharges;
	}

	public void setServicesCharges(Double servicesCharges) {
		this.servicesCharges = servicesCharges;
	}

	public Number getServicesChargesNum() {
		if (servicesCharges == null) {
			servicesCharges = new Double(0.0);
		}
		return servicesCharges;
	}

	public void setServicesChargesNum(Number servicesCharges) {
		if (servicesCharges != null) {
			this.servicesCharges = servicesCharges.doubleValue();
		}
	}

	public double getStampFees() {
		return stampFees;
	}

	public void setStampFees(double stampFees) {
		this.stampFees = stampFees;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public double getEndorsementNetPremium() {
		return endorsementNetPremium;
	}

	public void setEndorsementNetPremium(double endorsementNetPremium) {
		this.endorsementNetPremium = endorsementNetPremium;
	}

	public double getEndorsementAddOnPremium() {
		return endorsementAddOnPremium;
	}

	public void setEndorsementAddOnPremium(double endorsementAddOnPremium) {
		this.endorsementAddOnPremium = endorsementAddOnPremium;
	}

	public double getAdministrationFees() {
		return administrationFees;
	}

	public void setAdministrationFees(double administrationFees) {
		this.administrationFees = administrationFees;
	}

	public Date getInformDate() {
		return informDate;
	}

	public void setInformDate(Date informDate) {
		this.informDate = informDate;
	}

	public Date getEndorsementInformDate() {
		return endorsementInformDate;
	}

	public void setEndorsementInformDate(Date endorsementInformDate) {
		this.endorsementInformDate = endorsementInformDate;
	}

	public double getNcbPremium() {
		return (ncbPremium == null) ? 0.0 : ncbPremium.doubleValue();
	}

	public void setNcbPremium(double ncbPremium) {
		this.ncbPremium = ncbPremium;
	}

	public Number getNcbPremiumNum() {
		if (ncbPremium == null) {
			ncbPremium = new Double(0.0);
		}
		return ncbPremium;
	}

	public void setNcbPremiumNum(Number ncbPremium) {
		if (ncbPremium != null) {
			this.ncbPremium = ncbPremium.doubleValue();
		}
	}

	public double getPenaltyPremium() {
		return (penaltyPremium == null) ? 0.0 : penaltyPremium.doubleValue();
	}

	public void setPenaltyPremium(double penaltyPremium) {
		this.penaltyPremium = penaltyPremium;
	}

	public Number getPenaltyPremiumNum() {
		if (penaltyPremium == null) {
			penaltyPremium = new Double(0.0);
		}
		return penaltyPremium;
	}

	public void setPenaltyPremiumNum(Number penaltyPremium) {
		if (ncbPremium != null) {
			this.penaltyPremium = penaltyPremium.doubleValue();
		}
	}
	
	public CommonCreateAndUpateMarks getRecorder() {
		return recorder;
	}

	public void setRecorder(CommonCreateAndUpateMarks recorder) {
		this.recorder = recorder;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(addOnPremium);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(administrationFees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(basicPremium);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(discountPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(endorsementAddOnPremium);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((endorsementInformDate == null) ? 0 : endorsementInformDate.hashCode());
		temp = Double.doubleToLongBits(endorsementNetPremium);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((informDate == null) ? 0 : informDate.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((referenceNo == null) ? 0 : referenceNo.hashCode());
		result = prime * result + ((referenceType == null) ? 0 : referenceType.hashCode());
		result = prime * result + ((servicesCharges == null) ? 0 : servicesCharges.hashCode());
		result = prime * result + ((recorder == null) ? 0 : recorder.hashCode());
		temp = Double.doubleToLongBits(stampFees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcceptedInfo other = (AcceptedInfo) obj;
		if (Double.doubleToLongBits(addOnPremium) != Double.doubleToLongBits(other.addOnPremium))
			return false;
		if (Double.doubleToLongBits(administrationFees) != Double.doubleToLongBits(other.administrationFees))
			return false;
		if (Double.doubleToLongBits(basicPremium) != Double.doubleToLongBits(other.basicPremium))
			return false;
		if (Double.doubleToLongBits(discountPercent) != Double.doubleToLongBits(other.discountPercent))
			return false;
		if (Double.doubleToLongBits(endorsementAddOnPremium) != Double.doubleToLongBits(other.endorsementAddOnPremium))
			return false;
		if (endorsementInformDate == null) {
			if (other.endorsementInformDate != null)
				return false;
		} else if (!endorsementInformDate.equals(other.endorsementInformDate))
			return false;
		if (Double.doubleToLongBits(endorsementNetPremium) != Double.doubleToLongBits(other.endorsementNetPremium))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (informDate == null) {
			if (other.informDate != null)
				return false;
		} else if (!informDate.equals(other.informDate))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (referenceNo == null) {
			if (other.referenceNo != null)
				return false;
		} else if (!referenceNo.equals(other.referenceNo))
			return false;
		if (referenceType != other.referenceType)
			return false;
		if (servicesCharges == null) {
			if (other.servicesCharges != null)
				return false;
		} else if (!servicesCharges.equals(other.servicesCharges))
			return false;
		if (Double.doubleToLongBits(stampFees) != Double.doubleToLongBits(other.stampFees))
			return false;
		if (recorder == null) {
			if (other.recorder != null)
				return false;
		} else if (!recorder.equals(other.recorder))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

}