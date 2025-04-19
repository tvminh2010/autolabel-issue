package zve.com.vn.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "tbl_workorder") // Bổ sung dòng này nếu CSDL là sql server
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrder {

	@Id
	String woNumber;
	Date issueDate;
	Date setupDate;
	String fgCode;
	String fgNumber;
	String oldFglNumber;
	String lineNo;
	String bobbinNumber;
	
	Date prductionStartDate;
	Date prductionEndDate;
	
	String prductionStartShift;
	String prductionEndShift;
	
	Integer productionQty;
	Integer wipQty;
	
	Integer numberOfSpindle;
	Integer numberOfOperator;
	String remark;
	String packingBoxNumber;

	String createBy;
	Date createAt;
	String approvedBy;
	Date approveAt;
	String woStatus;
	
	
	@OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<WorkOrderItem> items = new HashSet<>();
}
