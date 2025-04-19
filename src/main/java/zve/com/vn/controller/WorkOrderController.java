package zve.com.vn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import zve.com.vn.entity.WorkOrder;
import zve.com.vn.service.ExcelWorkOrderImporter;
import zve.com.vn.service.WorkOrderServiceImpl;



@Controller
@RequestMapping("/workorder")
public class WorkOrderController {

	@Autowired
	private ExcelWorkOrderImporter excelImporter;
	
	@Autowired
	private WorkOrderServiceImpl service;
	/* ---------------------------------------------------- */
	@GetMapping({"","/edit", "/import"})
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
	
	@PostMapping("/import")
	public String handleImport(@RequestParam("file") MultipartFile file, Model model) {
	    String result = excelImporter.importExcel(file);
	    model.addAttribute("message", result);
	    return "workorder";
	}
	/* ---------------------------------------------------- */
}
