const BASE_URL = window.location.origin + "/api";

async function login() {

    const email =
        document.getElementById("email").value;

    const password =
        document.getElementById("password").value;

    try {

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

        if (!response.ok) {
            alert("Неверный email или пароль");
            return;
        }

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

        window.location.href = "dashboard.html";

    } catch (error) {

        console.error(error);

        alert("Не удалось подключиться к серверу");
    }
}