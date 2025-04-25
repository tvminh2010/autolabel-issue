package zve.com.vn.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date issueDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date setupDate;
	String noCode;
	String fgNumber;
	String oldFglNumber;
	String lineNo;
	String bobbinNumber;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date productionStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date productionEndDate;
	
	String productionStartShift;
	String productionEndShift;
	
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
	
	/* ------------------------------------------------------------ */
	public boolean getCategoryCheckStatus() {
	    for (WorkOrderItem item : items) {
	        if ("PI".equalsIgnoreCase(item.getCategory())) {
	            return true;
	        }
	    }
	    return false;
	}
	/* ------------------------------------------------------------ */
}
