package org.sid;

import org.sid.dao.ProduitRepository;
import org.sid.entities.Produit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CataSpringMvcHibernateApplication {

	public static void main(String[] args) {
	ApplicationContext ctx =	SpringApplication.run(CataSpringMvcHibernateApplication.class, args);
	ProduitRepository produitRepository=ctx.getBean(ProduitRepository.class);
	
	produitRepository.save(new Produit("PC ACER",3000,8));
	produitRepository.save(new Produit ("PC TOSHIBA",1500,11));
	produitRepository.save(new Produit("PC PROBOOK",9000,3));
	
	produitRepository.findAll().forEach(p->System.out.println(p.getDesignation()));
	}

}
