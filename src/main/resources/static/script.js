// API endpoint
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users/list`
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

// Fetch users from backend
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const users = await response.json();
        renderUserList(users.data);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

// Render user list
function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = ''; // Clear existing content

    users.forEach(user => {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        // 프로필 이미지 생성
        let profileUrl = '/default-avatar.png'; // 기본 이미지
        if (user.profileImg && user.profileImg.data && user.profileImg.fileType) {
            profileUrl = `data:${user.profileImg.fileType};base64,${user.profileImg.data}`;
        }

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.nickname}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.nickname}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    });
}
