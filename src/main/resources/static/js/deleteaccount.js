import { myFetch } from "./myfetch.js";

document.addEventListener("DOMContentLoaded", () => {
    const deleteAccountBtn = document.getElementById("deleteAccountBtn");

    deleteAccountBtn.addEventListener("click", async() => {
        if (!confirm("Are you sure you want to delete your entire account and all videos/tags? This cannot be undone")) {
            return;
        }

        const clientId = localStorage.getItem("clientId");
        if (!clientId) {
            alert("No client logged in");
            return;
        }

        try {
            const response = await myFetch(`/api/clients/${clientId}`, {
                method: "DELETE"});
            if (response.ok) {
                alert("Your account was deleted successfully");
                window.location.href = "/";
            } else {
                const msg = await response.text();
                alert("Error deleting account: " + msg);
            }
        } catch (err) {
            console.error(err);
            alert("An error occurred while deleting your account");
        }
    });
});