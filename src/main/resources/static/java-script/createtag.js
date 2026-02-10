import { myFetch } from "./myfetch.js";

document.addEventListener("DOMContentLoaded", () => {
    const createTagBtn = document.getElementById("createTag");
    createTagBtn.addEventListener("click", async () => {
        // Ask for the tag name
        const tagName = prompt("Enter a name for the new tag:");
        if (!tagName) return;
        try {
            const response = await myFetch("/api/tags", {
                method: "POST",
                headers: { "Content-Type": "application/json"},
                body: JSON.stringify({name: tagName})
            });
            if (response.status === 200) {
                location.reload();
                return;
            }

            if (response.status === 401) {
                return;
            }

            alert("Tag already exists");
            console.error("Tag already exists");
        } catch (err) {
            console.error("Error creating tag:", err);
        }
    });
});