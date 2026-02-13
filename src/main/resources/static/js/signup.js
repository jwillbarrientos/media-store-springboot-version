function attachSignupHandlers(){
    const signUpBtn = document.getElementById("signUpBtn");
    signUpBtn.addEventListener("click", async () => {
        //const params = new URLSearchParams();
        //params.append("email", document.getElementById("email").value);
        //params.append("password", document.getElementById("password").value)
        const response = await fetch("/public/signup", {
            method: "POST",
            headers: { "Content-type": "application/json" },
            body: JSON.stringify({
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
            })
        });
        console.log("Request sent to: " + response);
        if (response.status === 200) {
            const data = await response.json();
            localStorage.setItem("clientId", data.id);
            window.location.href = '/welcome';
        } else {
            alert('Sign up failed');
            console.log('Sign up failed');
        }
    });
}