# Besoins matériels, solution de stockage et d'intégration

## 1) Besoins matériels

### Serveur d'hébergement
- **Machine virtuelle Linux** pour héberger l'application web et l'API REST
  - **Explication:** Linux offre une stabilité éprouvée et un coût réduit pour les applications web. Une machine virtuelle permet d'ajuster facilement les ressources selon la demande.
- **Configuration minimale:** 2 processeurs virtuels, 4 GB de mémoire RAM, 20 GB d'espace disque
  - **Explication:** Cette configuration permet de gérer environ 500 étudiants simultanés pendant les périodes d'inscription, qui sont les moments de plus forte charge sur le système.
- **Connexion internet haute vitesse** avec certificat de sécurité HTTPS
  - **Explication:** HTTPS est essentiel pour protéger les données personnelles des étudiants et respecter la Loi 25 sur la protection des renseignements personnels au Québec.

### Accès utilisateur
- **Navigateurs web modernes** (Chrome, Firefox, Safari, Edge) sur ordinateur ou mobile
  - **Explication:** Les étudiants peuvent accéder au système depuis n'importe quel appareil sans installation, ce qui maximise l'accessibilité du service.
- **Aucune installation requise** côté client
  - **Explication:** Réduit les barrières d'entrée et le support technique nécessaire.

## 2) Solution de stockage

### Base de données
- **PostgreSQL** comme système de gestion de base de données relationnelle
  - **Explication:** PostgreSQL gère efficacement les relations complexes entre cours, prérequis, résultats et avis. Il permet aussi de faire des requêtes sophistiquées pour les comparaisons de cours et les recommandations personnalisées.

### Organisation des données
- **Tables principales stockées:**
  - Catalogue de cours (codes, titres, crédits, prérequis)
  - Résultats académiques agrégés (moyennes, nombres d'inscrits, taux d'échec)
  - Avis étudiants anonymisés (difficulté perçue, charge de travail, commentaires)
  - Préférences de personnalisation (sans information identifiante)
  - **Explication:** Cette structure sépare clairement les différents types d'information tout en permettant de les croiser pour offrir une vue complète de chaque cours.

### Protection des données
- **Anonymisation complète des avis** avec agrégation minimum de 5 réponses
  - **Explication:** Protège l'identité des étudiants qui donnent leur avis tout en fournissant des statistiques fiables. Le seuil de 5 empêche d'identifier un étudiant particulier.
- **Sauvegardes quotidiennes** avec possibilité de restauration rapide
  - **Explication:** En cas de problème technique, on peut récupérer les données avec une perte maximale de 24 heures, ce qui est acceptable pour ce type d'application.
- **Conformité à la Loi 25** sur la protection des renseignements personnels
  - **Explication:** Obligation légale au Québec de protéger les données personnelles des utilisateurs et de limiter leur collecte au strict nécessaire.

### Formats de données acceptés
- **CSV pour les résultats académiques** fournis par l'université
  - **Explication:** Format standard utilisé par l'administration pour transmettre les statistiques de fin de session de manière structurée.
- **JSON pour les avis Discord** collectés via le bot
  - **Explication:** Format flexible qui permet de capturer différents types de feedback (notes numériques, texte libre) tout en restant facile à valider.
- **API REST pour Planifium** (catalogue officiel)
  - **Explication:** Connexion directe au système officiel de l'université pour avoir toujours les informations à jour sur les cours et horaires.

## 3) Solution d'intégration

### Connexion avec Planifium (données officielles)
- **Synchronisation quotidienne** du catalogue de cours et des horaires
  - **Explication:** Les changements au catalogue sont peu fréquents, une mise à jour par jour suffit et évite de surcharger l'API de l'université.
- **Cache local des données** pour réduire la dépendance
  - **Explication:** Si Planifium est temporairement indisponible, les étudiants peuvent continuer à consulter les informations avec les dernières données connues.
- **Validation des prérequis** en temps réel lors des recherches
  - **Explication:** Permet d'informer immédiatement l'étudiant s'il est éligible ou non à un cours selon son parcours.

### Collecte des avis via Discord
- **Bot Discord** qui structure automatiquement les avis étudiants
  - **Explication:** Les étudiants utilisent déjà Discord pour discuter des cours. Le bot permet de collecter ces avis de manière structurée sans forcer un changement d'habitude.
- **Validation et modération** automatique du contenu
  - **Explication:** Filtre les contenus inappropriés et vérifie que les avis respectent le format attendu (note de difficulté, charge de travail, commentaire optionnel).
- **File d'attente** pour traiter les avis sans ralentir Discord
  - **Explication:** Même si beaucoup d'avis arrivent simultanément (fin de session), le système les traite progressivement sans perdre de données.

### Import des résultats académiques
- **Interface d'upload sécurisée** pour les fichiers CSV
  - **Explication:** Seuls les administrateurs autorisés peuvent importer les résultats officiels, garantissant l'authenticité des données.
- **Validation en plusieurs étapes** avant l'import
  - **Explication:** Vérifie que les moyennes sont entre 0 et 100, que le nombre d'échecs ne dépasse pas le nombre d'inscrits, etc. Cela évite de corrompre la base avec des données erronées.
- **Historique conservé** de tous les imports
  - **Explication:** Permet de tracer qui a importé quoi et quand, important pour l'audit et la résolution de problèmes.

### Gestion de la disponibilité
- **Mode dégradé intelligent** si une source est indisponible
  - **Explication:** Le système continue de fonctionner avec les dernières données connues et affiche un message pour informer les utilisateurs de la situation.
- **Surveillance automatique** des temps de réponse et taux d'erreur
  - **Explication:** Permet de détecter rapidement les problèmes d'intégration et d'intervenir avant que les utilisateurs soient impactés.
- **Documentation API publique** pour faciliter les intégrations futures
  - **Explication:** D'autres services de l'université pourraient vouloir se connecter à notre système pour enrichir leurs propres outils.

### Avantages de cette architecture
- **Résilience:** Le système reste utilisable même si une source externe tombe
- **Performance:** Les données sont locales donc les recherches sont rapides
- **Évolutivité:** On peut facilement ajouter de nouvelles sources de données
- **Traçabilité:** Tout est enregistré pour pouvoir analyser les problèmes
