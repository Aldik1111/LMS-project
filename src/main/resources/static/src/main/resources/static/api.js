const BASE_URL = window.location.origin + "/api";

function getToken() { return localStorage.getItem("token"); }
function getRole() { return localStorage.getItem("role"); }
function getFullName() { return localStorage.getItem("fullName"); }

function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + getToken()
    };
}

function requireAuth(requiredRole) {
    if (!getToken()) { window.location.href = "login.html"; return false; }
    if (requiredRole && getRole() !== requiredRole) { window.location.href = "dashboard.html"; return false; }
    return true;
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

async function apiGet(path) {
    const res = await fetch(BASE_URL + path, { headers: authHeaders() });
    if (!res.ok) throw new Error(`${res.status}: ${await res.text()}`);
    return res.json();
}

async function apiPost(path, body) {
    const res = await fetch(BASE_URL + path, { method: "POST", headers: authHeaders(), body: JSON.stringify(body) });
    return parseResponse(res);
}

async function apiPut(path, body) {
    const res = await fetch(BASE_URL + path, { method: "PUT", headers: authHeaders(), body: body ? JSON.stringify(body) : null });
    return parseResponse(res);
}

async function apiDelete(path) {
    const res = await fetch(BASE_URL + path, { method: "DELETE", headers: authHeaders() });
    return { ok: res.ok, status: res.status };
}

async function parseResponse(res) {
    const text = await res.text();
    let data = null;
    try { data = text ? JSON.parse(text) : null; } catch (e) {}
    return { ok: res.ok, status: res.status, data, text };
}

async function login(email, password) {
    const res = await fetch(BASE_URL + "/auth/login", {
        method: "POST", headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });
    if (!res.ok) throw new Error("Неверный email или пароль");
    const data = await res.json();
    localStorage.setItem("token", data.token);
    localStorage.setItem("role", data.role);
    localStorage.setItem("userId", data.userId);
    localStorage.setItem("fullName", data.fullName);
    return data;
}

function formatDate(iso) {
    if (!iso) return "—";
    return new Date(iso).toLocaleString("ru-RU", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit" });
}

function escapeHtml(str) {
    if (str == null) return "";
    return String(str).replace(/[&<>"']/g, c => ({ "&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#39;" }[c]));
}