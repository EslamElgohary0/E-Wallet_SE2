const API_BASE = 'http://localhost:8080';

function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
}

function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

function logout() {
    localStorage.clear();
    window.location.href = 'index.html';
}

function showToast(message, type = 'error') {
    const colors = {
        success: 'bg-green-500',
        error: 'bg-red-500',
        warning: 'bg-yellow-500',
        info: 'bg-blue-500'
    };
    const icons = {
        success: 'fa-check-circle',
        error: 'fa-times-circle',
        warning: 'fa-exclamation-triangle',
        info: 'fa-info-circle'
    };
    
    const toast = document.createElement('div');
    toast.className = `fixed top-5 left-5 right-5 z-[9999] ${colors[type]} text-white px-6 py-4 rounded-xl shadow-2xl flex items-center justify-between animate-slide-down`;
    toast.innerHTML = `
        <div class="flex items-center gap-3">
            <i class="fas ${icons[type]} text-xl"></i>
            <span class="font-medium">${message}</span>
        </div>
        <button onclick="this.parentElement.remove()" class="text-white hover:text-gray-200 transition">
            <i class="fas fa-times"></i>
        </button>
    `;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ar-EG');
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ar-EG', { 
        style: 'currency', 
        currency: 'EGP' 
    }).format(amount);
}

const style = document.createElement('style');
style.textContent = `
    @keyframes slide-down {
        from { transform: translateY(-100px); opacity: 0; }
        to { transform: translateY(0); opacity: 1; }
    }
    .animate-slide-down { animation: slide-down 0.3s ease; }
`;
document.head.appendChild(style);