---
title: Analyse des besoins - Cas d'utilisation
---

# Cas d'utilisation

## Vue d’ensemble

Les cas d’utilisation décrivent la manière dont les **acteurs interagissent avec le système** pour atteindre leurs objectifs.  
Dans le contexte de la plateforme **ChoixCours UdeM**, ils représentent les principales fonctionnalités offertes aux étudiants pour les aider à **planifier efficacement leur parcours académique**.  

Chaque cas d’utilisation correspond à une interaction concrète et observable entre un utilisateur (étudiant) et le système, comme par exemple la recherche de cours, la création d’une simulation d’horaire, la gestion de favoris ou encore la vérification du cheminement de diplomation.  
Ces cas permettent de comprendre **ce que fait le système**, sans entrer dans les détails techniques de son implémentation.  

---

## Liste des cas d’utilisation

| ID  | Nom                                             | Acteurs principaux | Description                                                                                                                                            |
|-----|-------------------------------------------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| CU01 | Exporter des données                            | Étudiant (principal) | L’étudiant sélectionne des cours et génère un document (PDF, CSV, ou autre) regroupant leurs informations essentielles.                                |
| CU02 | Modérer les avis étudiants                      | Modérateur         | Le modérateur valide ou rejette les avis soumis avant publication.                                                                                     |
| CU03 | Consulter un profil                             | Étudiant (principal) | Un étudiant peut consulter le profil public d’un autre contributeur                                                                                    |
| CU04 | Gérer ses favoris                               | Utilisateur        | L’utilisateur ajoute ou retire des cours à sa liste de favoris pour les retrouver plus facilement.                                                     |
| CU05 | Afficher tableau de bord                        | Étudiant (principal) | L’étudiant voit rapidement l’état de son parcours et ses cours favoris pour mieux planifier son prochain semestre.                                     |
| CU06 | Comparer cours                                  | Étudiant (principal) | L’étudiant peut comparer plusieurs options côte à côte pour choisir le cours qui correspond le mieux à ses objectifs et à sa disponibilité.            |
| CU07 | Créer/Mettre à jour un profil                   | Étudiant           | L'étudiant crée ou met à jour son profil personnel.                                                                                                    |
| CU08 | Afficher les avis étudiants                     | Étudiant           | L'étudiant consulte les avis agrégés et commentaires sur un cours.                                                                                     |
| CU09 | Rechercher cours                                | Étudiant (principal) | L’étudiant peut rapidement identifier les cours pertinents à son profil et savoir s’il peut s’y inscrire.                                              |
| CU10 | Afficher résultats académiques d'un cours       | Étudiant (principal) | L’étudiant obtient des repères sur la difficulté et la réussite des cours pour décider lesquels s’intègrent le mieux à son parcours.                   |
| CU11 | Consulter la fiche d’un cours                   | Étudiant (principal) | L'étudiant consulte les données essentielles liées à un cours donné.                                                                                   |
| CU12 | Créer une simulation d’horaire                  | Utilisateur        | L’utilisateur crée une simulation d’horaire à partir de cours choisis afin de visualiser la charge de travail et la compatibilité des plages horaires. |
| CU13 | Consulter la place d'un cours dans le programme | Utilisateur        | L’utilisateur consulte la contribution d’un cours à la diplomation selon son programme (catégories de crédits, exigences restantes, etc.).             |


## Détail


### CU01 - Exporter des données

**Acteurs** : Étudiant (principal).  
**Préconditions** : L'étudiant a un compte et y est connecté. L'étudiant a sélectionné des cours.  
**Postconditions** : L'étudiant a téléchargé un document contenant les données désirées.  
**Déclencheur** : L'étudiant clique sur le bouton "Exporter" pendant que les cours désirés sont sélectionnés.  
**Dépendances** : CU5 : le tableau de bord peut servir de point de départ pour la sélection des cours, CU11 : fiches de cours pour récupérer les informations détaillées, CU10 : résultats académiques des cours à comparer, CU8 : avis étudiants.  
**But** : Consulter les données principales des cours pertinents pour l'étudiant hors-ligne.  

