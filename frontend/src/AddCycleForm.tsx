import { useState } from "react";
import { postJson } from "./api";

export default function AddCycleForm({ onCreated }: { onCreated: () => void }) {
  const [version, setVersion] = useState("");
  const [error, setError] = useState<string | null>(null);

  async function submit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);
    try {
      await postJson("/api/release-cycles", { targetVersionTag: version });
      setVersion("");
      onCreated();
    } catch (err) {
      setError((err as Error).message);
    }
  }

  return (
    <form className="add-form" onSubmit={submit}>
      <input
        type="text"
        placeholder="np. v1.6.0"
        value={version}
        onChange={(event) => setVersion(event.target.value)}
        required
      />
      <button type="submit">Dodaj cykl</button>
      {error && <span className="error">{error}</span>}
    </form>
  );
}
