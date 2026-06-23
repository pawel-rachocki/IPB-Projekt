import {
  CYCLE_STATUS,
  TICKET_STATUS,
  TICKET_TYPE,
  BUG_PRIORITY,
  BUG_SOURCE,
  BUG_STATUS,
  DEPLOYMENT_STATUS,
  UAT_STATUS,
  RN_VIEW_TYPE
} from "./types";

export type Column = { key: string; label: string };
export type Filter = { param: string; label: string; options: string[] };

export type Resource = {
  key: string;
  label: string;
  endpoint: string;
  columns: Column[];
  filters: Filter[];
};

export const RESOURCES: Resource[] = [
  {
    key: "cycles",
    label: "Cykle wydawnicze",
    endpoint: "/api/release-cycles",
    columns: [
      { key: "cycleId", label: "ID" },
      { key: "targetVersionTag", label: "Wersja" },
      { key: "status", label: "Status" },
      { key: "startDate", label: "Start" },
      { key: "plannedDeploymentWindow", label: "Okno wdrozenia" }
    ],
    filters: [{ param: "status", label: "Status", options: CYCLE_STATUS }]
  },
  {
    key: "tickets",
    label: "Zadania ITS",
    endpoint: "/api/tickets",
    columns: [
      { key: "ticketId", label: "ID" },
      { key: "title", label: "Tytul" },
      { key: "type", label: "Typ" },
      { key: "status", label: "Status" },
      { key: "cycleId", label: "Cykl" },
      { key: "assigneeId", label: "Wykonawca" }
    ],
    filters: [
      { param: "status", label: "Status", options: TICKET_STATUS },
      { param: "type", label: "Typ", options: TICKET_TYPE }
    ]
  },
  {
    key: "bugs",
    label: "Bledy / rework",
    endpoint: "/api/bugs",
    columns: [
      { key: "bugId", label: "ID" },
      { key: "parentTicketId", label: "Zadanie" },
      { key: "priority", label: "Priorytet" },
      { key: "source", label: "Zrodlo" },
      { key: "status", label: "Status" },
      { key: "defectDescription", label: "Opis" }
    ],
    filters: [
      { param: "priority", label: "Priorytet", options: BUG_PRIORITY },
      { param: "source", label: "Zrodlo", options: BUG_SOURCE },
      { param: "status", label: "Status", options: BUG_STATUS }
    ]
  },
  {
    key: "builds",
    label: "Buildy",
    endpoint: "/api/builds",
    columns: [
      { key: "buildId", label: "ID" },
      { key: "cycleId", label: "Cykl" },
      { key: "commitHash", label: "Commit" },
      { key: "compilationDate", label: "Data kompilacji" },
      { key: "staticAnalysisPassed", label: "Analiza statyczna" }
    ],
    filters: []
  },
  {
    key: "releaseNotes",
    label: "Release Notes",
    endpoint: "/api/release-notes",
    columns: [
      { key: "rnId", label: "ID" },
      { key: "buildId", label: "Build" },
      { key: "viewType", label: "Widok" },
      { key: "lastUpdatedDate", label: "Aktualizacja" },
      { key: "textContent", label: "Tresc" }
    ],
    filters: [{ param: "viewType", label: "Widok", options: RN_VIEW_TYPE }]
  },
  {
    key: "deployments",
    label: "Wdrozenia",
    endpoint: "/api/deployments",
    columns: [
      { key: "deploymentId", label: "ID" },
      { key: "cycleId", label: "Cykl" },
      { key: "uatStatus", label: "Status UAT" },
      { key: "deploymentStatus", label: "Status wdrozenia" },
      { key: "approvedProdDate", label: "Zatwierdzono PROD" },
      { key: "uatEnvironmentUrl", label: "URL UAT" }
    ],
    filters: [
      { param: "uatStatus", label: "Status UAT", options: UAT_STATUS },
      { param: "deploymentStatus", label: "Status wdrozenia", options: DEPLOYMENT_STATUS }
    ]
  },
  {
    key: "verifications",
    label: "Weryfikacje UAT",
    endpoint: "/api/uat-verifications",
    columns: [
      { key: "verificationId", label: "ID" },
      { key: "deploymentId", label: "Wdrozenie" },
      { key: "requirementsMet", label: "Wymagania" },
      { key: "noOperationalImpact", label: "Brak wplywu" },
      { key: "securityAuditPassed", label: "Audyt bezp." },
      { key: "verificationDate", label: "Data" }
    ],
    filters: []
  }
];
