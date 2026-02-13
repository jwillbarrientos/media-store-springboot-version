export async function myFetch(url, options = {}) {
    //try {
        const response = await fetch(url, {
            credentials: "include",
            ...options
        });

        if (response.status === 401) {
            alert("Your session has expired. Please log in again");
            window.location.href = "/";
            //return;
        }

        if (!response.ok) {
            const msg = await response.text();
            throw new Error(msg || `Request failed: ${response.status}`);
        }

        return response;
    //} catch (err) {
    //    console.error("Fetch error: ", err);
    //    alert("An error occurred. Please try again");
    //    window.location.href = "/index.html";
    //}
}