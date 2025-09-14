# Spezifikation eines IntelliJ-Plugins für dateibasiertes Anforderungsmanagement mit AsciiDoc

---

## **1. Dateibasierte Struktur**
- **Domänen:**
    - Eine Domäne entspricht einem Dateisystemordner (z. B. `/automotive/`).
    - Jede Domäne enthält eine AsciiDoc-Datei (`domain.adoc`), die die Domäne beschreibt (mit denselben Pflichtfeldern und optionalen Feldern wie eine Anforderung).
- **Anforderungen:**
    - Eine Anforderung pro Datei: `<ID>.adoc`.
    - Hierarchie: Organisatorische Struktur über das Dateisystem (rekursiv).
- **IDs:**
    - Eindeutig pro Projekt.
    - Format pro Domäne konfigurierbar: Beliebiger Text mit Platzhalter `{}` für die fortlaufende Nummer (z. B. `REQ-AUTO-{}` → `REQ-AUTO-1`, `REQ-AUTO-2`).
    - Das Format der ID für die Domäne wird projektweit festegelegt.
- **Referenzen:**
    - Eigenes Format: `<<Typ:ID>>` (erzwungen).
    - Referenztypen (Typ): Benutzerdefinierte Schlüsselwörter mit Beschreibung und Kurztext (z. B. `<<depends:REQ-001>>`).
    - zirkuläre Referenzen sind erlaubt.

---

## **2. Metadaten und Inhalte**
### **Pflichtfelder (für Anforderungen und Domänen)**
- ID (automatisch erzeugt, entspricht dem Dateinamen ohne Erweiterung!)
- Kurztext (einzeilig, max 256 Zeichen)

### **Optionale Felder**
- Typ (vom Benutzer pro Projekt definierbare Werte)
- Status (vom Benutzer pro Projekt definierbare Werte)
- Version (vom Benutzer pro Projekt definierbare Werte)
- Priorität (vom Benutzer pro Projekt definierbare Werte)
- Verantwortlicher (vom Benutzer pro Projekt definierbare Werte)
- Erstellungsdatum (wird automatisch in lokalen Datumsformat während der Generiung einer neuen Anforderung in der GUI gesetzt)
- Referenzen (Liste mit Typ, z. B. `<<depends:REQ-001>>`)
- Detaillierte Beschreibung (beliebig langer ASCII Text)
- Labels (pro Domäne definiert, z. B. `backend`, `frontend`, vom Benutzer pro Projekt definierbare Werte)

---

## **3. IntelliJ-Plugin-Features**
### **Anforderungs-Assistent**
- UI zum Anlegen neuer Anforderungen.
    - Der Benutzer kann folgende Felder hier NICHT setzen: Detaillierte Beschreibung
    - Felder die auto-generiert werden, sind vom Benutzer editierbar (ID, Erstellungsdatum)
    - Alles was auf externe Plugins angewiesen ist wie die AsciiDoc-Formatierung (Detaillierte Beschreibung) kann hier nicht gesetzt werden.
- Automatische Vergabe von ID und Dateiname in der UI
- Vorschläge für Referenzen während der Eingabe (Autovervollständigung).
- Sobald der Benutzer OK drückt wird die entsprechende Datei angelegt.

### **Anforderung Editor**
- Es wird das AsciiDoc-Plugin in IntelliJ verwendet.
- Die Command-Completion sollte für alle Felder und Referenzen funktionieren.

### **Tabellarischer Editor**
- Rekursive Anzeige aller Anforderungen in einem Verzeichnis.
- Filter- und Sortierfunktionen (z. B. nach Domäne, Status, Labels).
- Klickbare Referenzen: Öffnet die referenzierte Datei im Editor.
- Die Spalten entsprechen den Feldern.
- Jede Spalte kann sortiert werden.
- Für Spalten in denen vordefinierte Liste von Werten existieren, kann der Benutzer Filtern.
-

### **Echtzeit-Konsistenzprüfung**
- Überprüft Referenzen auf Gültigkeit.
- Markiert Fehler direkt in IntelliJ (z. B. ungültige Referenzen).
- Keine inhaltliche Konsistenzprüfung (nur strukturell).
- Es wird nur die Datei überprüft, die der Benutzer aktuell offen hat.
- Eine explizite Prüfung aller Dateien, erfolgt nur manuell (ähnlich wie beim Build einer Anwendung)

