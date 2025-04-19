package zve.com.vn.service;

import java.util.List;

import zve.com.vn.entity.WorkOrderItem;

public interface WorkOrderItemService {
	WorkOrderItem save(WorkOrderItem item);
    List<WorkOrderItem> findAll();
}
