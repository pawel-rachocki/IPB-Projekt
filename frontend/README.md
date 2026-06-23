# Frontend — Zarządzanie Wdrożeniami (IPB)

Prosty panel webowy (React + Vite + TypeScript) do przeglądania danych z REST API backendu.
Stanowi dodatek do projektu — kluczowe artefakty to diagramy BPMN / UML / ERD oraz backend.

## Stack

- React 18 + TypeScript
- Vite (dev server + proxy na backend)

## Uruchomienie

Najpierw uruchom backend (`cd backend && mvn spring-boot:run`) — musi działać na `http://localhost:8080`.

Następnie:

```bash
cd frontend
npm install
npm run dev
```

Aplikacja startuje pod adresem wskazanym przez Vite (domyślnie `http://localhost:5173`).
Żądania `/api/*` są przez proxy przekierowywane na backend (`vite.config.ts`), więc nie ma
problemów z CORS.

## Funkcje

- Zakładki dla 7 obiektów domenowych (cykle, zadania, błędy, buildy, release notes, wdrożenia, weryfikacje UAT)
- Tabele z danymi pobieranymi z API
- Filtrowanie po statusie / typie / priorytecie (zależnie od obiektu)
- Prosty formularz dodawania nowego cyklu wydawniczego (POST)

### Interakcja uczestników procesu (na ocenę 5.0)

Aplikacja pokazuje współpracę dwóch ról procesu z reakcją systemu:

- **Zmiana statusu zadania ITS** (Programista → Inżynier Jakości): na zakładce *Zadania ITS*
  kolumna „Akcja" pozwala zmienić status zadania (`PATCH /api/tickets/{id}/status`) — odwzorowuje
  przekazanie pracy z developmentu do QA.
- **Rejestracja weryfikacji UAT** (Klient): na zakładce *Wdrożenia* formularz rejestruje weryfikację
  (`POST /api/deployments/{id}/verifications`). Backend automatycznie ustawia status wdrożenia na
  `ACCEPTED` (gdy wszystkie kryteria spełnione) wraz z datą zatwierdzenia, albo `REJECTED` —
  zmiana jest widoczna w tabeli po odświeżeniu.
