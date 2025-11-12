document.addEventListener("DOMContentLoaded", async () => {
  const calendarEl = document.getElementById("calendar");
  const modal = document.getElementById("eventModal");
  const modalBody = document.getElementById("modalBody");
  const modalDate = document.getElementById("modalDate");
  const closeModal = document.getElementById("closeModal");

  // Fetch records
  const response = await fetch("/api/records");
  const records = await response.json();

  const grouped = {};
  for (const r of records) {
    if (!r.date) continue;
    if (!grouped[r.date]) grouped[r.date] = [];
    grouped[r.date].push(r);
  }

  // Build simple monthly calendar (current month)
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  for (let d = 1; d <= daysInMonth; d++) {
    const dateStr = `${year}-${String(month + 1).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
    const cell = document.createElement("div");
    cell.className = "day";
    if (grouped[dateStr]) cell.classList.add("has-event");
    cell.textContent = d;
    cell.addEventListener("click", () => showEvents(dateStr));
    calendarEl.appendChild(cell);
  }

  function showEvents(date) {
    modalBody.innerHTML = "";
    modalDate.textContent = date;
    const events = grouped[date] || [];
    if (events.length === 0) {
      modalBody.innerHTML = "<p>No records for this date.</p>";
    } else {
      for (const e of events) {
        const div = document.createElement("div");
        div.innerHTML = `
          <p><strong>${e.customerName}</strong> (${e.documentType})</p>
          <p>${e.vehicleRegNumber} — ${e.requirementCategory}</p>
          <p>Amount: ${e.amount || 0}</p>
          <hr/>
        `;
        modalBody.appendChild(div);
      }
    }
    modal.classList.remove("hidden");
  }

  closeModal.addEventListener("click", () => modal.classList.add("hidden"));
});
