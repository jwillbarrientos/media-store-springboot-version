import { myFetch } from "./myfetch.js";

window.editTag = async function(tagId) {
        const newName = prompt("Enter new tag name:");
        if (!newName) return;
        try {
            const response = await myFetch(`/api/tags/${tagId}?name=${encodeURIComponent(newName)}`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: newName })
            });

            console.log("Edit response:", response.status);
            if (response.ok) {
                location.reload();
            } else {
                alert("Error updating tag");
            }
        } catch (err) {
            console.error("Network error while editing:", err);
        }
}