const deleteButton = document.getElementById("delete-btn");

if (deleteButton) {
  deleteButton.addEventListener("click", (event) => {
    let id = document.getElementById("article-id").value;
    const success = () => {
      alert("삭제 완료");
      location.replace("/articles");
    };
    const fail = () => {
      alert("삭제 실패");
      location.replace("/articles");
    };

    httpRequest("DELETE", `/api/articles/${id}`, null, success, fail);
    // fetch(`/api/articles/${id}`, {
    //   method: "DELETE",
    // })
    //   .then(() => {
    //     alert("Delete Complete!");
    //     location.replace("/articles");
    //   })
    //   .catch((err) => {
    //     alert("Error!", err.messages);
    //   });
  });
}

const modityButton = document.getElementById("modify-btn");

if (modityButton) {
  modityButton.addEventListener("click", (event) => {
    let params = new URLSearchParams(location.search);
    let id = params.get("id");

    const body = JSON.stringify({
      title: document.getElementById("title").value,
      content: document.getElementById("content").value,
    });
    const success = () => {
      alert("수정 완료");
      location.replace(`/articles/${id}`);
    };
    const fail = () => {
      alert("삭제 실패");
      location.replace(`/articles/${id}`);
    };

    httpRequest("PUT", `/api/articles/${id}`, body, success, fail);
    //   fetch(`/api/articles/${id}`, {
    //     method: "PUT",
    //     headers: {
    //       "Content-Type": "application/json",
    //     },
    //     body: JSON.stringify({
    //       title: document.getElementById("title").value,
    //       content: document.getElementById("content").value,
    //     }),
    //   }).then(() => {
    //     alert("Update Complete!");
    //     location.replace(`/articles/${id}`);
    //   });
  });
}

const createButton = document.getElementById("create-btn");

if (createButton) {
  createButton.addEventListener("click", (event) => {
    // fetch("/api/articles", {
    //   method: "POST",
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    //   body: JSON.stringify({
    //     title: document.getElementById("title").value,
    //     content: document.getElementById("content").value,
    //   }),
    // }).then(() => {
    //   alert("Write Complete!");
    //   location.replace("/articles");
    // });
    body = JSON.stringify({
      title: document.getElementById("title").value,
      content: document.getElementById("content").value,
    });
    const success = () => {
      alert("등록 완료");
      location.replace("/articles");
    };
    const fail = () => {
      alert("등록 실패");
      location.replace("/articles");
    };

    httpRequest("POST", "/api/articles", body, success, fail);
  });
}

function getCookie(key) {
  let result = null;
  let cookie = document.cookie.split(",");

  cookie.some(function (item) {
    item = item.replace(" ", "");

    let dic = item.split("=");

    if (key === dic[0]) {
      result = dic[1];
      return true;
    }
  });

  return result;
}

function httpRequest(method, url, body, success, fail) {
  fetch(url, {
    method,
    headers: {
      Authorization: "Bearer " + localStorage.getItem("access_token"),
      "Content-Type": "application/json",
    },
    body,
  }).then((response) => {
    if (response.status === 200 || response.status === 201) {
      return success();
    }
    const refresh_token = getCookie("refresh_token");
    if (response.status === 401 && refresh_token) {
      fetch("/api/token", {
        method,
        headers: {
          Authorization: "Bearer " + localStorage.getItem("access_token"),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          refresh_token: getCookie("refresh_token"),
        }),
      })
        .then((res) => {
          if (res.ok) {
            return res.json();
          }
        })
        .then((result) => {
          console.log("result", result);
          localStorage.setItem("access_token", result.accessToken);
          httpRequest(method, url, body, success, fail);
        })
        .catch((err) => fail());
    } else {
      return fail();
    }
  });
}
