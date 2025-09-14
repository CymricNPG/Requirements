
-----------------------------------------------------
Projektplan
-----------------------------------------------------
Hier ist der **komplette, überarbeitete Projektplan** mit **früher I18N-Integration** und klaren Meilensteinen. Der Plan ist in **kleine, umsetzbare Schritte** unterteilt, die jeweils ein **funktionierendes MVP** liefern.

---

## **Projektplan: IntelliJ-Plugin für dateibasiertes Anforderungsmanagement**
**Ziel:** Schrittweise Umsetzung mit I18N von Anfang an.

---

### **Phase 1: Grundgerüst + I18N (MVP 1)**
**Ziel:** Plugin-Grundgerüst mit I18N, Konfiguration, und einfacher Anforderungsverwaltung.

| Schritt | Aufgabe                                                                                     | Ergebnis (MVP)                                                                                     | Technische Hinweise                                                                                     |
|---------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| 1.1     | **IntelliJ-Plugin-Projekt einrichten**                                                     | Leeres Plugin mit Menüeintrag "Anforderungsmanagement".                                          | Nutzung des [IntelliJ Plugin SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html).           |
| 1.2     | **I18N-Infrastruktur aufbauen**                                                            | Resource-Bundles für UI-Texte (`messages_en/de.properties`).                                    | Beispiel: `requirement.create.title=Create New Requirement` / `Neue Anforderung erstellen`.           |
| 1.3     | **Konfigurationsdatei (`config.yaml`) parsen**                                            | Plugin liest `language`, `id-format`, `types`, `states`, etc.                                    | Nutzung von [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml) für YAML-Parsing.                  |
| 1.4     | **Sprachauswahl in UI**                                                                    | Dropdown-Menü zur Auswahl der Sprache (Englisch/Deutsch).                                       | Sprache wird in `config.yaml` gespeichert.                                                             |
| 1.5     | **Anforderungs-Assistent (UI mit I18N)**                                                   | UI zum Anlegen von Anforderungen (Pflichtfelder: ID, Kurztext) in der gewählten Sprache.         | Felder: ID (auto), Kurztext, Typ, Status (Dropdowns mit I18N).                                         |
| 1.6     | **Automatische ID-Verwaltung**                                                             | IDs werden nach `id-format` (z. B. `REQ-AUTO-{}`) vergeben.                                      | Counter für `{}` wird in einer `.counter`-Datei pro Domäne gespeichert.                              |
| 1.7     | **AsciiDoc-Datei-Generierung**                                                             | Anforderungen werden als `<ID>.adoc` gespeichert.                                               | Nutzung von [Asciidoctor](https://asciidoctor.org/) für spätere Generierung.                          |
| 1.8     | **Tabellarischer Editor (Grundfunktionen + I18N)**                                         | Anzeige aller Anforderungen in einem Verzeichnis (Spalten: ID, Kurztext, Status).                | Spaltenüberschriften werden aus Resource-Bundles geladen.                                          |

---
### **Phase 2: Referenzen + Konsistenzprüfung (MVP 2)**
**Ziel:** Referenzen, Autovervollständigung, und Echtzeit-Konsistenzprüfung.

| Schritt | Aufgabe                                                                                     | Ergebnis (MVP)                                                                                     | Technische Hinweise                                                                                     |
|---------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| 2.1     | **Referenzformat `<<Typ:ID>>` implementieren**                                              | Referenzen werden in `.adoc`-Dateien als `<<depends:REQ-AUTO-1>>` gespeichert.                     | Parsing mit Regex: `<<(\w+):([\w-]+)>>`.                                                                |
| 2.2     | **Autovervollständigung für Referenzen (I18N)**                                            | IntelliJ schlägt gültige IDs und **lokalisierte Referenztypen** vor (z. B. "Abhängigkeit").      | Nutzung der `summary`-Felder aus `config.yaml`.                                                        |
| 2.3     | **Konsistenzprüfung (einzelne Datei)**                                                    | Ungültige Referenzen werden im Editor markiert (Fehlermeldung in Projektsprache).                 | Fehler: `"Ungültige Referenz: <<{0}>>"` (Resource-Bundle).                                            |
| 2.4     | **Klickbare Referenzen**                                                                   | Klick auf `<<depends:REQ-AUTO-1>>` öffnet `REQ-AUTO-1.adoc`.                                       | Nutzung von IntelliJ’s `PsiReference` für Navigation.                                                  |
| 2.5     | **Fehlerlog für Konsistenzprüfung (I18N)**                                                 | Fehler werden in einem Tool-Fenster in IntelliJ angezeigt.                                       | Log-Datei: `consistency_errors.log` im Projektverzeichnis.                                             |

---
### **Phase 3: Dokumentationsgenerierung (MVP 3)**
**Ziel:** PDF/HTML-Generierung mit Referenzauflösung und I18N.

| Schritt | Aufgabe                                                                                     | Ergebnis (MVP)                                                                                     | Technische Hinweise                                                                                     |
|---------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| 3.1     | **Zwei-Schritt-Generierung (Grundgerüst)**                                                 | Alle `.adoc`-Dateien werden zu einem Gesamtdokument zusammengestellt.                             | Nutzung von Asciidoctor’s `include`-Direktive.                                                          |
| 3.2     | **Auflösen von `<<Typ:ID>>`-Referenzen**                                                   | Referenzen werden zu AsciiDoc-Links (`xref:REQ-AUTO-1.adoc[REQ-AUTO-1]`) umgewandelt.             | Ersetzung mit Regex und Pfadauflösung.                                                                 |
| 3.3     | **Fehlerbehandlung für nicht aufgelöste Referenzen (I18N)**                                 | Nicht aufgelöste Referenzen werden im PDF als `<<Typ:ID>>` + **lokalisierter Fehlereintrag**.     | Fehlereintrag: `"Nicht aufgelöste Referenz: {0}"`.                                                     |
| 3.4     | **Sortierung nach IDs und Domänen**                                                        | Anforderungen werden alphanumerisch nach ID sortiert (Domänenpfad → ID).                       | Sortierung mit `Comparator` für Pfade (z. B. `/automotive/REQ-1` vor `/automotive/REQ-2`).         |
| 3.5     | **PDF/HTML-Generierung mit I18N**                                                          | PDF/HTML mit lokalisierten Überschriften (z. B. "Anforderungen" vs. "Requirements").             | Nutzung von Asciidoctor-PDF/HTML.                                                                     |
| 3.6     | **Fehlerlog für Generierung (I18N)**                                                        | Alle Fehler werden in `generation_errors.log` gespeichert (Projektsprache).                      | Log-Format: `[ERROR] {timestamp}: {localized_message}`.                                               |

---
### **Phase 4: Erweiterte Features (MVP 4)**
**Ziel:** Filter, Scrum-Board, und Git-Integration.

| Schritt | Aufgabe                                                                                     | Ergebnis (MVP)                                                                                     | Technische Hinweise                                                                                     |
|---------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| 4.1     | **Filter und Sortierung im tabellarischen Editor**                                          | Filter nach Status, Labels, Typ; Sortierung nach allen Spalten.                                   | Nutzung von `JTable` mit `RowFilter` und `TableRowSorter`.                                              |
| 4.2     | **Scrum-Board-Integration**                                                                 | Anforderungen als Karten im Scrum-Board (Status = Swimlanes).                                    | UI mit JavaFX oder Swing (z. B. `JPanel` mit Drag & Drop).                                              |
| 4.3     | **Drag & Drop für Statusänderungen**                                                        | Ziehen einer Karte in eine andere Swimlane ändert den Status in der `.adoc`-Datei.               | Event-Listener für Drag & Drop + Dateiaktualisierung.                                                  |
| 4.4     | **Git-Integration (Commit-Nachrichten)**                                                   | Vorschlag für Commit-Nachrichten basierend auf geänderten Feldern (I18N).                         | Beispiel: `"Updated REQ-AUTO-1: [Added] acceptance criteria"`.                                          |
| 4.5     | **Projekttemplates**                                                                        | Vorlagen für neue Projekte (Ordnerstruktur, `domain.adoc`, `config.yaml`).                        | Template-Dateien im Plugin-Bundle.                                                                    |

---
### **Phase 5: Optimierungen (MVP 5)**
**Ziel:** Performance, Dokumentation, und Feinabstimmung.

| Schritt | Aufgabe                                                                                     | Ergebnis (MVP)                                                                                     | Technische Hinweise                                                                                     |
|---------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| 5.1     | **Performance-Optimierungen**                                                              | Caching für Referenzauflösung; inkrementelle Generierung.                                         | Nutzung von `Guava Cache` für Referenzen.                                                             |
| 5.2     | **Dokumentation und Hilfe (I18N)**                                                          | Tooltips für alle Metadaten; integrierte Dokumentation im Plugin (Projektsprache).                | Hilfe-Dateien als `.adoc` im Plugin-Bundle.                                                            |
| 5.3     | **Fehlermeldungen und Logging verbessern**                                                 | Alle Fehlermeldungen und Warnungen werden geloggt (I18N).                                         | Nutzung von `java.util.logging`.                                                                       |
| 5.4     | **Tests und Qualitätsicherung**                                                            | Unit-Tests für Kernfunktionen (I18N, Referenzen, Generierung).                                    | Nutzung von JUnit 5.                                                                                  |

---
## **Zeitplan und Meilensteine**
| Phase  | Dauer   | Meilenstein                                                                                     | Abhängigkeiten                                                                                     |
|--------|---------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| Phase 1| 3 Wochen| MVP 1: Anforderungen anlegen, speichern, und tabellarisch anzeigen (mit I18N).                  | IntelliJ-Plugin-SDK, Resource-Bundles.                                                              |
| Phase 2| 2 Wochen| MVP 2: Referenzen, Autovervollständigung, und Konsistenzprüfung (mit I18N-Fehlermeldungen).      | Asciidoctor für spätere Generierung.                                                                |
| Phase 3| 3 Wochen| MVP 3: Dokumentationsgenerierung (PDF/HTML) mit I18N.                                          | Asciidoctor-PDF/HTML.                                                                               |
| Phase 4| 3 Wochen| MVP 4: Scrum-Board, Git-Integration, und Projekttemplates.                                      | JavaFX/Swing für Scrum-Board.                                                                        |
| Phase 5| 2 Wochen| MVP 5: Performance, Dokumentation, und Tests.                                                 | JUnit 5, Guava Cache.                                                                               |

---
## **Risikomanagement**
| Risiko                                                                                         | Gegenmaßnahme                                                                                     |
|-----------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| Komplexität der I18N-Implementierung                                                          | Frühzeitige Prototypen mit Resource-Bundles und Tests.                                          |
| Performance-Probleme bei vielen Dateien                                                       | Inkrementelle Verarbeitung und Caching von Anfang an.                                           |
| Integration in IntelliJ (z. B. UI, AsciiDoc-Plugin)                                           | Nutzung von IntelliJ-APIs und bestehenden Plugins (z. B. AsciiDoc-Plugin).                    |
| Unklare Anforderungen an die Dokumentationsgenerierung                                       | Frühzeitige Beispiele für generierte PDFs/HTMLs mit Platzhaltern für Referenzen.                 |

---
## **Empfohlene Tools und Technologien**
| Bereich               | Tool/Technologie                                                                               |
|-----------------------|-------------------------------------------------------------------------------------------------|
| **IntelliJ-Plugin**   | [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)              |
| **I18N**             | Java `ResourceBundle`, `.properties`-Dateien                                                   |
| **YAML-Parsing**     | [SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml)                                          |
| **AsciiDoc**         | [Asciidoctor](https://asciidoctor.org/)                                                        |
| **PDF/HTML-Generierung** | [Asciidoctor PDF](https://asciidoctor.org/docs/asciidoctor-pdf/)                              |
| **UI**               | JavaFX oder Swing (für Scrum-Board)                                                            |
| **Testing**          | JUnit 5, Mockito                                                                               |
| **Logging**          | `java.util.logging` oder SLF4J                                                                 |

---
## **Beispiel: Resource-Bundle für I18N**
### `messages_en.properties`
```properties
requirement.create.title=Create New Requirement
requirement.field.id=ID
requirement.field.summary=Summary
requirement.field.type=Type
requirement.field.status=Status
error.invalid_reference=Invalid reference: {0}
```

### `messages_de.properties`
```properties
requirement.create.title=Neue Anforderung erstellen
requirement.field.id=ID
requirement.field.summary=Kurztext
requirement.field.type=Typ
requirement.field.status=Status
error.invalid_reference=Ungültige Referenz: {0}
```

---
## **Beispiel: Konfigurationsdatei (`config.yaml`) mit I18N**
```yaml
language: de
id-format: REQ-AUTO-{}
referencetypes:
  - id: depends
    summary:
      en: Dependency
      de: Abhängigkeit
    description:
      en: This requirement depends on another.
      de: Diese Anforderung hängt von einer anderen ab.
types:
  - en: Functional
    de: Funktional
  - en: Non-Functional
    de: Nicht-funktional
states:
  - en: Draft
    de: Entwurf
  - en: Approved
    de: Freigegeben
```