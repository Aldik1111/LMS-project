const BASE_URL = "http://localhost:8080/api";

async function login() {

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    const response = await fetch(
        BASE_URL + "/auth/login",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email,
                password
            })
        }
    );

    const data = await response.json();

    console.log(data);

    localStorage.setItem(
        "token",
        data.token
    );

    localStorage.setItem(
        "role",
        data.role
    );

    if (data.role === "ADMIN")
        window.location.href = "admin.html";

    if (data.role === "MANAGER")
        window.location.href = "manager.html";

    if (data.role === "STUDENT")
        window.location.href = "student.html";
}