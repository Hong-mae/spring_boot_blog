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

const modityButton = document.getElementById("modify-btn");

if (modityButton) {
  modityButton.addEventListener("click", (event) => {
    let params = new URLSearchParams(location.search);
    let id = params.get("id");

    fetch(`/api/articles/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: document.getElementById("title").value,
        content: document.getElementById("content").value,
      }),
    }).then(() => {
      alert("Update Complete!");
      location.replace(`/articles/${id}`);
    });
  });
}

const createButton = document.getElementById("create-btn");

if (createButton) {
  createButton.addEventListener("click", (event) => {
    fetch("/api/articles", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: document.getElementById("title").value,
        content: document.getElementById("content").value,
      }),
    }).then(() => {
      alert("Write Complete!");
      location.replace("/articles");
    });
  });
}
