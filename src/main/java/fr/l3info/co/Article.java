package fr.l3info.co;

import java.util.Objects;

/**
 * Classe décrivant les articles
 */
public class Article {
    private long ean13;
    private double prix;
    private String nom;
    public boolean debug = false;

    public Article(long ean13, double prix, String nom) {
        this.ean13 = ean13;
        this.prix = prix;
        this.nom = nom;

    }

    public String getNom() {
        return nom;
    }

    public double getPrixUnitaire() {
        return prix;
    }

    public long getEAN13() {
        return ean13;
    }

    public boolean isValidEAN13() {
        boolean isValid = false;
        // We find the EAN13 key

        // We cast the EAN13 value in a String to handle it better
        StringBuilder ean = new StringBuilder(Long.toString(ean13));

        // TODO : Case ean13 begins with 0s.
        // If the ean number starts with one or multiple 0,
        // Than it is not detected because we are using longs.
        while(ean.length() < 13) {
            String zero = "0";
            ean.insert(0, zero);
        }

        if(ean.length() != 13) {
            return false;
        }

        // We find the raw key value

        int keyValue = 0;

        for(int i = 0; i < 12; i++) {
            int digit = (int) ean.charAt(i) - '0';
            if(i % 2 == 0) {
               // Even number
                keyValue += digit;

            } else {
                // Odd number
                keyValue += digit * 3;
            }
        }

        // We calculate the key value
        keyValue = keyValue % 10;

        if(keyValue != 0) keyValue = 10 - keyValue;


        // We calculated the key of the ean13 value, we check if it is valid
        int actualKey = (int)ean.charAt(12) - '0';

        if(actualKey == keyValue) {
            return true;
        }

        return false;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null) return false;

        if (getClass() != o.getClass()) return false;

        Article arO = (Article) (o);
        return this.ean13 == arO.getEAN13();

        /*
        if(o == this) return true;
        if(o == null) return false;
        if(o instanceof Article) {
            Article isEqual = (Article) o;
            if(this.getEAN13() != isEqual.getEAN13()) return false;
            if(!Objects.equals(this.getNom(), isEqual.getNom())) return false;
            return this.getPrixUnitaire() == isEqual.getPrixUnitaire();
        }
        return false;*/
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ean13);
    }

    public String prettyEan13() {
        StringBuilder str_vl = new StringBuilder(String.valueOf(this.ean13));
        while(str_vl.length() < 13) str_vl.insert(0, "0");
        return str_vl.toString();
    }
}
