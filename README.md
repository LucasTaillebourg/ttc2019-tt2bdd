 # Installation du projet
 Le projet a été configuré pour les environnements Unix.
 
 * **Prérequis**: 
    * Python 3.3 ou plus
    * R
 * **Lancement**: À partir du dossier ttc2019-tt2bdd : `./scripts/run.py ` 
 
 Par défaut ce script sera lancé sur les solutions FirstNaiveDraft ( La nôtre), Fulib ( la meilleur), ATLEMFTVMImproved (une bonne référence).
 Ils seront lancés sur 4 modèles exemples.
 
 Pour modifier les modèles utilisés: `config/config.json` 
 
* **Résultat**: Les diagrammes de comparaisons sont produits dans `diagrams`
 
**/!\**

* En cas d'erreurs "xx must be a finite number" supprimer le fichier `output/output.csv` 
 
* En cas d'erreur `Command './gradlew installDist' returned non-zero exit status 1.` faire `gradle wrapper`

 

  
# First Naive draft
FirstNaiveDraft est un algorithme qui prend en entrée une Table de véritée et en sort un Arbre  binaire de décision.

Il existe également le début d'un algorithme d'optimisation de graphe qui n'est plus utilisé par la solution car il détruit la validitée de l'arbre.

Dans l'idée nous souhaitions réaliser plusieurs algorithmes pour comparez leur performances mais nous avons perdu du temps lors de l'instalation de notre projet dans le système fourni par le concours (run.py)
Evaluation de la solution:

(+) Facile à comprendre

(+) Produit un BDD valide pour tout les modèles proposés (validator.jar)

( - ) Performance globale mauvaise (notement sur le calcul des feuilles).





# Problèmes rencontrés
* Intégration dans le contexte du projet
* Nous avons interprété le modèle de 2 manière différente. La première version de notre tranformateur donnait alors un port de sortie par feuille au lieu de créer les ports de sorties  aux bon nombre et des les utiliser ensuite dans le feuilles. Nous nous sommes rendu compte de cette erreur lors du travail sur l'algorithme d'optimisation.
* Le language java ne se prête pas vraiment a du fonctionnel. L'absence d'opérateur de nullitée était notamment a déplorer.


# Retour sur le projet
* Nous avons choisis d'utilisez le language Java car c'est le seul que nous maitrisions. Cependant, nous avons entre temps eut un module ou nous utilisions Kotlin et avec du recul ce dernier aurait été beaucoup plus élégant pour faire un algorithme fonctionnel.
* Nous aurions dut travailler sur l'optimisation de la création de l'arbre de décision  au lieu de son optimisation car c'est la vitesse de trasphormation qui était jugée et non la beautée ce ce qui en sortait.

  
