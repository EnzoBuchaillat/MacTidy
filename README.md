# 🧹 MacTidy

Utilitaire en ligne de commande pour macOS, écrit en Java (`java.nio`), qui repère les fichiers volumineux et inutilisés depuis longtemps dans un dossier, puis propose de les mettre à la corbeille.

> Projet personnel en cours de développement, pour apprendre la gestion de fichiers en Java.

## ✅ Fonctionnalités

- **Choix du dossier** : l'utilisateur saisit un chemin à partir de son dossier personnel (par exemple `Downloads`). Le programme redemande tant que le chemin n'existe pas ou n'est pas un dossier, avec un message d'erreur différent pour chaque cas.
- **Filtre par taille** : seuls les fichiers dont la taille est strictement supérieure au seuil saisi (en Mo) sont retenus.
- **Filtre par inactivité** : seuls les fichiers non modifiés depuis strictement plus de N jours sont retenus (d'après la date de dernière modification).
- **Saisies sécurisées** : si l'utilisateur tape autre chose qu'un nombre positif, le programme affiche une erreur et redemande au lieu de planter.
- **Aperçu avant action** : pour chaque fichier trouvé, le chemin complet, la taille en Mo (2 décimales) et le nombre de jours depuis la dernière modification, puis un résumé avec le nombre de fichiers et la taille totale.
- **Confirmation** : le programme demande `o` ou `n` avant de toucher à quoi que ce soit, et redemande tant que la réponse n'est pas valide. Avec `n`, aucun fichier n'est modifié.
- **Mise à la corbeille** : avec `o`, les fichiers trouvés sont déplacés dans la corbeille de macOS (et non supprimés définitivement), donc récupérables. Le programme affiche le nombre de fichiers déplacés et signale ceux qui n'ont pas pu l'être.

## ⚠️ Limites actuelles

- **Tout ou rien** : la confirmation s'applique à tous les fichiers trouvés, on ne peut pas en choisir certains seulement.
- **Pas de sous-dossiers** : seuls les fichiers placés directement dans le dossier choisi sont analysés.
- **Dossier personnel uniquement** : le chemin saisi est toujours relatif au dossier personnel de l'utilisateur.
- **Arrêt en cas d'erreur de lecture** : si un fichier ou dossier ne peut pas être lu pendant l'analyse, le scan s'arrête.
- **Unité des tailles** : 1 Mo = 1 024 × 1 024 octets. Le Finder de macOS compte en base 10 (1 Mo = 1 000 000 octets), donc les tailles peuvent légèrement différer.

## 🚀 Lancer le projet

Prérequis : **Java 21**.

- **Avec IntelliJ IDEA** : ouvrir le dossier du projet, puis lancer la classe `Main`.
- **En ligne de commande**, depuis la racine du projet :

```bash
javac -d out src/*.java
java -cp out Main
```

> 💡 Pour essayer sans risque, créez un dossier de test avec de faux fichiers et vieillissez-les avec `touch -t 202501010000 fichier.txt`.

## 🗺️ Feuille de route

Fonctionnalités **prévues**, pas encore disponibles :

- [ ] Recherche de doublons (comparaison par taille, puis par hash)
- [ ] Automatisation avec `launchd`

## 🌿 Organisation Git

- `main` : versions stables uniquement (taguées)
- `dev` : branche d'intégration
- `feature/...`, `fix/...`, `docs/...` : une branche par modification, fusionnée dans `dev` via une Pull Request
