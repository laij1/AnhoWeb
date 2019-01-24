
public class InvoiceRecord {
	Integer rid;
	String createOn;
	String medicineName;
	Integer subtotal;
	String chargeOn;
	String receiptOn;
	String patientName;

	
	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public InvoiceRecord(Integer rid, String createOn, String medicine, Integer subtotal) {
		this.rid = rid;
		this.createOn = createOn;
		this.medicineName = medicine;
		this.subtotal = subtotal;
		
	
	}
	
	public Integer getRid() {
		return rid;
	}
	
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	
	public String getCreateOn() {
		return createOn;
	}
	
	public void setCreateOn(String createOn) {
		this.createOn = createOn;
	}
	
	public String getMedicineName() {
		return medicineName;
	}
	
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	public Integer getSubtotal() {
		return subtotal;
	}
	
	public void setSubtotal(Integer subtotal) {
		this.subtotal = subtotal;
	}
	
	
	public void setChargeOn(String chargeOn) {
		if(chargeOn == null) {
			this.chargeOn = "";
		} else {
			this.chargeOn = chargeOn;
		}
		
	}
	
	public String getChargeOn() {
		if(chargeOn == null) {
			return "";
		}
		return chargeOn;
	}
	
	public void setReceiptOn(String receiptOn) {
		if(receiptOn == null) {
			this.receiptOn = "";
		} else {
			this.receiptOn = receiptOn;
		}
		
	}
	
	public String getReceiptOn() {
		if(receiptOn == null) {
			return "";
		}
		return receiptOn;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createOn == null) ? 0 : createOn.hashCode());
		result = prime * result + ((medicineName == null) ? 0 : medicineName.hashCode());
		result = prime * result + ((subtotal == null) ? 0 : subtotal.hashCode());
		result += rid.intValue();
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
		InvoiceRecord other = (InvoiceRecord) obj;
		return (other.rid.intValue() == rid.intValue());
	}
	
	
	

}

