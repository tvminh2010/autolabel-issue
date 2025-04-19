package zve.com.vn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import zve.com.vn.entity.WorkOrderItem;
import zve.com.vn.repository.WorkOrderItemRepository;

@Service
public class WorkOrderItemServiceImpl implements WorkOrderItemService {
	
	private final WorkOrderItemRepository repository;
	/* ---------------------------------------------------- */
    public WorkOrderItemServiceImpl(WorkOrderItemRepository repository) {
        this.repository = repository;
    }
    
	/* ---------------------------------------------------- */
	@Override
	public WorkOrderItem save(WorkOrderItem item) {
		 return repository.save(item);
	}
	
	/* ---------------------------------------------------- */
	@Override
	public List<WorkOrderItem> findAll() {
		return repository.findAll();
	}
	/* ---------------------------------------------------- */
}
