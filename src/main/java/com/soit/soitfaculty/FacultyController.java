package com.soit.soitfaculty;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.soit.soitfaculty.entity.Faculty;
import com.soit.soitfaculty.service.FacultyService;

@Controller
@RequestMapping("/posts")
public class FacultyController {

	public FacultyService facultyService;

	public FacultyController(FacultyService theFacultyService) {
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

		// retrieve the faculty from the service layer
		Faculty theFaculty = facultyService.findById(theId);

		// pre-populate the form by setting the faculty as a model attribute
		theModel.addAttribute("faculty", theFaculty);

		// redirect us to the faculty form
		return "faculties/faculty-form";

	}

	@PostMapping("/save")
	public String save(@ModelAttribute("faculty") Faculty theFaculty,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		theFaculty.setPhotos(fileName);
		// register the faculty
		facultyService.save(theFaculty);
		String uploadDir = "faculty-photos/" + theFaculty.getId();

		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		// block duplicate submission for accidental page refreshed
		return "redirect:/posts/list";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("facultyId") int theId) {

		// remove faculty
		facultyService.deleteById(theId);

		// return to the faculty's directory
		return "redirect:/posts/list";
	}
	
}
