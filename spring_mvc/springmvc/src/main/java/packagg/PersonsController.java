package packagg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import packagg.dao.PersonDAO;
import packagg.models.Person;

@Controller
@RequestMapping("/people")
public class PersonsController {

	@Autowired
	private PersonDAO personDAO;
	
	@GetMapping()
	public String findAll(Model model) {
		model.addAttribute("allPersons", personDAO.findAll());
		return "people/index";
	}
	
	@GetMapping("/{id}")
	public String find(@PathVariable("id") int id, Model model) {
		model.addAttribute("person", personDAO.find(id));
		return "people/show";
	}
	
	@GetMapping("/new")
	public String newPerson(Model model) {
		model.addAttribute("person", new Person());
		return "people/new";
	}
	
	@PostMapping()
	public String create(@ModelAttribute("person") Person person) {
		personDAO.save(person);
		return "redirect:/people";
	}
}
