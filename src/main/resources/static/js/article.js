const deleteButton = document.getElementById("delete-btn");

if (deleteButton) {
  deleteButton.addEventListener("click", (event) => {
    let id = document.getElementById("article-id").value;
    fetch(`/api/articles/${id}`, {
      method: "DELETE",
    })
      .then(() => {
        alert("Delete Complete!");
        location.replace("/articles");
      })
      .catch((err) => {
        alert("Error!", err.messages);
      });
  });
}
