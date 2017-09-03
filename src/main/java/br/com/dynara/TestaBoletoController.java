package br.com.dynara;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.dynara.modelo.Titulo;

@Controller
public class TestaBoletoController {
	
	Titulo titulo;
	
	@RequestMapping("/")
    public String index(Model model){
		
		if (titulo==null) {
			model.addAttribute("titulo", new Titulo(""));
		} else {
			model.addAttribute("titulo", titulo);
		}
		
		return "index";
    }
	
	@RequestMapping(value= "validar", method = RequestMethod.POST)
	public String salvar(Model model, @RequestParam("linha") String linha) {
		titulo = new Titulo(linha);
		model.addAttribute("titulo", titulo);
	    return "index";
	}
	
}
