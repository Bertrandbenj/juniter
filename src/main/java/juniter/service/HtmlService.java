package juniter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HtmlService {

	private static List<Car> carList = new ArrayList<Car>();

	@RequestMapping(value = "/cars", method = RequestMethod.GET)
	public String init(@ModelAttribute("model") ModelMap model) {
		model.addAttribute("carList", carList);
		return "index";
	}

	public class Car {

		private String make;
		private String model;

		public Car() {

		}

		public Car(String make, String model) {
			this.make = make;
			this.model = model;
		}

		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

	}

}