---
### CU02 - Modérer les avis étudiants

**Acteurs** : Modérateur (Administrateur) (principal), Système (secondaire)  
**Préconditions** : Le modérateur est authentifié et connecté. Le modérateur dispose des permissions d'administration. Il existe une file d'attente d'avis en attente de modération.  
**Postconditions** : L'avis est soit approuvé (et devient visible), soit rejeté (et est supprimé ou archivé). La base de données est mise à jour.  
**Déclencheur** : Un nouvel avis est soumis via le bot Discord OU un utilisateur signale un avis déjà publié.  
**Dépendances** : Flux de données depuis le bot Discord. Le CU "Afficher les avis étudiants" (CU08) dépend de ce CU.  
**But** : S'assurer que les avis soumis respectent les règles de la plateforme. 

**Scénario principal:**  
1. Le système notifie le modérateur qu'un ou plusieurs avis sont en attente de modération (via un tableau de bord admin).  
2. Le modérateur accède à l'interface de modération.  
3. Le système présente une liste d'avis en attente, chacun affichant le contenu du commentaire, la difficulté perçue, la charge de travail estimée, le cours concerné et l'identifiant anonyme de l'étudiant.  
4. Le modérateur sélectionne un avis dans la liste pour l'examiner en détail.  
5. Le modérateur évalue l'avis par rapport à une liste de critères (pertinence, langage, spam, etc.).  
6. Le modérateur prend une décision :  
    * **6a. Approuver l'avis :**  
      6a.1. Le modérateur clique sur le bouton "Approuver".  
      6a.2. Le système enregistre l'avis comme "approuvé" dans la base de données.  
      6a.3. L'avis devient visible dans la fiche du cours concerné (CU 8).  
      6a.4. Le système retire l'avis de la file d'attente de modération.  
    * **6b. Rejeter l'avis :**  
      6b.1. Le modérateur clique sur le bouton "Rejeter".  
      6b.2. Le système demande une raison pour le rejet (optionnel, mais recommandé).  
      6b.3. Le modérateur sélectionne ou saisit une raison (ex: "langage inapproprié").  
      6b.4. Le système enregistre l'avis comme "rejeté" et l'archive avec la raison.  
      6b.5. Le système retire l'avis de la file d'attente de modération.  

**Scénario alternatif:**  
 5a. Le modérateur décide de mettre en pause et de revenir plus tard.  
 5a.1. L'avis reste dans la file d'attente "en cours de modération".  
 5a.2. Le modérateur peut reprendre la modération plus tard.  

 6a.1. Erreur lors de l'approbation.  
 6a.1.1. Le système ne peut pas mettre à jour la base de données.  
 6a.1.2. Un message d'erreur est affiché.  
 6a.1.3. L'avis reste dans son état précédent.  

---
### CU03 - Consulter un profil étudiant

**Acteurs** : Étudiant (Principal)  
**Préconditions** : L'étudiant qui fait la recherche et l'étudiant recherché ont leurs profils respectifs.  
**Postconditions** : Le profil de l'étudiant recherché apparaît à l'étudiant qui fait la recherche.    
**Déclencheur** : L'étudiant qui fait la recherche clique sur le profil de l'étudiant recherché dans une liste de profils dans le module de recherche de profils.  
**Dépendances** : CU7: les deux étudiants doivent avoir créé des profils.   
**But** : S'inspirer des choix d'autres étudiants afin d'éclairer ses propres décisions. Contextualiser les avis d'un étudiant donné.  

---
### CU04 - Gérer ses favoris

