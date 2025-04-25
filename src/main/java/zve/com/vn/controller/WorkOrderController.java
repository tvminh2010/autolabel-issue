package zve.com.vn.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import zve.com.vn.entity.WorkOrder;
import zve.com.vn.entity.WorkOrderItem;
import zve.com.vn.service.ExcelWorkOrderImporter;
import zve.com.vn.service.WorkOrderItemServiceImpl;
import zve.com.vn.service.WorkOrderServiceImpl;



@Controller
@RequestMapping("/workorder")
public class WorkOrderController {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date issueDate;

	@Autowired
	private ExcelWorkOrderImporter excelImporter;
	
	@Autowired
	private WorkOrderServiceImpl service;
	
	@Autowired
	private WorkOrderItemServiceImpl woItemservice;
	/* ---------------------------------------------------- */
	@GetMapping({"","/edit"})
	 public String editWorkOrder(
			 @RequestParam(value = "wo_id", required = false) String woNumber, 
			 Model model) {
		
		 WorkOrder workOrder = new WorkOrder(); 
		 
		 List<WorkOrder> woorderlist = service.findAll();
		 if (woNumber != null) {
			 Optional<WorkOrder> workOrderOpt = service.findByWoNumber(woNumber);
			 if (workOrderOpt.isPresent()) {
				 	workOrder = workOrderOpt.get();
			 } 
		 }
		 model.addAttribute("workOrder", workOrder);
		 model.addAttribute("woorderlist", woorderlist);
		 return "workorder"; 
	}
	/* ---------------------------------------------------- */
	@GetMapping("/salecheck")
	public String saleCheck(
	        @RequestParam(value = "wo_id", required = false) String woNumber,
	        @RequestParam(value = "so_no", required = false) String soNumber,
	        Model model,
	        RedirectAttributes redirectAttributes) {

	    if (woNumber == null) {
	        redirectAttributes.addFlashAttribute("message", "Chưa chọn WorkOrder để kiểm tra.");
	        return "redirect:/workorder";
	    }

	    Optional<WorkOrder> optionalWorkOrder = service.findByWoNumber(woNumber);
	    if (!optionalWorkOrder.isPresent()) {
	        redirectAttributes.addFlashAttribute("message", "Không tìm thấy Work Order với mã: " + woNumber);
	        return "redirect:/workorder";
	    }

	    WorkOrder workOrder = optionalWorkOrder.get();
	    Set<WorkOrderItem> workOrderItemLists = workOrder.getItems();
	    WorkOrderItem workOrderItem = new WorkOrderItem();

	    if (soNumber != null) {
	        Optional<WorkOrderItem> optionalItem = woItemservice.findBySoNumber(soNumber);
	        if (optionalItem.isPresent()) {
	            workOrderItem = optionalItem.get();
	        } else {
	            redirectAttributes.addFlashAttribute("message", "Không tìm thấy WorkOrderItem với SO: " + soNumber);
	            return "redirect:/workorder/salecheck?wo_id=" + woNumber;
	        }
	    }

	    List<WorkOrderItem> sortedItemLists = workOrderItemLists.stream()
	            .sorted(Comparator.comparing(WorkOrderItem::getSoNumber))
	            .collect(Collectors.toList());

	    model.addAttribute("workOrder", workOrder);
	    model.addAttribute("workOrderItem", workOrderItem);
	    model.addAttribute("workOrderItemLists", sortedItemLists);

	    return "salecheck";
	}


	/* ---------------------------------------------------- */	
	@PostMapping("/import")
	public String handleImport(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		String result = excelImporter.importExcel(file);
	    redirectAttributes.addFlashAttribute("message", result);
	    WorkOrder workOrder = new WorkOrder();
	    List<WorkOrder> woorderlist = service.findAll();
	    model.addAttribute("workOrder", workOrder);
	    model.addAttribute("woorderlist", woorderlist);
	   
	    return "redirect:/workorder"; 
	}
	/* ---------------------------------------------------- */
	
	@PostMapping("/save")
	public String handleEdit(@ModelAttribute("workOrder") WorkOrder workOrderForm,
	                         BindingResult result,
	                         RedirectAttributes redirectAttributes) {
	    if (result.hasErrors()) {
	    	redirectAttributes.addFlashAttribute("message", "Lưu không thành công!");
	    	return "redirect:/workorder";
	    }

	    WorkOrder workOrder = service.findByWoNumber(workOrderForm.getWoNumber()).get();
	    
	    workOrder.setIssueDate(workOrderForm.getIssueDate());
	    workOrder.setSetupDate(workOrderForm.getSetupDate());
	    workOrder.setOldFglNumber(workOrderForm.getOldFglNumber());
	    workOrder.setLineNo(workOrderForm.getLineNo());
	    workOrder.setNumberOfSpindle(workOrderForm.getNumberOfSpindle());
	    workOrder.setNumberOfOperator(workOrderForm.getNumberOfOperator());
	    workOrder.setProductionStartDate(workOrderForm.getProductionStartDate());
	    workOrder.setProductionEndDate(workOrderForm.getProductionEndDate());
	    workOrder.setProductionStartShift(workOrderForm.getProductionStartShift());
	    workOrder.setProductionEndShift(workOrderForm.getProductionEndShift());
	    workOrder.setNoCode(workOrderForm.getNoCode());
	    
	    service.save(workOrder);
	    redirectAttributes.addFlashAttribute("message", "Lưu thành công!");
	    return "redirect:/workorder";
	}

	/* ---------------------------------------------------- */
	@GetMapping("/del")
	public String deleteWorkOrder(@RequestParam("wo_id") String woNumber, RedirectAttributes redirectAttributes) {
	    Optional<WorkOrder> workOrderOpt = service.findByWoNumber(woNumber);
	    if (workOrderOpt.isPresent()) {
	        service.delete(workOrderOpt.get());
	        redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
	    } else {
	        redirectAttributes.addFlashAttribute("message", "Không tìm thấy Work Order!");
	    }
	    return "redirect:/workorder"; 
	}
	/* ---------------------------------------------------- */
}