### **Dokumentationsgenerierung**
- **Zwei-Schritt-Prozess:**
    1. Erstellung eines Gesamtdokuments aus allen Dateien (Auflösen von `<<>>`-Referenzen in korrektes Asciidoc-Format)
    2. Generierung in PDF/HTML (unter Nutzung aller AsciiDoc-Features).
- Bei nicht aufgelösten Referenzen wird nur der Referenztyp und die ID dargestellt. Es wird ein Fehlereintrag erstellt.
- Referenzen werden im Dokument nur als Links zu den entsprechenden kapitel dargestellt.
- **Integration:**
    - Eigenständige Funktion in IntelliJ (später auch in CI auslagerbar).
- Am Ende der Generierung wird ein Fehlerlog mit allen Fehlern dem Benutzer angezeigt.
- Vor der Generierung werden dem Benutzer alle Fehler der Konsistenzprüfung angezeigt.
- Die Anforderungen sind alphanumerisch nach den IDs geordnet.
- Falls eine Subdomäne vorhanden ist, wird diese nach den Anforderungen der aktuellen Domäne einsortiert.
- Duplikate werden nicht explizit gelöst, sie erzeugen nur einen Fehlereintrag.
- Performanz ist zweitrangig vor Korrektheit.
- Die hierachische Struktur im Dateisystem, wird auf in der Dokumentengenierung beibehalten.
- Der Benutzer kann die Generierung auf jeder Ebene anstossen.
- Der Benutzer kann auswählen, ob er nur die aktuelle Domäne oder rekursiv alle Sub-Domänen inkludieren will.

### **Git-Integration**
- Vorschlag für Commit-Nachrichten (Auflistung geänderter Dateien und pro Datei welche Felder sich geändert haben).
- Keine eigene Git-Verwaltung (Nutzung der IntelliJ-Funktionen).

### **Darstellung**
- Nutzung des AsciiDoc-Plugins für Rendering und Vorschau.

### **Hilfestellung**
- Tooltips und Dokumentation für alle spezifischen Metadaten.

### **Projekttemplates**
- Vorlagen zum Aufsetzen eines neuen Projekts (z. B. Ordnerstruktur, `domain.adoc`-Vorlagen, etc.).
- Beim Anlegen eines neuen Projekts in IntelliJ kann der Benutzer "Anfoderungsanalyse" auswählen.

### **Migration**
- Import von Jira (z. B. über CSV/JSON) über externe Python-Skripte
- Kein Teil der aktuellen Anforderungen

---

## **4. Referenzen und Labels**
- **Referenztypen:**
    - Benutzer kann Typen anlegen (Schlüsselwort + Beschreibung).
    - Format: `<<typ:ID>>` (z. B. `<<depends:REQ-001>>`).
- **Labels:**
    - Pro Domäne definierbar.
    - Frei wählbar in den optionalen Feldern.

---

## **5. Versionierung**
- Keine eigene Git-Verwaltung.
- Nutzung der IntelliJ-Git-Integration (Commit/Merge/Branching).

