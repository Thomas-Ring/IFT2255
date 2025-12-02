Voici la version améliorée avec la définition de l'API REST et l'ajout des contraintes manquantes :

# Description du domaine

Tout étudiant a déjà eu des difficultés pour faire son choix de cours. Chacun essaie d'optimiser son cheminement dépendamment de beaucoup de facteurs (la charge de travail, la difficulté perçue, les prérequis, la compatibilité avec leur horaire et leurs intérêts personnels) qui peuvent provoquer des interrogations et du stress. Ce projet est une réponse directe à toutes ces problématiques puisqu'il concerne la planification académique et le choix de cours des étudiants de l'Université de Montréal, en particulier ceux du Département d'informatique et de recherche opérationnelle (DIRO).  

Les étudiants basent leur jugement sur plusieurs sources qui, réunies ensemble, peuvent mener à la confusion vis-à-vis du manque de transparence quant à la charge réelle de travail, le rythme du cours ou le niveau de difficulté ressenti. On peut notamment distinguer les données officielles (plans de cours, résultats académiques agrégés, API Planifium) fournissant une base fiable, mais restent partielles. En parallèle, les sources informelles (forums, discussions sur Discord, bouche-à-oreille) regorgent d'informations précieuses mais non structurées. 

La plateforme a pour fonction d'épurer toutes ces informations dans une interface web unifiée, accessible également via une **API REST** (Application Programming Interface - Representational State Transfer). Une API REST est une interface de programmation qui permet à différents systèmes de communiquer entre eux via des requêtes HTTP standardisées (GET pour lire, POST pour créer, PUT pour modifier, DELETE pour supprimer). Dans notre contexte, cela signifie que d'autres applications pourront interroger notre système pour obtenir des informations sur les cours de manière automatisée et structurée.

Elle combinera les données officielles de l'Université (issues de Planifium et des fichiers CSV de résultats académiques) avec les avis étudiants recueillis via un bot Discord. Au-delà du système de confidentialité qui sera mis en place, des options comme rechercher, comparer et filtrer des cours, consulter des tableaux de bord interactifs et bénéficier d'une personnalisation fondée sur le profil seront présentes pour maximiser l'efficacité et rentrer dans une nouvelle ère où le choix de cours devient trivial.

Prenant tout cela en compte, l'étudiant se connecte à la plateforme, crée un profil (intérêts, préférences).  
Il peut ensuite rechercher des cours, consulter leurs informations (officielles et issues des avis Discord), comparer plusieurs cours et obtenir des recommandations.  
Le système interagit avec l'API Planifium pour récupérer les données de cours, agrège les résultats académiques à partir d'un fichier CSV, et affiche les avis pertinents dès qu'un seuil de 5 avis est atteint.  

---

## Acteurs du domaine

**Étudiant :** utilisateur principal qui recherche, compare et consulte les cours selon ses préférences et son profil. Il peut personnaliser son expérience et soumettre des avis via Discord.  

**Modérateur :** supervise les avis publiés par les étudiants, s'assure de leur pertinence et supprime le contenu inapproprié afin de préserver la fiabilité des données.  

**TGDE :** intervenant administratif qui consulte les données agrégées (résultats, statistiques) et signale les erreurs ou incohérences liées aux programmes officiels.  

**Système Planifium :** système externe de l'université fournissant les données officielles sur les cours (codes, crédits, prérequis, horaires) via son API pour alimenter la plateforme.  

**Bot Discord :** outil automatisé collectant les avis étudiants sur les cours et les transmettant au système sous forme structurée pour agrégation et affichage.  

---

## Dépendances du système

Le système repose sur plusieurs dépendances logicielles, techniques et organisationnelles essentielles à son fonctionnement :

**Dépendance à l'API Planifium**  
   - Fournit les informations officielles sur les cours (codes, crédits, horaires, pré/co-requis, structures de programmes).  
   - Toute modification ou indisponibilité de l'API peut limiter la précision des données affichées sur la plateforme.

**Dépendance aux fichiers CSV de résultats académiques**  
   - Source de données agrégées (moyenne, taux d'échec, nombre d'inscrits).  
   - Ces fichiers doivent être régulièrement mis à jour pour garantir l'exactitude des statistiques présentées.

**Dépendance au bot Discord (avis étudiants)**  
   - Outil externe chargé de collecter et transmettre les avis étudiants au système.  
   - Le fonctionnement de la plateforme dépend du volume et de la fiabilité de ces avis, qui ne sont affichés qu'à partir du seuil **n ≥ 5**.

**Dépendance à la base de données interne**  
   - Centralise et relie toutes les données issues des différentes sources (Planifium, CSV, Discord, profils).  
   - Une défaillance ou corruption des données pourrait impacter la cohérence du système.

**Dépendance au profil utilisateur**  
   - Les fonctions de personnalisation (recommandations, favoris, simulations) dépendent des informations renseignées dans le profil (programme, préférences, cheminement).  
   - Ces données doivent être protégées et synchronisées selon les exigences de confidentialité.

**Dépendance à l'infrastructure d'hébergement**  
   - Le système nécessite un hébergement web stable pour le frontend, l'API REST et la base de données.  
   - La disponibilité, la sécurité et les sauvegardes régulières sont indispensables à la fiabilité globale du service.

