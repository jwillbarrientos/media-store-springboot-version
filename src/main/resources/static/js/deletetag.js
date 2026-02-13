import { myFetch } from "./myfetch.js";

window.deleteTag = async function(tagId) {
        if (!confirm("Are you sure you want to delete this tag?")) return;

        try {
            const response = await myFetch(`/api/tags/${tagId}`, {
                method: "DELETE"
            });
            if (response.ok) {
                location.reload();
                return;
            }
            alert("Error deleting tag");
        } catch (err) {
            console.error("Error deleting tag:", err);
        }
}