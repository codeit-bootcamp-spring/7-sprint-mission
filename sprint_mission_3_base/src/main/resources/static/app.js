const API_BASE = "http://localhost:8080/api";
const FIXED_USER_ID = "00000000-0000-0000-0000-000000000001";

let currentChannelId = null;

window.addEventListener("DOMContentLoaded", () => {
    loadChannels();
});

/* ============================================================
   1) 채널 목록 불러오기
   ============================================================ */
async function loadChannels() {
    try {
        const res = await fetch(`${API_BASE}/channels`);
        const channels = await res.json();

        const channelList = document.querySelector(".channel-list");
        channelList.innerHTML = "";

        channels.forEach(ch => {
            const item = document.createElement("div");
            item.classList.add("channel-item");
            item.textContent = ch.name;

            item.addEventListener("click", () => {
                selectChannel(ch.id);
            });

            channelList.appendChild(item);
        });
    } catch (e) {
        console.error("채널을 불러오는 중 오류:", e);
    }
}

/* ============================================================
   2) 채널 선택 → 메시지 불러오기
   ============================================================ */
async function selectChannel(channelId) {
    currentChannelId = channelId;
    loadMessages(channelId);
}

async function loadMessages(channelId) {
    try {
        const res = await fetch(`${API_BASE}/messages/channel/${channelId}?page=0&size=50`);
        const page = await res.json();
        const messages = page.content;

        const msgArea = document.querySelector(".messages");
        msgArea.innerHTML = "";

        // ❗ reverse() 제거 (정렬 꼬임 방지)
        messages.forEach(msg => {
            const item = document.createElement("div");
            item.classList.add("message-item");

            // 텍스트 출력
            if (msg.content) {
                const textNode = document.createElement("div");
                textNode.textContent = msg.content;
                item.appendChild(textNode);
            }

            // 이미지 출력
            if (msg.binaryContentId) {
                const img = document.createElement("img");

                // ❗ URL 수정
                img.src = `${API_BASE}/binaryContents/${msg.binaryContentId}/download`;

                img.style.maxWidth = "260px";
                img.style.borderRadius = "6px";
                img.style.marginTop = "6px";
                item.appendChild(img);
            }

            msgArea.appendChild(item);
        });

        // 최신 메시지까지 자동 스크롤
        msgArea.scrollTop = msgArea.scrollHeight;

    } catch (e) {
        console.error("메시지 로딩 실패:", e);
    }
}

/* ============================================================
   3) 메시지 전송 (텍스트 + 이미지 업로드)
   ============================================================ */
async function sendMessage() {
    if (!currentChannelId) {
        alert("채널을 먼저 선택하세요!");
        return;
    }

    const input = document.getElementById("message-input");
    const fileInput = document.getElementById("file-input");

    const content = input.value.trim();
    const file = fileInput.files[0];

    const form = new FormData();

    form.append("json", new Blob([JSON.stringify({
        channelId: currentChannelId,
        authorId: FIXED_USER_ID,
        content: content
    })], { type: "application/json" }));

    if (file) form.append("file", file);

    try {
        await fetch(`${API_BASE}/messages`, {
            method: "POST",
            body: form
        });

        input.value = "";
        fileInput.value = "";

        refreshLatestMessages();

    } catch (e) {
        console.error("메시지 전송 실패:", e);
        alert("메시지 전송 중 오류 발생!");
    }
}

/* ============================================================
   4) 자동 새로고침 (전송 후 최신 메시지만 갱신)
   ============================================================ */
async function refreshLatestMessages() {
    if (!currentChannelId) return;
    await loadMessages(currentChannelId);
}
