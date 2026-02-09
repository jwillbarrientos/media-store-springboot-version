import { myFetch } from "./myfetch.js";

document.addEventListener("DOMContentLoaded", async () => {
    try {
        const response = await myFetch("/api/getprofilename");
        if (!response.ok) {
            throw new Error("Failed to get profile name");
        }
        if (response.status === 200) {
            const client = await response.json();
            document.getElementById("userName").textContent = client.email;
        }
    } catch (err) {
        console.error("Error getting name:", err);
    }
});