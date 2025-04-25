package zve.com.vn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import zve.com.vn.entity.WorkOrderItem;

public interface WorkOrderItemRepository extends JpaRepository<WorkOrderItem, String> {
	Optional<WorkOrderItem> findBySoNumber(String soNumber);
}
