package com.soit.soitfaculty;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soit.soitfaculty.entity.Faculty;
import com.soit.soitfaculty.service.FacultyService;

@Controller
@RequestMapping("/Faculties")
public class FacultyController {
	
	public FacultyService facultyService;
	public FacultyController (FacultyService theFacultyService) {
		facultyService = theFacultyService;
	}
	
	// mapping for "/list"
	@GetMapping("/list")
	public String listFaculties(Model theModel) {
		
		// retrieve faculties from the database
		List<Faculty> theFaculties = facultyService.findAll();
		
		// add faculties to the spring model
		theModel.addAttribute("faculties", theFaculties);
		
		return "faculties/list-faculties";
	}
	
	@GetMapping("/viewAddForm")
	public String viewAddForm(Model theModel) {
		
		// model attribute for data binding
		Faculty theFaculty = new Faculty();
		
		theModel.addAttribute("faculty", theFaculty);
		
		return "faculties/faculty-form";
	}
	
	@GetMapping("/viewUpdateForm")
	public String viewUpdateForm(@RequestParam("facultyId") int theId, Model theModel) {
		
		//retrieve the faculty from the service layer
		Faculty theFaculty = facultyService.findById(theId);
		
		//pre-populate the form by setting the faculty as a model attribute
		theModel.addAttribute("faculty", theFaculty);
		
		//redirect us to the faculty form
		return "faculties/faculty-form";
		
	}
	
	@PostMapping("/save")
	public String saveFaculty(@ModelAttribute("faculty") Faculty theFaculty) {
		
		// register the faculty
		facultyService.save(theFaculty);
	
		// block duplicate submission for accidental page refreshed
		return "redirect:/Faculties/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("facultyId")int theId) {
		
		// remove faculty
		facultyService.deleteById(theId);
		
		// return to the faculty's directory
		return "redirect:/Faculties/list";
	}
	

}