**Acteurs** : Utilisateur (principal)  
**Préconditions** : L’utilisateur est connecté à son compte et dispose d’une liste de cours consultables.  
**Postconditions** : Le cours est ajouté ou retiré de la liste des favoris persistée dans le profil utilisateur.  
**Déclencheur** : L’utilisateur clique sur l’icône « Favori » ou accède à sa liste de favoris.  
**Dépendances** : Système de profil utilisateur et base de données des cours.  
**But** : Permettre à l’utilisateur de conserver une liste personnalisée de cours qu’il souhaite suivre, comparer ou évaluer plus tard.  

**Scénario principal:**  
1) L’étudiant consulte la fiche d’un cours.  
2) Le système affiche les détails du cours.  
3) L’étudiant clique sur “Ajouter aux favoris”.  
4) Le système enregistre le cours dans la liste de favoris de l’étudiant.  
5) L’étudiant peut accéder à l’onglet “Favoris” pour consulter tous ses cours enregistrés.  
6) L’étudiant peut retirer un cours de la liste.  
7) Le système met à jour la liste des favoris.  

**Scénario alternatif:**  
3a. Le cours est déjà dans les favoris.   
  3a.1. Le système affiche un message “Ce cours est déjà dans vos favoris.”  
  3a.2. L’étudiant peut choisir de le retirer immédiatement.  
  3a.3. Le système met à jour la liste et affiche la confirmation.  

4a. Une erreur survient lors de l’enregistrement.   
  4a.1. Le système ne parvient pas à écrire dans la base de données.  
  4a.2. Le système affiche un message “Erreur lors de l’ajout du cours. Veuillez réessayer.”  

5a. L’étudiant n’a aucun favori enregistré.  
  5a.1. Le système affiche un message “Aucun cours favori pour le moment.”  
  5a.2. Il propose d’explorer les cours populaires.  

---
### CU05 - Affficher tableau de bord
**Acteurs** : Étudiant (principal)    
**Préconditions** : L’étudiant est connecté et a un profil existant.  
**Postconditions** : Le tableau de bord est affiché avec les informations disponibles et interactives. 
**Déclencheur** : L’étudiant clique sur le bouton ou le menu « Tableau de bord » après s’être connecté.    
**Dépendances** :  CU1 / CU7 : Profil étudiant et favoris stockés dans la base de données,API Planifium : récupération des données officielles des cours ,CU9 / CU11 : consultation et recherche de cours (si le tableau de bord affiche des liens ou résumés de cours)  
**But** : Permettre à l’étudiant de visualiser un résumé personnalisé de ses cours, favoris, et informations clés pour faciliter la prise de décision.  

**Scénario principal:**  
1) L'étudiant accède à la page d'accueil après sa connexion.  
2) Le système vérifie les informations de connexion et le type de profil.  
3) Le système récupère les informations du profil depuis la base de données.    
4) Le système interroge l’API Planifium pour mettre à jour les données des cours suivis et favoris.    
5) Le système récupère les statistiques académiques et avis liés aux cours de l’étudiant.  
6) Le système compile toutes les données (favoris, suggestions, statistiques).  
7) Le système affiche le tableau de bord personnalisé à l’écran.  
8) L’étudiant consulte et interagit avec les éléments (ex. accéder à la fiche d’un cours, modifier un favori).  

**Scénario alternatif**  
3a. Le profil de l’étudiant n’existe pas dans la base de données.  
 3a.1. Le système affiche un message invitant à créer un profil.  
 3a.2. L’étudiant choisit de créer son profil.  
 3a.3. Le système redirige vers le CU7 (Créer profil).  
 3a.4. Une fois le profil créé, le scénario principal reprend à l’étape 4.  

7a. Une erreur interne empêche l’affichage du tableau.  
 7a.1. Le système affiche un message d’erreur générique.  
 7a.2. Le système envoie un rapport automatique à l’équipe technique.  
 7a.3. L’étudiant peut réessayer ou se déconnecter.  

---
### CU06 - Comparer Cours

