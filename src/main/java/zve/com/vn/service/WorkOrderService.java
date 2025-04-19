package zve.com.vn.service;

import java.util.List;
import java.util.Optional;

import zve.com.vn.entity.WorkOrder;

public interface WorkOrderService {
	WorkOrder save(WorkOrder workOrder);
	List<WorkOrder> findAll();
	Optional<WorkOrder> findByWoNumber(String woNumber);
}
