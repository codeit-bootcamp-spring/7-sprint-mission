async function loadUsers() {
    try {
        const response = await fetch("/api/users/findAll");
        if (!response.ok) throw new Error("HTTP error! status: " + response.status);

        const users = await response.json();
        const container = document.getElementById("user-list");
        container.innerHTML = "";

        users.forEach(user => {
            const card = document.createElement("div");
            card.classList.add("user-card");

            const info = document.createElement("div");
            info.classList.add("user-info");

            const img = document.createElement("img");
            img.classList.add("avatar");

            if (user.profileId) {
                // 서버 BinaryContent에서 이미지 불러오기
                img.src = `/api/binaryContent/find?binaryContentId=${user.profileId}`;
            } else {
                // ✅ img 폴더의 avatar1~avatar4 중 랜덤 선택
                const randomIndex = Math.floor(Math.random() * 4) + 1;
                img.src = `/img/avatar${randomIndex}.png`;
            }

            const text = document.createElement("div");
            text.classList.add("user-text");

            const name = document.createElement("div");
            name.classList.add("username");
            name.textContent = user.username;

            const email = document.createElement("div");
            email.classList.add("email");
            email.textContent = user.email;

            text.appendChild(name);
            text.appendChild(email);
            info.appendChild(img);
            info.appendChild(text);

            const status = document.createElement("button");
            status.classList.add("status");
            status.textContent = user.online ? "온라인" : "오프라인";

            card.appendChild(info);
            card.appendChild(status);
            container.appendChild(card);
        });

    } catch (error) {
        console.error("❌ 사용자 목록 불러오기 실패:", error);
    }
}

document.addEventListener("DOMContentLoaded", loadUsers);
