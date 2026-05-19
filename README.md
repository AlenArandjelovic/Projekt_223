Multi User Messaging System – README
Projektbeschreibung
Das Multi User Messaging System ist eine webbasierte Chat‑Anwendung, in der Benutzer in öffentlichen Gruppen miteinander kommunizieren können.
 User können Gruppen beitreten, Nachrichten senden und zwischen Chats wechseln.
 Das System basiert auf einem Spring‑Boot‑Backend und einem React‑Frontend.



Features
Öffentliche Gruppen anzeigen & beitreten
Nachrichten senden & empfangen
Gruppen erstellen
Rollenlogik: User, Member, Owner
JWT‑Login
Automatisierte Tests (Backend + Frontend)



Systemrollen
User: Gruppen sehen, Join‑Request senden
Member: Nachrichten schreiben & lesen
Owner: Join‑Requests annehmen/ablehnen, Mitglieder entfernen
Admin: (geplant) Gruppen löschen



Tech‑Stack
Backend
Spring Boot
Spring Security + JWT
JPA / Hibernate
MySQL
Frontend
React (Vite)
Axios
React Router
Vitest + Testing Library

📁 Projektstruktur
Backend
src/
 ├─ controller/
 ├─ model/
 ├─ repository/
 ├─ security/
 └─ dto/

Frontend
src/
 ├─ pages/
 ├─ api/
 ├─ components/
 └─ index.css


Authentifizierung
Login → Backend erstellt JWT
Frontend speichert Token in localStorage
Axios‑Interceptor setzt Authorization: Bearer <token>
Backend prüft Token & Rollen



Tests
Backend
Test 1: Join‑Request wird gespeichert
Test 2: Nachricht wird persistiert
Frontend
Rendering‑Test der Gruppenliste
Nachricht erscheint nach Eingabe



Installation & Start
Backend starten
mvn spring-boot:run

Backend läuft auf:
 http://localhost:8080
Frontend starten
npm install
npm start

Frontend läuft auf:
 http://localhost:3000
.env Beispiel
REACT_APP_API_URL=http://localhost:8080



Deployment
Backend Build
mvn clean package
java -jar target/app.jar

Frontend Build
npm run build

Build‑Ordner kann über Nginx/Apache oder Spring Boot Static Folder ausgeliefert werden.



Bekannte Probleme
Admin‑Rolle nicht vollständig implementiert
Kein WebSocket‑Live‑Chat (Polling statt Echtzeit)
Minimalistisches UI

