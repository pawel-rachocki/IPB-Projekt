import { TICKET_STATUS } from "./types";
import { patch } from "./api";

export default function TicketStatusSelect({
  ticketId,
  current,
  onChanged
}: {
  ticketId: string;
  current: string;
  onChanged: () => void;
}) {
  async function change(event: React.ChangeEvent<HTMLSelectElement>) {
    const value = event.target.value;
    try {
      await patch(`/api/tickets/${ticketId}/status?value=${value}`);
      onChanged();
    } catch (err) {
      alert((err as Error).message);
    }
  }

  return (
    <select value={current} onChange={change}>
      {TICKET_STATUS.map((status) => (
        <option key={status} value={status}>
          {status}
        </option>
      ))}
    </select>
  );
}
