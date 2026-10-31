# Violations SOLID – Résidence Campus Vert

Analyse du module `residence-legacy` du dépôt [Tetsuyass/refactoring-examen-groupe-i](https://github.com/Tetsuyass/refactoring-examen-groupe-i/tree/main) (branche `main`).

Chemin des sources : `residence-legacy/src/main/java/fr/efrei/residence/`

## SRP : responsabilité unique

| Défaut                                                                                                                                          | Emplacement                                       | Principe violé |
|-------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|----------------|
| `GestionResidence` fait tout : inscription, parc, attribution, loyers, solvabilité, caution, préavis, sortie, relances, rapport, notifications. | `GestionResidence.java` (classe entière, L10-421) | **SRP**        |
| `inscrireEtudiant` mélange validation métier, persistance, calcul de l'identifiant et affichage console.                                        | `GestionResidence.java` L19-60                    | **SRP**        |
| `Bail` (entité métier) génère aussi le texte de la quittance, avec mise en forme et formatage des euros.                                        | `Bail.java` L75-107                               | **SRP**        |
| `genererRapport()` calcule les statistiques et construit la chaîne d'affichage.                                                                 | `GestionResidence.java` L347-401                  | **SRP**        |
| `Database` mélange connexion, génération SQL par concaténation, cache mémoire et tri.                                                           | `Database.java` L11-63                            | **SRP**        |

## OCP : ouvert à l'extension / fermé à la modification

| Défaut                                                                                                                                                                               | Emplacement                                                    | Principe violé |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|----------------|
| Chaîne de `if/else` sur le type de logement pour le loyer de base. Un nouveau type oblige à modifier la classe.                                                                      | `CalculateurLoyer.java` L12-23                                 | **OCP**        |
| Options codées en dur (`menage`, `parking`, `laverie`, `fibre`, `assurance`) avec des cas particuliers `COLOCATION`. Une nouvelle option oblige à modifier la signature et le corps. | `CalculateurLoyer.java` L29-39                                 | **OCP**        |
| Remise par profil (`BOURSIER`, `ALTERNANT`) en `if/else`. Un nouveau profil oblige à modifier la méthode.                                                                            | `CalculateurLoyer.java` L42-48                                 | **OCP**        |
| Règles par profil dispersées : solvabilité, caution, frais de dossier, préavis, pénalité de retard. Chaque nouveau profil touche au moins cinq endroits.                             | `GestionResidence.java` L109-115, L149-165, L207-212, L238-242 | **OCP**        |
| Choix du canal de notification par `switch` / `if` répété. Un nouveau canal oblige à modifier les deux.                                                                              | `GestionResidence.java` L323-338 et L407-419                   | **OCP**        |
| Création des logements par `if/else` sur une chaîne, dupliquée dans un `switch` à un autre endroit.                                                                                  | `GestionResidence.java` L70-79 et `Residence.java` L39-55      | **OCP**        |
| Le rapport compte chaque type en dur (`nbStudios`, `nbT1`, `nbT2`…).                                                                                                                 | `GestionResidence.java` L350-381                               | **OCP**        |

## LSP : substitution de Liskov

| Défaut                                                                                                                                                                             | Emplacement                                                    | Principe violé |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------|----------------|
| `Colocation.loger()` et `liberer()` lèvent `UnsupportedOperationException`, alors que le contrat du parent est de loger ou libérer. Un `Logement` n'est pas substituable.          | `Colocation.java` L16-24                                       | **LSP**        |
| `Studio`, `T1` et `T2` lèvent `UnsupportedOperationException` pour `ajouterColocataire`, `retirerColocataire`, `getColocataires`, `getSurfaceBalcon` et `nettoyerPartiesCommunes`. | `Studio.java` L11-34, `T1.java` L11-21, `T2.java` L14-35       | **LSP**        |
| Comportements incohérents d'un sous-type à l'autre pour la même méthode : `getColocataires()` renvoie `null` (T1) ou lève une exception (Studio, T2).                              | `T1.java` L19-21 vs `Studio.java` L19-21                       | **LSP**        |
| Le code appelant doit tester `instanceof Colocation` et utiliser `try/catch UnsupportedOperationException` pour contourner les sous-types non substituables.                       | `GestionResidence.java` L130-134, L263-268, L301-305, L369-372 | **LSP**        |

## ISP : ségrégation des interfaces

| Défaut                                                                                                                                                         | Emplacement                                                     | Principe violé |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------|----------------|
| Interface « fourre-tout » qui impose la colocation, le balcon et l'entretien à tous les logements.                                                             | `ILogement.java` L30-43                                         | **ISP**        |
| Les classes clientes sont forcées d'implémenter des méthodes inutiles, d'où les exceptions et les stubs vides.                                                 | `Studio.java`, `T1.java`, `T2.java` (méthodes citées plus haut) | **ISP**        |
| `T1.getSurfaceBalcon()` renvoie 0 « pour éviter les exceptions » et `nettoyerPartiesCommunes()` est vide : l'interface est contournée au lieu d'être découpée. | `T1.java` L27-33                                                | **ISP**        |

## DIP : inversion des dépendances

| Défaut                                                                                                                                                                                                                 | Emplacement                                                                                 | Principe violé |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|----------------|
| Le service métier instancie ses dépendances concrètes : `Database.getInstance()` et `new CalculateurLoyer()`.                                                                                                          | `GestionResidence.java` L14-15                                                              | **DIP**        |
| Instanciation directe de `new SmsService()` et `new CourrierService()` dans le métier, plus appel statique à `EmailService.envoyer()`. Aucune abstraction « canal de notification ».                                   | `GestionResidence.java` L325-334 et L407-418                                                | **DIP**        |
| Le métier dépend de l'état global statique : `Residence.logements`, `Residence.trouver`, `Database.etudiants/baux`, compteurs statiques. Les tests doivent vider ces champs, ce que `LegacyAdapter` doit « bricoler ». | `GestionResidence.java` L65, L84, L97, L355 ; `Residence.java` L26 ; `Database.java` L15-16 | **DIP**        |
| Le code manipule la classe concrète `Logement` alors qu'une interface `ILogement` existe.                                                                                                                              | `GestionResidence.java` L64, L97, L262                                                      | **DIP**        |
| Le reporting lit directement les compteurs statiques `SmsService.compteurSms` et `CourrierService.totalAffranchissement`.                                                                                              | `GestionResidence.java` L397-398                                                            | **DIP**        |

## Autres défauts (hors SOLID)

- **Primitive obsession** : le statut du bail, le profil, le canal et le type de logement sont des `String` (`Bail.java` L15, `Etudiant.java` L13 et L17). C'est ce qui alimente tous les `if/else` ci-dessus.
- **Injection SQL** : les requêtes sont construites par concaténation (`Database.java` L34-54).
- **Champs publics** : `Bail.parking`, `Bail.paiements`, `Bail.soldeDu`, `Paiement.*`.