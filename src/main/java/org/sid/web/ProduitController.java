package org.sid.web;

import javax.validation.Valid;

import org.sid.dao.ProduitRepository;
import org.sid.entities.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RestController
public class ProduitController {
	
	@Autowired
	private ProduitRepository produitRepository;
	
	@RequestMapping(value="/user/index")
	public String index(Model model,@RequestParam(name="motCle",defaultValue ="")String mc, @RequestParam(name="page",defaultValue = "0") int p , @RequestParam(name="size",defaultValue = "3") int s) {
		Page<Produit> pageProduits = 
				produitRepository.chercher("%"+mc+"%",PageRequest.of(p, s)).getSize() == 0 && p > 0 ? produitRepository.chercher("%"+mc+"%",PageRequest.of(p-1, s)) : produitRepository.chercher("%"+mc+"%",PageRequest.of(p, s));
		
		model.addAttribute("listProduits",pageProduits.getContent());
		int[] pages = new int[pageProduits.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("motCle",mc);
		model.addAttribute("size", s);
		model.addAttribute("pageCourante", p);
		return "produits";
	} 
	
	@RequestMapping(value="/admin/delete",method=RequestMethod.GET)
	public ModelAndView delete(ModelMap model,Long id,String motCle,int page,int size) {
		
		produitRepository.deleteById(id);
		
		return new ModelAndView("redirect:/user/index?page="+page+"&size="+size+"&motCle="+motCle, model);
	}
	
	
	@RequestMapping(value="/admin/form",method = RequestMethod.GET)
	public String formProduit(Model model) {
		model.addAttribute("produit", new Produit());
		return "FormProduit";
	}
	
	@RequestMapping(value="/admin/edit",method = RequestMethod.GET)
	public String edit(Model model, Long id) {
		Produit p = produitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("produit", p);
		return "EditProduit";
	}
	
	@RequestMapping(value="/admin/save",method = RequestMethod.POST)
	public String formProduit(Model model,@Valid Produit produit,BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "FormProduit";
		produitRepository.save(produit);
		return "Confirmation";
	}
	
	@RequestMapping(value = "/")
	public String home() {
		return "redirect:/user/index";
	}
	
	@RequestMapping(value = "/403")
	public String accessDenied() {
		return "403";
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	
	/*
	@GetMapping("/all")
	public List<Produit> products() {
		return produitRepository.findAll();
	}*/
}
