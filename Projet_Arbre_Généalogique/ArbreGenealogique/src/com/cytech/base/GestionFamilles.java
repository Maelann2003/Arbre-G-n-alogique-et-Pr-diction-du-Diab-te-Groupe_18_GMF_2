package com.cytech.base;
import com.opencsv.CSVReader;	
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import smile.clustering.KMeans;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GestionFamilles {

    private static final String CSV_FILE_PATH = "./familles.csv";
    private List<Personne> personnes;

    public GestionFamilles() throws CsvValidationException {
        this.personnes = chargerPersonnes();
    }
    
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd/M/yyyy")
        };

    public List<Personne> chargerPersonnes() throws CsvValidationException {
        List<Personne> personnes = new ArrayList<>();
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(CSV_FILE_PATH))
                    .withSkipLines(1)
                    .build();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                
                String id = nextLine[0];
                String nom = nextLine[1];
                String prenom = nextLine[2];
                String dateNaissance = nextLine[3];
                String nationalite = nextLine[4];
                String idPere = nextLine[5];
                String idMere = nextLine[6];
                boolean antecedentsDiabete = Boolean.parseBoolean(nextLine[7]);
                String typeDiabete = nextLine[8].isEmpty() ? null : nextLine[8];
                Integer ageDiagnostic = nextLine[9].isEmpty() ? 0 : Integer.parseInt(nextLine[9]);

                Personne p = new Personne(id, nom, prenom, dateNaissance, nationalite, idPere, idMere, typeDiabete, ageDiagnostic, antecedentsDiabete);
                personnes.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personnes;
    }


    public void sauvegarderPersonnes() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
        	
            writer.writeNext(new String[]{"id", "nom", "prenom", "dateNaissance", "nationalite", "idPere", "idMere", "antecedentsDiabete", "typeDiabete", "ageDiagnostic"});
            for (Personne p : this.personnes) {
                String[] entries = {
                        p.getId(),
                        p.getNom(),
                        p.getPrenom(),
                        p.getDateNaissance(),
                        p.getNationalite(),
                        p.getIdPere(),
                        p.getIdMere(),
                        String.valueOf(p.isAntecedentsDiabete()),
                        p.getTypeDiabete(),
                        p.getAgeDiagnostic() != 0 ? String.valueOf(p.getAgeDiagnostic()) : ""
                };
                writer.writeNext(entries);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifierCoherence() {
        List<String> ids = this.personnes.stream().map(Personne::getId).collect(Collectors.toList());
        boolean hasInconsistentId = this.personnes.stream().anyMatch(p -> !ids.contains(p.getIdPere()) || !ids.contains(p.getIdMere()));
        if (hasInconsistentId) {
            System.out.println("Incohérence détectée dans les liens parentaux.");
        } else {
            System.out.println("Toutes les entrées sont cohérentes.");
        }
    }
    public void verifierEtCorrigerCoherence() {
        Set<String> idsValides = new HashSet<>();
        this.personnes.forEach(p -> idsValides.add(p.getId()));

        this.personnes.forEach(p -> {
            if (!p.getIdPere().isEmpty() && (!idsValides.contains(p.getIdPere()) || p.getIdPere().equals(p.getId()))) {
                System.out.println("Incohérence trouvée: ID père invalide pour " + p.getNom() + " " + p.getPrenom());
                p.setIdPere(""); 
            }
            if (!p.getIdMere().isEmpty() && (!idsValides.contains(p.getIdMere()) || p.getIdMere().equals(p.getId()))) {
                System.out.println("Incohérence trouvée: ID mère invalide pour " + p.getNom() + " " + p.getPrenom());
                p.setIdMere("");
            }
        });
        sauvegarderPersonnes();
    }

   
    public void ajouterPersonne(Personne personne) {
        this.personnes.add(personne);
        sauvegarderPersonnes();
    }

    public void supprimerPersonne(String id) {
        this.personnes.removeIf(p -> p.getId().equals(id));
        sauvegarderPersonnes();
    }

    
    public void nettoyerDonnees() {
        Iterator<Personne> it = this.personnes.iterator();
        while (it.hasNext()) {
            Personne p = it.next();

            if (p.getNom().isEmpty() || p.getPrenom().isEmpty()) {
                System.out.println("Suppression de l'entrée pour cause de nom ou prénom vide : " + p);
                it.remove();
                continue;
            }
            LocalDate dateNaissance = parseDateWithMultipleFormats(p.getDateNaissance());
            if (dateNaissance == null) {
                System.out.println("Format de date de naissance invalide pour " + p + ". Correction nécessaire.");
                continue;
            }
            
            verifyParentDates(p, dateNaissance, it);
        }
        
        sauvegarderPersonnes();
    }



    private void verifyParentDates(Personne p, LocalDate childBirthDate, Iterator<Personne> it) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            if (!p.getIdPere().isEmpty()) {
                Personne pere = findPersonById(p.getIdPere());
                if (pere != null) {
                    LocalDate dateNaissancePere = parseDateWithMultipleFormats(pere.getDateNaissance());
                    if (dateNaissancePere != null && childBirthDate.isBefore(dateNaissancePere)) {
                        System.out.println("Incohérence détectée: " + p.getPrenom() + " est né(e) avant son père " + pere.getPrenom());
                    }
                }
            }

            if (!p.getIdMere().isEmpty()) {
                Personne mere = findPersonById(p.getIdMere());
                if (mere != null) {
                    LocalDate dateNaissanceMere = parseDateWithMultipleFormats(mere.getDateNaissance());
                    if (dateNaissanceMere != null && childBirthDate.isBefore(dateNaissanceMere)) {
                        System.out.println("Incohérence détectée: " + p.getPrenom() + " est né(e) avant sa mère " + mere.getPrenom());
                    }
                }
            }
        }
    }


    public Personne findPersonById(String id) {
        return this.personnes.stream()
                             .filter(person -> person.getId().equals(id))
                             .findFirst()
                             .orElse(null);
    }
    
    
    private LocalDate parseDateWithMultipleFormats(String dateStr) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        System.out.println("Format de date invalide : " + dateStr);
        return null;
    }
    
    
    public void afficherArbreGlobal() {
        this.personnes.forEach(System.out::println);
    }

    public void afficherArbreFamille(String nomFamille) {
        List<Personne> famille = this.personnes.stream()
                                               .filter(p -> p.getNom().equalsIgnoreCase(nomFamille))
                                               .collect(Collectors.toList());
        famille.forEach(System.out::println);
    }

    public void afficherDescendance(String id) {
        List<Personne> descendants = trouverDescendants(id);
        descendants.forEach(System.out::println);
    }

    private List<Personne> trouverDescendants(String id) {
        List<Personne> descendants = new ArrayList<>();
        for (Personne p : this.personnes) {
            if (p.getIdPere().equals(id) || p.getIdMere().equals(id)) {
                descendants.add(p);
                descendants.addAll(trouverDescendants(p.getId()));
            }
        }
        return descendants;
    }

    public void afficherAscendance(String id) {
        List<Personne> ascendants = trouverAscendants(id);
        ascendants.forEach(System.out::println);
    }

    private List<Personne> trouverAscendants(String id) {
        List<Personne> ascendants = new ArrayList<>();
        Personne personne = findPersonById(id);
        if (personne != null) {
            if (!personne.getIdPere().isEmpty()) {
                Personne pere = findPersonById(personne.getIdPere());
                if (pere != null) {
                    ascendants.add(pere);
                    ascendants.addAll(trouverAscendants(pere.getId()));
                }
            }
            if (!personne.getIdMere().isEmpty()) {
                Personne mere = findPersonById(personne.getIdMere());
                if (mere != null) {
                    ascendants.add(mere);
                    ascendants.addAll(trouverAscendants(mere.getId()));
                }
            }
        }
        return ascendants;
    }

    public void afficherProches(String id) {
        List<Personne> proches = trouverProches(id);
        proches.forEach(System.out::println);
    }

    private List<Personne> trouverProches(String id) {
        Personne personne = findPersonById(id);
        if (personne == null) {
            return Collections.emptyList();
        }
        return this.personnes.stream()
                             .filter(p -> (p.getIdPere().equals(personne.getIdPere()) || p.getIdMere().equals(personne.getIdMere())) && !p.getId().equals(id))
                             .collect(Collectors.toList());
    }

    public void afficherSansAscendant() {
        List<Personne> sansAscendants = this.personnes.stream()
                                                      .filter(p -> p.getIdPere().isEmpty() && p.getIdMere().isEmpty())
                                                      .collect(Collectors.toList());
        sansAscendants.forEach(System.out::println);
    }
    public void afficherArbreGenealogiqueGlobal() {
        List<Personne> racines = this.personnes.stream()
                                               .filter(p -> p.getIdPere().isEmpty() && p.getIdMere().isEmpty())
                                               .collect(Collectors.toList());

        for (Personne racine : racines) {
            afficherArbre(racine, 0);
        }
    }

    private void afficherArbre(Personne personne, int niveau) {
        String indentation = " ".repeat(niveau * 4);
        System.out.println(indentation + personne.getNom() + " " + personne.getPrenom() + " (" + personne.getId() + ")");

        List<Personne> enfants = this.personnes.stream()
                                               .filter(p -> p.getIdPere().equals(personne.getId()) || p.getIdMere().equals(personne.getId()))
                                               .collect(Collectors.toList());

        for (Personne enfant : enfants) {
            afficherArbre(enfant, niveau + 1);
        }
    }
    
    public void appliquerKMeans() {
        double[][] data = prepareDataForClustering(this.personnes);

        KMeans kmeans = KMeans.fit(data, 3);
        System.out.println("Clusters:");
        for (int i = 0; i < kmeans.centroids.length; i++) {
            System.out.println("Cluster " + i + ":");
            for (int j = 0; j < data.length; j++) {
                if (kmeans.y[j] == i) {
                    System.out.println(this.personnes.get(j));
                }
            }
        }
    }

    public double[][] prepareDataForClustering(List<Personne> personnes) {
        double[][] data = new double[personnes.size()][3];

        for (int i = 0; i < personnes.size(); i++) {
            Personne p = personnes.get(i);
            data[i][0] = p.isAntecedentsDiabete() ? 1 : 0;
            data[i][1] = p.getAgeDiagnostic() != 0 ? p.getAgeDiagnostic() : 0;
            data[i][2] = "Type 1".equals(p.getTypeDiabete()) ? 1 : "Type 2".equals(p.getTypeDiabete()) ? 2 : "Gestationnel".equals(p.getTypeDiabete()) ? 3 : 0;
        }

        return data;
    }
}
