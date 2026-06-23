import type { Row } from "./types";

export async function getList(endpoint: string, params: Record<string, string>): Promise<Row[]> {
  const query = new URLSearchParams();
  for (const [key, value] of Object.entries(params)) {
    if (value) query.append(key, value);
  }
  const url = query.toString() ? `${endpoint}?${query}` : endpoint;
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`Blad ${response.status}`);
  }
  return response.json();
}

export async function postJson(endpoint: string, body: unknown): Promise<Row> {
  const response = await fetch(endpoint, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });
  if (!response.ok) {
    throw new Error(`Blad ${response.status}`);
  }
  return response.json();
}

export async function patch(endpoint: string): Promise<Row> {
  const response = await fetch(endpoint, { method: "PATCH" });
  if (!response.ok) {
    throw new Error(`Blad ${response.status}`);
  }
  return response.json();
}
