package packagg;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FirstController {	
	
	@GetMapping("/hello")
	public String helloPage2(HttpServletRequest request) {
		String name = request.getParameter("name");
		System.out.println(name);
		
		return "first/hello";
	}
	
	@GetMapping("/hello2")
	public String helloPage2(@RequestParam(value="name", required=false) String name, Model model) {
		
//		System.out.println(name);
		
		model.addAttribute("message", name+"_gospodin");
		return "first/hello";
	}
	
	@GetMapping("/calculator")
	public String calculator(@RequestParam("a") int a, @RequestParam("b") int b, @RequestParam("action") String action, Model model) {
		
		double result;
		switch(action) {
			case "mul":
				result = a * b;
				break;
			case "div":
				result = a / b;
				break;
			case "add":
				result = a + b;
				break;
			case "sub":
				result = a - b;
				break;
			default:
				result = 0;
		}
		
		model.addAttribute("result", result);
		
		return "first/calculator";
	}
	
	
	@GetMapping("/goodbye")
	public String goodbyePage() {
		return "first/goodbye";
	}
}