**Acteurs** : Étudiant (principal)  
**Préconditions** : L’étudiant est connecté et a accès aux cours à comparer.  
**Postconditions** : L’étudiant visualise un tableau comparatif à jour des cours sélectionnés.  
**Déclencheur** : L’étudiant sélectionne l’option « Comparer cours » dans le menu ou depuis la page d’un cours.   
**Dépendances** : CU5 : le tableau de bord peut servir de point de départ pour la sélection de cours, CU11 : fiches de cours pour récupérer les informations détaillées, CU10 : résultats académiques des cours à comparer, CU8 : avis étudiants (optionnel, si affiché dans la comparaison)  
**But** : Permettre à l’étudiant de comparer plusieurs cours pour évaluer la charge de travail et faciliter son choix.

---
### CU07 - Créer/Mettre à jour un profil étudiant

**Acteurs** : Étudiant (principal), Système (secondaire)  
**Préconditions** : Pour la création : L'étudiant n'a pas de compte. Pour la mise à jour : L'étudiant est authentifié.  
**Postconditions** : Succès (Création) : Nouveau profil créé. Succès (Mise à jour) : Modifications sauvegardées. Échec : Message d'erreur affiché.  
**Déclencheur** : Création : Nouvel utilisateur s'inscrit. Mise à jour : Étudiant authentifié modifie son profil.  
**Dépendances** : Prérequis pour les CUs nécessitant un profil personnalisé.  
**But** : Permettre à un étudiant de créer et maintenir son profil pour personnaliser son expérience. 

---
### CU08 - Afficher les avis étudiants pour un cours

**Acteurs** : Étudiant (principal), Système (secondaire)  
**Préconditions** : Le cours existe. Il existe au moins un avis approuvé et le seuil d'agrégation (n≥5) est atteint.  
**Postconditions** : Succès : Avis agrégés et commentaires affichés. Échec : Message "Avis insuffisants" affiché.  
**Déclencheur** : L'étudiant consulte la fiche d'un cours et souhaite voir la section avis.  
**Dépendances** : Dépend du CU "Modérer les avis" (CU02) et des CUs "Rechercher" (CU09) ou "Consulter fiche" (CU11).  
**But** : Permettre à l'étudiant de consulter les avis pour évaluer la difficulté et charge de travail.

---
### CU09 - Rechercher cours

**Acteurs** : Étudiant (principal)  
**Préconditions** : L’étudiant est connecté et a un profil existant.  
**Postconditions** : La liste des cours correspondant à la recherche est affichée avec indication d’éligibilité.  
**Déclencheur** : L’étudiant saisit un code, titre ou mot-clé dans la barre de recherche et appuie sur « Rechercher ».    
**Dépendances** :  API Planifium : accès aux informations des cours, CU10 : pour afficher les résultats académiques d’un cours trouvé, CU11 : pour accéder à la fiche détaillée d’un cours,Base de données : vérifier l’éligibilité de l’étudiant pour certains cours
**But** : Permettre à l’étudiant de trouver des cours selon différents critères (code, titre, mots-clés) et vérifier son éligibilité.  

**Scénario principal: **  
1) L’étudiant ouvre la section « Exigences de diplomation ».  
2) Il choisit un cours à évaluer.  
3) Le système vérifie la correspondance entre le cours et les catégories du programme.  
4) Le système indique si le cours est obligatoire, à option ou hors programme.  
5) L’étudiant consulte les informations affichées et décide s’il souhaite l’ajouter à sa planification.  

**Scénario alternatif:**  
2a. Le cours sélectionné ne fait pas partie du programme.  
 2a.1. Le système affiche un message : « Ce cours n’appartient pas à votre programme. »  
 2a.2. Le système propose de vérifier si le cours peut être reconnu comme équivalent ou option libre.  
 2a.3. L’étudiant clique sur « Vérifier les équivalences ».  
 2a.4. Le système affiche la liste des cours similaires ou équivalents.  
 2a.5. L’étudiant choisit un cours équivalent et relance la vérification.

 
---
### CU10 - Afficher résultats académiques d’un cours