**Dépendance aux règles institutionnelles**  
   - Les données sur les programmes et les exigences de diplomation proviennent des structures officielles de l'Université de Montréal.  
   - Tout changement administratif (programme, cours obligatoires, crédits requis) doit être reflété dans la plateforme pour conserver la validité des recommandations.

---

En résumé, le système dépend d'un **écosystème interconnecté** entre :
- les **sources externes** (Planifium, fichiers CSV, Discord),  
- les **acteurs humains** (étudiants, TGDE, modérateurs),  
- et les **composants internes** (API REST, base de données, interface web).  

La qualité, la disponibilité et la cohérence de ces dépendances conditionnent directement la fiabilité et la pertinence de la plateforme d'aide au choix de cours.

## Hypothèses et contraintes

### Hypothèses

Les hypothèses suivantes définissent les conditions de base supposées vraies lors de la conception de la plateforme d'aide au choix de cours.

#### 1. Sources de données
- L'**API Planifium** est disponible et fournit des données fiables sur les cours et programmes.  
- Les **résultats académiques agrégés** sont transmis régulièrement sous format CSV.  
- Les **avis étudiants** collectés via Discord respectent un format standard et peuvent être intégrés automatiquement.

#### 2. Confidentialité
- Les avis étudiants sont **anonymisés** avant leur affichage.  
- Aucune donnée personnelle (nom, identifiant Discord, etc.) n'est stockée.  
- Les profils étudiants sont protégés conformément à la **Loi 25** sur la protection des renseignements personnels.

#### 3. Données et fiabilité
- Un cours doit avoir **au moins 5 avis (n ≥ 5)** pour que les statistiques soient affichées.  
- Les fichiers CSV et les données Planifium sont considérés comme exacts et à jour.  

#### 4. Technique
- Le système est **hébergé sur un serveur stable** avec une connexion sécurisée (HTTPS).  
- Les services externes (Planifium, Discord) sont disponibles la plupart du temps.  
- L'application web respecte les standards modernes (API REST, JSON).  

#### 5. Utilisation
- Les **étudiants du DIRO** sont les principaux utilisateurs.  
- Ils utilisent un **navigateur récent** et une connexion Internet stable.  
- La plateforme est conçue pour être simple et intuitive, sans besoin de formation.

#### 6. Hors du périmètre
- Le système **n'effectue pas d'inscriptions officielles** aux cours.  
- L'interface visuelle finale est **prototypique** et sert à illustrer les fonctionnalités.  

### Contraintes

Les contraintes suivantes limitent ou encadrent la conception et l'implémentation du système.

#### 1. Contraintes légales et réglementaires
- **Conformité à la Loi 25** : Toutes les données personnelles doivent être protégées selon la législation québécoise sur la protection des renseignements personnels.
- **Respect des politiques universitaires** : Le système doit se conformer aux règlements de l'Université de Montréal concernant l'utilisation des données académiques.
- **Droits d'auteur** : Les plans de cours et contenus officiels ne peuvent être reproduits sans autorisation.

#### 2. Contraintes techniques
- **Compatibilité API Planifium** : Le système doit s'adapter au format et aux limitations de l'API existante (taux de requêtes, structure des données).
- **Formats imposés** : Les résultats académiques sont fournis uniquement en CSV avec une structure prédéfinie.
- **Limite Discord** : Le bot Discord doit respecter les limites de l'API Discord (nombre de messages, fréquence des requêtes).
- **Performance** : Le système doit répondre en moins de 2 secondes pour 95% des requêtes, même en période de pointe.

#### 3. Contraintes de ressources
- **Budget limité** : Utilisation de technologies open source et d'infrastructure cloud abordable.
- **Équipe de développement** : Projet réalisé par une équipe étudiante avec disponibilité limitée.
- **Maintenance** : Le système doit être conçu pour minimiser les besoins de maintenance continue.

#### 4. Contraintes de sécurité
- **Authentification obligatoire** : Accès restreint aux étudiants de l'UdeM via authentification institutionnelle.
- **Chiffrement des communications** : Toutes les données transitent via HTTPS.
- **Anonymisation irréversible** : Les avis ne doivent jamais pouvoir être retracés à leur auteur.

#### 5. Contraintes opérationnelles
- **Disponibilité minimale** : Le système doit être disponible 95% du temps, sauf lors des maintenances planifiées.
- **Seuil d'affichage** : Les statistiques d'avis ne s'affichent qu'à partir de 5 avis minimum pour préserver l'anonymat.
- **Mise à jour des données** : Les données Planifium doivent être synchronisées au moins une fois par jour.

#### 6. Contraintes d'utilisabilité
- **Accessibilité web** : Respect des normes WCAG 2.1 niveau AA pour l'accessibilité.
- **Support multi-appareils** : Interface responsive fonctionnant sur ordinateur, tablette et mobile.
- **Langues** : Interface disponible au minimum en français, avec possibilité d'extension à l'anglais.

---

**En résumé :**  
Le projet suppose que les données externes sont accessibles et fiables, que la confidentialité est assurée, et que les étudiants utilisent la plateforme dans un contexte réaliste et stable. Les contraintes imposent un cadre légal, technique et opérationnel strict qui guide la conception et assure la conformité, la sécurité et la performance du système.
