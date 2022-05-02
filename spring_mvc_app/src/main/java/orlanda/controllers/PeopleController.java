package orlanda.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import orlanda.dao.person.PersonDaoInterface;
import orlanda.models.Person;

import javax.validation.Valid;
import java.io.File;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    @Qualifier("personDaoJdbcTemplate")
    PersonDaoInterface personDAO;

    @GetMapping()
    public String index(Model model) {
        System.out.print(new File("").getAbsolutePath());
        model.addAttribute("people", personDAO.getAll());

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {

        model.addAttribute("person", personDAO.getById(id));

        return "people/person";
    }

    @GetMapping("/new")
    public String newPerson(Model model) {
        model.addAttribute("person", new Person());
        return "people/new_person";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new_person";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("person", personDAO.getById(id));
        return "people/edit_person";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/"+id+"/edit_person";

        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

}