## **6. Konfiguration **
- Alle Konfigurationsdaten werden in einer *config.yaml* im YAML-Format abegelgt.
- Es gibt nur eine projekteweite Konfigurationsdatei.
- Die projektweite Konfigurationsdatei liegt im Projektverzeichnis.
- Die Konfigurationsdatei legt folgende Werte fest:
    - Referenztypen: Liste von Objekten (siehe unten
    - Typ: Liste von Strings
    - Status: Liste von Strings
    - Version: Liste von Strings
    - Priorität: Liste von Strings
    - Labels: Liste von Strings
    - Mitglieder: Liste von Strings

### **6.1 Referenztypen**
- Referenztypen bestehen aus:
    - ID: nur alphanumerische Zeichen
    - Kurztext: String (wird für die Dokumentengenerierung benutzt)
    - Beschreibung: String (ausführliche Beschreibung

## **7. Erweiterungen**
- Die Dokumentationsgeneriung und ein Jira-Import sind Erweiterungen und nicht im Fokus eienr initialen Umsetzung.
- Eine einfache Taskvcerwaltung mit IntelliJ-Integration mit einem Scrum-Board sollte mit den beschriebenen Anforderungen möglich sein.
    - Dazu werden alle Anforderungen einer Domäne und aller Sub-domänen in einem Scrum-Borad dargestellt.
        - Der Status entspricht den Swimlanes.
        - Die Karten mit dem Kurztext (beshränkt auf 3 Zeilen, max 80 Zeichen)
        - Der Benutzer kann die Karten von eienr Swimlane in eine andere ziehen, dabei ändert sich der Status.

## **8. Qualitätsanforderungen**
- Das Plugin muss I18N unterstützen. Initiale Sprachen sind Englisch und Deutsch.
- Der Benutzer kann die Sprache in der Konfigurationsdatei festlegen.
- Metadaten und interne Bezeichnungen sind immer in Englisch.
- Fehlermeldungen müssen den Benutzer immer direkt und offensichtlich angezeigt werden.
    - Bei Konsistenzprüfungen werden diese direkt im Asciidoc-Editor angezeigt.
    - Bei der Dokumentationsgenerierung am Ende der Generierung.
- Fehlermeldungen werden in der lokalen Sprache angezeigt.
- Konsistenzprobleme sollte schon durch das Design verhindert werden.
- Es müssen alle Fehlermeldungen und Warnungen gelogged werden.
- Mehrsprachigkeit von Anforderungen wird nicht unterstützt.

---

## **9. Beispiel: Domänenbeschreibung (`domain.adoc`)**
```asciidoc
= Domäne: Automotive-1
:status: Active
:version: 1.0
:beschreibung: Enthält alle Anforderungen für das Automotive-Projekt.
:labels: backend, frontend, hardware
```

---

## **10. Beispiel: Anforderungsdatei (`REQ-AUTO-1.adoc`)**
```asciidoc
= REQ-AUTO-1: Temperatursensor messen
:type: Funktional
:state: Draft
:version: 1.0
:priority: Hoch
:responsible: Team A
:created: 2025-09-14
:references: <<depends:REQ-AUTO-2>>, <<tests:TEST-001>>
:labels: backend, hardware

== Beschreibung
Der Sensor muss alle 100ms den Temperaturwert messen.
```

---

## **11: Beispiel Konfigurationsdatei**
```yaml
referenceypes:
	- id: depends
	  summary: Abhängigkeit
	  description: Längere Beschreibung
	- id: tests
	  summary: Testet
	  description: Längere Beschreibung
	- id: extends
	  summary: Erweitert
	  description: Längere Beschreibung
types: ["Funktional", "Nicht-funktional", "Sicherheit"]
states: ["Draft", "Reviewed", "Approved", "Obsolete"]
versions: ["1.0", "2.0"]
priorities: ["Hoch", "Mittel", "Niedrig"]
labels: ["backend", "frontend", "hardware"]
members: ["Hans Meier", "Robin Marshall"]
id-format: REQ-AUTO-{}
domain-id-format: DOMAIN-{}
```

## **12. Beispiel: Tabellarischer Editor**
| ID           | Kurztext                     | Domäne    | Status | Labels       |
|--------------|------------------------------|-----------|--------|--------------|
| REQ-AUTO-1   | Temperatursensor messen      | Automotive| Draft  | backend      |
| REQ-AUTO-2   | Sensor kalibrieren           | Automotive| Active | hardware     |

---

## **13. Beispiel: Commit-Nachrichtenvorschlag**
```
Updated requirements:
- REQ-AUTO-1: Added acceptance criteria
- REQ-AUTO-2: Changed priority to Hoch
```

## **Technische Umsetzungshinweise für I18N**
1. **Java ResourceBundle:**
   ```java
   ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.forLanguageTag(config.getLanguage()));
   String createTitle = messages.getString("requirement.create.title");
   ```
2. **Dynamische Spracheinstellung:**
    - Sprache wird beim Plugin-Start geladen und kann **ohne Neustart** gewechselt werden (z. B. über Einstellungen).
3. **Fallback auf Englisch:**
    - Falls ein Text in der gewählten Sprache fehlt, wird Englisch verwendet.