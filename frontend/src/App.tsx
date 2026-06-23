import { useEffect, useState } from "react";
import { RESOURCES, type Resource } from "./resources";
import { getList } from "./api";
import type { Row } from "./types";
import AddCycleForm from "./AddCycleForm";
import TicketStatusSelect from "./TicketStatusSelect";
import UatVerificationForm from "./UatVerificationForm";

function formatCell(value: unknown): string {
  if (value === null || value === undefined) return "-";
  if (typeof value === "boolean") return value ? "Tak" : "Nie";
  if (typeof value === "string" && value.includes("T") && value.length >= 16) {
    return value.replace("T", " ").slice(0, 16);
  }
  return String(value);
}

export default function App() {
  const [active, setActive] = useState<Resource>(RESOURCES[0]);
  const [filters, setFilters] = useState<Record<string, string>>({});
  const [rows, setRows] = useState<Row[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  function load() {
    setLoading(true);
    setError(null);
    getList(active.endpoint, filters)
      .then((data) => setRows(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    load();
  }, [active, filters]);

  function selectTab(resource: Resource) {
    setActive(resource);
    setFilters({});
  }

  function changeFilter(param: string, value: string) {
    setFilters((prev) => ({ ...prev, [param]: value }));
  }

  return (
    <div className="app">
      <header>
        <h1>Zarządzanie Wdrożeniami</h1>
        <p>Prosty panel przeglądania danych z API (projekt IPB)</p>
      </header>

      <nav className="tabs">
        {RESOURCES.map((resource) => (
          <button
            key={resource.key}
            className={resource.key === active.key ? "tab active" : "tab"}
            onClick={() => selectTab(resource)}
          >
            {resource.label}
          </button>
        ))}
      </nav>

      <main>
        <h2>{active.label}</h2>

        {active.filters.length > 0 && (
          <div className="filters">
            {active.filters.map((filter) => (
              <label key={filter.param}>
                {filter.label}:
                <select
                  value={filters[filter.param] ?? ""}
                  onChange={(event) => changeFilter(filter.param, event.target.value)}
                >
                  <option value="">— wszystkie —</option>
                  {filter.options.map((option) => (
                    <option key={option} value={option}>
                      {option}
                    </option>
                  ))}
                </select>
              </label>
            ))}
          </div>
        )}

        {active.key === "cycles" && <AddCycleForm onCreated={load} />}
        {active.key === "deployments" && <UatVerificationForm deployments={rows} onSaved={load} />}

        {loading && <p>Ładowanie…</p>}
        {error && <p className="error">Błąd: {error} (czy backend działa na :8080?)</p>}

        {!loading && !error && (
          <table>
            <thead>
              <tr>
                {active.columns.map((column) => (
                  <th key={column.key}>{column.label}</th>
                ))}
                {active.key === "tickets" && <th>Akcja (zmiana statusu)</th>}
              </tr>
            </thead>
            <tbody>
              {rows.length === 0 && (
                <tr>
                  <td colSpan={active.columns.length + (active.key === "tickets" ? 1 : 0)}>
                    Brak danych
                  </td>
                </tr>
              )}
              {rows.map((row, index) => (
                <tr key={index}>
                  {active.columns.map((column) => (
                    <td key={column.key}>{formatCell(row[column.key])}</td>
                  ))}
                  {active.key === "tickets" && (
                    <td>
                      <TicketStatusSelect
                        ticketId={String(row.ticketId)}
                        current={String(row.status)}
                        onChanged={load}
                      />
                    </td>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </main>
    </div>
  );
}
