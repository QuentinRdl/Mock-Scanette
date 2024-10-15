package fr.l3info.co;

import java.util.HashMap;


public class MaCaisse implements ICaisse {

    enum ETAT_CAISSE {
        EN_ATTENTE,
        ATTENTE_CAISSIER,
        PAIEMENT,
        AUTHENTIFIE
    }

    private ETAT_CAISSE etat;
    private ArticleDB produits;
    private HashMap<Article, Integer> achats;
    private double aPayer = 0;

    public double THRESHOLD = 0.1;

    public MaCaisse(ArticleDB db) {
        produits = db;
        achats = new HashMap<Article, Integer>();
        etat = ETAT_CAISSE.EN_ATTENTE;
    }

    /**
     * Admet une connexion de la scanette lorsqu'elle est sollicité alors qu'elle est en attente.
     * @param s la scanette qui se connecte à la caisse.
     * @return  0 si la caisse était en attente et qu'elle ne demande pas de relecture
     *          1 si la caisse demande une relecture
     *         -1 pour tous les autres cas
     */
    public int connexion(Scanette s) {
        if (s == null) {
            return -1;
        }

        if (this.etat != ETAT_CAISSE.EN_ATTENTE) {
            return -1;
        }

        if (!s.relectureEffectuee() & s.getArticles().size() > 0 & demandeRelecture()) {
            /**
            System.out.println("\n=====================\n");
            System.out.println("demande is : " + demandeRelecture() +
                    "\nand relecture is : " + s.relectureEffectuee() +
                    "\nand size is : " + s.getArticles().size());
            System.out.println("\n=====================\n");
             */
            return 1;
        }

        achats.clear();
        for (Article a : s.getArticles()) {
            achats.put(a, s.quantite(a.getEAN13()));
        }

        if (s.getReferencesInconnues().size() > 0 || s.getArticles().size() == 0) {
            this.etat = ETAT_CAISSE.ATTENTE_CAISSIER;
        }
        else {
            this.etat = ETAT_CAISSE.PAIEMENT;
        }

        return 0;
    }


    /**
     * Fonction utilitaire, permet de savoir si une relecture est demandée.
     * @return true si une relecture doit être demandée, faux sinon.
     */
    public boolean demandeRelecture() {
        return Math.random() < THRESHOLD;
    }


    /**
     * Permet de réaliser un paiement sur la caisse.
     * @param somme la somme qui est payée.
     * @return une valeur >= 0 si le paiement a pu être effectué et qu'il reste un éventuel rendu.
     *         une valeur < 0 pour indiquer une erreur (cf. sujet)
     */
    public double payer(double somme) {
        if (etat != ETAT_CAISSE.PAIEMENT) {
            return -42;
        }
        aPayer = 0;
        for (Article a : achats.keySet()) {
            aPayer += a.getPrixUnitaire() * achats.get(a);
        }
        if (aPayer - somme < 0.01) {
            achats.clear();
            etat = ETAT_CAISSE.EN_ATTENTE;
        }
        return somme - aPayer;
    }


    /**
     * Abandonne toute transaction en cours et replace la caisse en attente.
     */
    public void abandon() {
        achats.clear();
        etat = ETAT_CAISSE.EN_ATTENTE;
    }


    /**
     * Permet à un caissier de s'authentifier pour ouvrir une session.
     * @return 0 si la session a pu s'ouvrir
     *        -1 si l'appel n'a pas été réalisé depuis le(s) bon(s) état(s).
     */
    public int ouvrirSession() {
        // System.out.println("\nState = " + etat + "\n");
        if (etat == ETAT_CAISSE.PAIEMENT || etat == ETAT_CAISSE.ATTENTE_CAISSIER) {
            etat = ETAT_CAISSE.AUTHENTIFIE;
            return 0;
        }
        return -1;
    }


    /**
     * Permet de fermer une session préalablement ouverte.
     * @return  0 l'appel a réussi et la session est fermée
     *         -1 si l'appel a été effectué alors que le caissier n'était pas authentifié
     */
    public int fermerSession() {
        if (etat == ETAT_CAISSE.AUTHENTIFIE) {
            etat = achats.isEmpty() ?
                    ETAT_CAISSE.EN_ATTENTE :
                    ETAT_CAISSE.PAIEMENT;
            return 0;
        }
        return -1;
    }

    public int scanner(long ean13) {
        if (etat != ETAT_CAISSE.AUTHENTIFIE) {
            return -1;
        }
        Article a = produits.getArticle(ean13);
        if (a == null) {
            return -2;
        }
        if (achats.containsKey(a)) {
            achats.put(a, achats.get(a) + 1);
        }
        else {
            achats.put(a, 1);
        }
        return 0;
    }

    public int supprimer(long ean13) {
        if (etat != ETAT_CAISSE.AUTHENTIFIE) {
            return -1;
        }
        for (Article a : achats.keySet()) {
            if (a.getEAN13() == ean13) {
                int nb = achats.get(a);
                if (nb > 1) {
                    achats.put(a, nb-1);
                }
                else {
                    achats.remove(a);
                }
                return 0;
            }
        }
        return -2;
    }
}

