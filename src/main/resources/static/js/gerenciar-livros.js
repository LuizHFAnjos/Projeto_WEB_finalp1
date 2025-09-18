document.addEventListener("DOMContentLoaded", () => {
  const bookList = document.getElementById("book-list");

  if (bookList) {
    bookList.addEventListener("click", (event) => {
      const deleteButton = event.target.closest(".delete-btn");
      if (!deleteButton) return;

      event.preventDefault();

      
      const deleteUrl = deleteButton.href;

      Swal.fire({
        title: "Você tem certeza?",
        text: "Esta ação não poderá ser revertida!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Sim, deletar!",
        cancelButtonText: "Cancelar",
        reverseButtons: true,
        customClass: {
          confirmButton: "swal-btn swal-btn--danger",
          cancelButton: "swal-btn swal-btn--primary",
        },
      }).then((result) => {
        if (result.isConfirmed) {
          window.location.href = deleteUrl;
        }
      });
    });
  }

 
  const searchInput = document.getElementById("table-search-input");

  if (searchInput) {
    searchInput.addEventListener("input", (event) => {
      const term = event.target.value.trim().toLowerCase();
      const rows = document.querySelectorAll("#book-list tr");

      rows.forEach((row) => {
        const cells = row.cells;
        if (!cells || cells.length < 3) return;
        const title = cells[1].textContent.toLowerCase();
        const author = cells[2].textContent.toLowerCase();
        row.style.display =
          title.includes(term) || author.includes(term) ? "" : "none";
      });
    });
  }
});