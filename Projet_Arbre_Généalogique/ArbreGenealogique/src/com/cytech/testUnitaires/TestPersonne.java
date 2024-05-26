package com.cytech.testUnitaires;

import com.cytech.base.Personne;

public class TestPersonne {

	public static void main(String[] args) {
		
		Personne p1 = new Personne();
		p1.setId("2");
		p1.setNom("PIERRAT");
		p1.setPrenom("Thomas");
		p1.setDateNaissance("29 mai 2003");
		p1.setNationalite("Française");
		p1.setIdPere("0");
		p1.setIdMere("1");
		p1.setTypeDiabete(null);
		p1.setAgeDiagnostic(0);
		p1.setAntecedentsDiabete(false);
		
		System.out.println("Informations de la personne:\n");
		System.out.println(p1);
		
		
	}

}
