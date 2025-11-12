document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".service-form");
  if (!form) return;

  form.addEventListener("submit", (e) => {
    const date = form.querySelector("input[name='date']");
    const name = form.querySelector("input[name='customerName']");
    if (!date.value || !name.value.trim()) {
      e.preventDefault();
      alert("Please fill in required fields: Date and Customer Name");
    }
  });
});