**Acteurs** : Étudiant (principal)  
**Préconditions** :  L’étudiant est connecté et consulte la fiche d’un cours existant.  
**Postconditions** : Les résultats académiques disponibles du cours sont affichés.  
**Déclencheur** : L’étudiant clique sur le bouton ou le lien « Résultats académiques » depuis la fiche d’un cours.  
**Dépendances** :  CU11 : fiche de cours contenant le lien ou bouton pour afficher les résultats,API Planifium : pour récupérer les statistiques globales du cours, Base de données : stockage et accès aux résultats agrégés (si nécessaire)  
**But** : Permettre à l’étudiant de consulter les statistiques agrégées d’un cours pour mieux évaluer la charge et le niveau de difficulté.  

---
### CU11 - Consulter la fiche d’un cours

**Acteurs** : Étudiant (principal)  
**Préconditions** : L'étudiant a un profil et y est connecté. La fiche du cours qu'il recherche existe.  
**Postconditions** : Les données détaillées du cours recherché sont affichées.  
**Déclencheur** : L'étudiant clique sur le cours désiré dans la liste des cours correspondant à sa recherche dans le module de recherche de cours.  
**Dépendances** : CU7: l'étudiant doit avoir un profil. CU9: l'étudiant doit pouvoir recherche le cours.  
**But** : Accéder aux données pertinentes d'un cours donné afin d'éclairer les décisions de l'étudiant.  

---

### CU12 - Créer une simulation d’horaire

**Acteurs** : Utilisateur (principal)  
**Préconditions** : L’utilisateur est connecté et a sélectionné au moins un cours disponible pour la session à venir.  
**Postconditions** : Une simulation d’horaire est générée et enregistrée; les conflits éventuels sont signalés.  
**Déclencheur** : L’utilisateur clique sur « Créer une simulation » ou ajoute un cours à son horaire simulé.  
**Dépendances** : Données d’horaires de l’API Planifium et informations de cours dans la base locale.  
**But** : Offrir à l’étudiant une vue interactive de son horaire potentiel afin d’évaluer la charge totale et détecter d’éventuels conflits.

**Scénario principal**  
1) L’étudiant accède à l’outil de simulation.  
2) Il sélectionne un trimestre et des cours souhaités.  
3) Le système récupère les horaires de chaque cours via Planifium.  
4) Le système affiche une visualisation hebdomadaire de l’horaire.  
5) L’étudiant ajoute, déplace ou supprime des cours dans la simulation.  
6) Le système calcule la charge totale de travail et les conflits d’horaire.  
7) L’étudiant enregistre ou exporte la simulation.  

**Scénarios alternatifs**  
3a. Un ou plusieurs cours ne sont pas disponibles à la session choisie.  
 3a.1. Le système affiche “Cours non offert ce trimestre.”  
 3a.2. Il suggère d’autres cours similaires disponibles.  

4a. Conflit horaire entre deux cours.  
 4a.1. Le système signale le conflit visuellement.  
 4a.2. L’étudiant choisit quel cours garder.  

6a. L’étudiant dépasse le nombre de crédits permis.  
 6a.1. Le système affiche un avertissement et empêche la sauvegarde.  


---

### CU13 - Consulter la place d'un cours dans le programme

**Acteurs** : Utilisateur (principal)  
**Préconditions** : L’utilisateur a défini son programme ou son profil académique (ex. Baccalauréat en informatique).  
**Postconditions** : Le système affiche la contribution du cours choisi dans les catégories du programme (ex. cours obligatoires, à option, crédits restants).  
**Déclencheur** : L’utilisateur consulte un cours et sélectionne l’option « Voir contribution au programme ».  
**Dépendances** : Base de données des programmes, API Planifium (structure du programme et types de cours).  
**But** : Aider l’étudiant à comprendre l’impact d’un cours sur sa progression vers le diplôme, et à planifier ses choix en fonction des exigences restantes.
