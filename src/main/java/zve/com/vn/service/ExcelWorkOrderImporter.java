package zve.com.vn.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import zve.com.vn.entity.WorkOrder;
import zve.com.vn.entity.WorkOrderItem;

@Component
public class ExcelWorkOrderImporter {
	private final WorkOrderService workOrderService;
	/* ---------------------------------------------------------------------------- */
	public ExcelWorkOrderImporter(WorkOrderService workOrderService) {
	    this.workOrderService = workOrderService;
	}
	/* ---------------------------------------------------------------------------- */
	public String importExcel(MultipartFile file) {
	    try (InputStream input = file.getInputStream()) {
	        Workbook workbook = WorkbookFactory.create(input);
	        Sheet sheet = workbook.getSheetAt(0);
	
	        WorkOrder currentWO = null;
	        Set<WorkOrderItem> items = null;
	
	        for (Row row : sheet) {
	            if (row.getRowNum() < 2) continue;
	
	            String colA = getCellValue(row.getCell(0));
	            String colC = getCellValue(row.getCell(2));
	            String colG = getCellValue(row.getCell(6));
	            String colR = getCellValue(row.getCell(17)); // soNumber
	
	            if (isEmpty(colR)) break; // Kết thúc nếu không còn soNumber
	
	            boolean isNewWorkOrder = row.getRowNum() == 2 || (!isEmpty(colA) && !isEmpty(colC) && !isEmpty(colG));
	
	            if (isNewWorkOrder) {
	                if (currentWO != null) {
	                    currentWO.setItems(items);
	                    workOrderService.save(currentWO);
	                }
	
	                currentWO = WorkOrder.builder()
	                    
	                    .issueDate(parseDate(row.getCell(0)))
	                    .setupDate(parseDate(row.getCell(1)))
	                    .woNumber(colC)
	                    .lineNo(getCellValue(row.getCell(3)))
	                    .oldFglNumber(getCellValue(row.getCell(4)))
	                    .fgNumber(getCellValue(row.getCell(6)))
	                    .numberOfSpindle(parseInt(row.getCell(7)))
	                    .numberOfOperator(parseInt(row.getCell(8)))
	                    .wipQty(parseInt(row.getCell(9)))
	                    .productionQty(parseInt(row.getCell(10)))
	                    .productionStartDate(parseDate(row.getCell(11)))
	                    .productionStartShift(getCellValue(row.getCell(12)))
	                    .productionEndDate(parseDate(row.getCell(13)))
	                    .productionEndShift(getCellValue(row.getCell(14)))
	                    .remark(getCellValue(row.getCell(15)))
	                    .woStatus("created")
	                    .createAt(new Date())
	                    .items(new HashSet<>())
	                    .build();
	                items = new HashSet<>();
	            }
	
	            if (currentWO != null) {
	                WorkOrderItem item = WorkOrderItem.builder()
	                    .category(getCellValue(row.getCell(16)))
	                    .soNumber(getCellValue(row.getCell(17)))
	                    .dueDate(parseDate(row.getCell(18)))
	                    .saleOderQty(parseInt(row.getCell(19)))
	                    .customerName(getCellValue(row.getCell(20)))
	                    .workOrder(currentWO)
	                    .build();
	                items.add(item);
	            }
	        }
	
	        if (currentWO != null) {
	            currentWO.setItems(items);
	            workOrderService.save(currentWO);
	        }
	
	        return "Import thành công!";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Lỗi khi import: " + e.getMessage();
	    }
	}
	/* ---------------------------------------------------------------------------- */
	/* ---------------------------------  Helpers --------------------------------- */
	/* ---------------------------------------------------------------------------- */ 
	    private boolean isEmpty(String val) {
	        return val == null || val.trim().isEmpty();
	    }
	    /* ---------------------------------------------------------------------------- */
	    private String getCellValue(Cell cell) {
	        if (cell == null) return "";
	        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
	        if (cell.getCellType() == CellType.NUMERIC) {
	            if (DateUtil.isCellDateFormatted(cell)) return cell.getDateCellValue().toString();
	            return String.valueOf((long) cell.getNumericCellValue());
	        }
	        return "";
	    }
	    /* ---------------------------------------------------------------------------- */
	    private Date parseDate(Cell cell) {
	        return (cell != null && DateUtil.isCellDateFormatted(cell)) ? cell.getDateCellValue() : null;
	    }
	    /* ---------------------------------------------------------------------------- */
	    private Integer parseInt(Cell cell) {
	        return (cell != null && cell.getCellType() == CellType.NUMERIC) ? (int) cell.getNumericCellValue() : null;
	    }
		/* ---------------------------------------------------------------------------- */
		/* ---------------------------------  End Helpers------------------------------ */
		/* ---------------------------------------------------------------------------- */ 
}
	