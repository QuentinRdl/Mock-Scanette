package fr.l3info.co;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe décrivant la scanette de supermarché
 */
public class Scanette {

    /** Etats possibles de la scanette */
    enum ETAT { BLOQUEE, EN_COURSES, RELECTURE, RELECTURE_OK, RELECTURE_KO };

    /** Etat courant de la scanette */
    private ETAT etat;

    /** Base de données des produits */
    private ArticleDB produits;


    /** Données utiles pendant les courses */

    /** Panier de l'utilisateur */
    private HashMap<Long, Integer> panier;
    /** Articles non trouvés */
    private ArrayList<Long> nonReconnus;


    /** Données utiles pour la relecture */

    /** Nombre maximum de produits à rescanner */
    final private int A_RESCANNER = 12;
    /** Nombre actuel de produits à rescanner */
    private int aRescanner = 0;
    /** Verification en cours (sorte de second achats) */
    private HashMap<Long, Integer> verif;


    /**
     * Créé une scanette en l'initialisant avec la base de données d'articles dont
     * le chemin est donné en paramètre.
     * @param db la base de données d'articles
     */
    public Scanette(ArticleDB db) {
        produits = db;
        etat = ETAT.BLOQUEE;
        panier = new HashMap<Long, Integer>();
        nonReconnus = new ArrayList<Long>();
        verif = new HashMap<Long, Integer>();
    }

    /**
     * Permet de débloquer la scanette pour un client donné. On supposera que
     * l'on ne gère pas les identifiants des clients.
     * @return  0 si la scanette a bien été débloquée,
     *          -1 si celle-ci n'était pas bloquée.
     */
    public int debloquer() {
        if (etat == ETAT.BLOQUEE) {
            etat = ETAT.EN_COURSES;
            return 0;
        }
        return -1;  // wrong state
    }

    /**
     * Scanne un produit par l'intermédiaire de son code EAN13.
     * Cette méthode sert à la fois pour ajouter un produit au
     * achats du client et pour effectuer une relecture.
     * @param ean13 le code EAN13 du produit scanné
     * @return  0 si le scan du produit s'est correctement déroulé.
     *          -1 si la scanette n'était pas dans le bon état
     *          -2 si le produit scanné n'était pas reconnu (en courses)
     *          -3 si le produit n'était pas dans le achats (en relecture)
     */
    public int scanner(long ean13) {
        if (etat == ETAT.EN_COURSES) {
            Article a = produits.getArticle(ean13);
            if (a == null) {
                nonReconnus.add(ean13);
                return -2;
            }
            int qu = quantite(ean13);
            qu++;
            panier.put(ean13, qu);
            return 0;
        }
        if (etat == ETAT.RELECTURE) {
            // ajout aux articles relus
            int qt = (verif.containsKey(ean13)) ? verif.get(ean13) : 0;
            verif.put(ean13, qt + 1);
            if (!panier.containsKey(ean13) || verif.get(ean13) > panier.get(ean13)) {
                etat = ETAT.RELECTURE_KO;
                return -3;
            } else {
                aRescanner--;
                if (aRescanner == 0) {
                    etat = ETAT.RELECTURE_OK;
                }
                return 0;
            }
        }
        return -1;
    }

    /**
     * Supprime du achats le produit dont le code EAN13 est donné en paramètre.
     * @param ean13 le code EAN du produit à supprimer.
     * @return  0 si l'occurrence du produit a bien été supprimée,
     *          -1 si la scanette n'était pas dans le bon état,
     *          -2 si le produit n'existait pas dans le achats
     */
    public int supprimer(long ean13) {
        if (etat != ETAT.EN_COURSES) {
            return -1;
        }
        int qu = quantite(ean13);
        if (qu < 1) {
            return -2;
        }
        if (qu == 1) {
            panier.remove(ean13);
            return 0;
        }
        panier.put(ean13, qu - 1);
        return 0;
    }

    /**
     * Permet de connaître la quantité d'un produit dans le achats.
     * @param ean13 le code EAN13 du produit dont on souhaite connaître la quantité.
     * @return le nombre d'occurrences du produit dans le achats.
     */
    public int quantite(long ean13) {
        return panier.containsKey(ean13) ? panier.get(ean13) : 0;
    }


    /**
     * Permet d'abandonner toute transaction en cours et de re-bloquer la scanette.
     */
    public void abandon() {
        etat = ETAT.BLOQUEE;
        panier.clear();
        nonReconnus.clear();
    }


    /**
     * Permet de consulter les codes EAN qui n'ont pas été reconnus lors des courses.
     * @return Un ensemble de codes EAN13 non reconnus.
     */
    public Set<Long> getReferencesInconnues() {
        return new HashSet<Long>(nonReconnus);
    }


    /**
     * Permet d'extraire les articles composant le achats du client.
     * @return Un ensemble d'fr.l3info.co.Article reconnus par la scanette.
     */
    public Set<Article> getArticles() {
        HashSet<Article> ret = new HashSet<Article>();
        for (long l : panier.keySet()) {
            ret.add(produits.getArticle(l));
        }
        return ret;
    }


    private int getNbArticles() {
        int nb = 0;
        for (long l : panier.keySet()) {
            nb += panier.get(l);
        }
        return nb;
    }

    /**
     * Permet de transmettre les informations de la scanette à la caisse en se connectant
     *  à celle-ci. En fonction de la réponse de la caisse, la scanette changera d'état.
     * @param c La caisse avec laquelle la scanette interagit.
     * @return  0 si la scanette a terminé son travail (pas de relecture)
     *          1 si une relecture est demandée par la caisse
     *          -1 en cas d'erreur
     */
    public int transmission(ICaisse c) {

        if (c == null) {
            return -1;
        }

        if (etat != ETAT.RELECTURE_OK && etat != ETAT.EN_COURSES) {
            System.out.println(ETAT.RELECTURE_OK + "\n" + ETAT.EN_COURSES);
            return -1;
        }

        int codeRetourCaisse = c.connexion(this);

        if (codeRetourCaisse == 0) {
            etat = ETAT.BLOQUEE;
            panier.clear();
            nonReconnus.clear();
            return 0;
        }
        else if (etat == ETAT.EN_COURSES && codeRetourCaisse == 1) {
            etat = ETAT.RELECTURE;
            verif.clear();
            int nb = getNbArticles();
            aRescanner = (nb > A_RESCANNER) ? A_RESCANNER : nb;
            if (aRescanner == 0) {
                etat = ETAT.RELECTURE_OK;
            }
            return 1;
        }

        return -1;
    }

    /**
     * Indique si la scanette vient de finir une relecture avec succès. 
     * @return true si la scanette est dans l'état RELECTURE_OK, false sinon.
     */
    public boolean relectureEffectuee() {
        return etat == ETAT.RELECTURE_OK;
    }
}