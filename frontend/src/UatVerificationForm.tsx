import { useState } from "react";
import { postJson } from "./api";
import type { Row } from "./types";

export default function UatVerificationForm({
  deployments,
  onSaved
}: {
  deployments: Row[];
  onSaved: () => void;
}) {
  const [deploymentId, setDeploymentId] = useState("");
  const [requirementsMet, setRequirementsMet] = useState(true);
  const [noOperationalImpact, setNoOperationalImpact] = useState(true);
  const [securityAuditPassed, setSecurityAuditPassed] = useState(true);
  const [justification, setJustification] = useState("");
  const [message, setMessage] = useState<string | null>(null);

  async function submit(event: React.FormEvent) {
    event.preventDefault();
    setMessage(null);
    try {
      await postJson(`/api/deployments/${deploymentId}/verifications`, {
        requirementsMet,
        noOperationalImpact,
        securityAuditPassed,
        rejectionJustification: justification || null
      });
      const accepted = requirementsMet && noOperationalImpact && securityAuditPassed;
      setMessage(`Zapisano. Wdrożenie otrzymało status UAT: ${accepted ? "ACCEPTED" : "REJECTED"}.`);
      onSaved();
    } catch (err) {
      setMessage((err as Error).message);
    }
  }

  return (
    <form className="uat-form" onSubmit={submit}>
      <strong>Rejestracja weryfikacji UAT (Klient)</strong>
      <label>
        Wdrożenie:
        <select value={deploymentId} onChange={(e) => setDeploymentId(e.target.value)} required>
          <option value="">— wybierz —</option>
          {deployments.map((d) => (
            <option key={String(d.deploymentId)} value={String(d.deploymentId)}>
              #{String(d.deploymentId)} (cykl {String(d.cycleId)})
            </option>
          ))}
        </select>
      </label>
      <label>
        <input
          type="checkbox"
          checked={requirementsMet}
          onChange={(e) => setRequirementsMet(e.target.checked)}
        />
        Wymagania spełnione
      </label>
      <label>
        <input
          type="checkbox"
          checked={noOperationalImpact}
          onChange={(e) => setNoOperationalImpact(e.target.checked)}
        />
        Brak wpływu na działanie
      </label>
      <label>
        <input
          type="checkbox"
          checked={securityAuditPassed}
          onChange={(e) => setSecurityAuditPassed(e.target.checked)}
        />
        Audyt bezpieczeństwa OK
      </label>
      <input
        type="text"
        placeholder="Uzasadnienie odrzucenia (opcjonalne)"
        value={justification}
        onChange={(e) => setJustification(e.target.value)}
      />
      <button type="submit">Zapisz weryfikację</button>
      {message && <span className="info">{message}</span>}
    </form>
  );
}
