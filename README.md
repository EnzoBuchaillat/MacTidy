# 🧹 MacTidy

Utilitaire en ligne de commande pour macOS, écrit en Java (`java.nio`), qui repère les fichiers volumineux et inutilisés depuis longtemps dans un dossier.

> Projet d'apprentissage en cours de développement (BUT Informatique).

## ✅ Fonctionnalités

- **Choix du dossier** : l'utilisateur saisit un chemin à partir de son dossier personnel (par exemple `Downloads`). Le programme redemande tant que le chemin n'existe pas ou n'est pas un dossier, avec un message d'erreur différent pour chaque cas.
- **Filtre par taille** : seuls les fichiers dont la taille est strictement supérieure au seuil saisi (en Mo) sont affichés.
- **Filtre par inactivité** : seuls les fichiers non modifiés depuis strictement plus de N jours sont affichés (d'après la date de dernière modification).
- **Saisies sécurisées** : si l'utilisateur tape autre chose qu'un nombre positif, le programme affiche une erreur et redemande au lieu de planter.
- **Affichage** : pour chaque fichier trouvé, le chemin complet, la taille en Mo (2 décimales) et le nombre de jours depuis la dernière modification.

## ⚠️ Limites actuelles

- **Lecture seule** : MacTidy ne supprime ni ne déplace aucun fichier, il se contente de les lister.
- **Pas de sous-dossiers** : seuls les fichiers placés directement dans le dossier choisi sont analysés.
- **Dossier personnel uniquement** : le chemin saisi est toujours relatif au dossier personnel de l'utilisateur.
- **Unité des tailles** : 1 Mo = 1 024 × 1 024 octets. Le Finder de macOS compte en base 10 (1 Mo = 1 000 000 octets), donc les tailles peuvent légèrement différer.

## 🚀 Lancer le projet

Prérequis : **Java 21**.

- **Avec IntelliJ IDEA** : ouvrir le dossier du projet, puis lancer la classe `Main`.
- **En ligne de commande**, depuis la racine du projet :

```bash
java -cp out Main
```

## 🗺️ Feuille de route

Fonctionnalités **prévues**, pas encore disponibles :

- [ ] Recherche de doublons (comparaison par taille, puis par hash)
- [ ] Mise à la corbeille au lieu d'une suppression définitive
- [ ] Mode dry-run (simulation sans rien modifier)
- [ ] Automatisation avec `launchd`

## 🌿 Organisation Git

- `main` : versions stables uniquement (taguées)
- `dev` : branche d'intégration
- `feature/...`, `fix/...`, `docs/...` : une branche par modification, fusionnée dans `dev` via une Pull Request
