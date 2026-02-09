# JavaFX Launcher & Updater

Launcher et système de mise à jour automatique développé en **Java** avec **JavaFX**, utilisant **Google Cloud Storage** comme backend de distribution des fichiers.

Ce projet a pour objectif de fournir une solution simple, rapide et fiable pour le déploiement, la mise à jour et le lancement d’un logiciel desktop.

---

## 🚀 Fonctionnalités

- Interface graphique moderne basée sur **JavaFX**
- Téléchargement sécurisé des fichiers depuis **Google Cloud Storage**
- Vérification de version automatique
- Mise à jour incrémentielle des fichiers
- Barre de progression et logs en temps réel
- Gestion des erreurs réseau
- Lancement automatique du logiciel après mise à jour
- Compatible Windows / Linux / macOS (selon JRE)

---

## 🛠️ Technologies utilisées

- **Java 17+**
- **JavaFX**
- **Google Cloud Storage SDK**
- **Gradle / Maven** (selon configuration)
- **JSON** pour la gestion des versions et manifestes

---

## 📦 Architecture du projet

src/
├─ main/
│ ├─ java/
│ │ ├─ launcher/
│ │ │ ├─ Main.java
│ │ │ ├─ ui/
│ │ │ ├─ updater/
│ │ │ └─ gcs/
│ └─ resources/
│ ├─ fxml/
│ ├─ css/
│ └─ assets/


- `ui` : gestion de l’interface JavaFX  
- `updater` : logique de vérification et de mise à jour  
- `gcs` : communication avec Google Cloud Storage  

---

## ☁️ Google Cloud Storage

Le launcher utilise **Google Cloud Storage** pour :
- Stocker les fichiers du logiciel
- Héberger le fichier de version (`manifest.json`)
- Distribuer les mises à jour

### Exemple de structure du bucket :

/releases/
├─ manifest.json
├─ app.jar
├─ lib/
└─ assets/


---

## ⚙️ Configuration

1. Créer un projet Google Cloud
2. Activer **Cloud Storage**
3. Créer un bucket
4. Configurer une **clé de service**
5. Ajouter la clé dans le projet (ou via variable d’environnement)

```bash
export GOOGLE_APPLICATION_CREDENTIALS="credentials.json"

🌿 Branches

Ce dépôt utilise une structure simple :

master : contient l’intégralité du projet (code source, ressources, configuration)

Aucune autre branche n’est utilisée actuellement.
Le développement, les correctifs et les mises à jour sont directement réalisés sur la branche master.
