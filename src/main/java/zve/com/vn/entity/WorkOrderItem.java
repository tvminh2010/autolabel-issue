package zve.com.vn.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tbl_workorder_item") // Bổ sung dòng này nếu CSDL là sql server
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkOrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;
	String category;
	String soNumber;
	Date dueDate;
	Integer saleOderQty;
	String customerName;
	String saleConfirmedBy;
	Date saleConfirmedAt;
	
	@ManyToOne
	@JoinColumn(name="wo_id", nullable=false)
	private WorkOrder workOrder;

}
