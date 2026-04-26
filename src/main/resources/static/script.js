async function uploadFile() {
    const file = document.getElementById('file-input').files[0];
    const status = document.getElementById('upload-status');

    if (!file) {
        status.textContent = 'Selecione um arquivo primeiro.';
        return;
    }

    status.textContent = 'Enviando...';
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/documents/upload', {
            method: 'POST',
            body: formData
        });
        const text = await response.text();
        status.textContent = response.ok ? '✓ ' + text : '✗ ' + text;
    } catch (e) {
        status.textContent = '✗ Erro ao enviar arquivo.';
    }
}

async function sendMessage() {
    const input = document.getElementById('question-input');
    const messages = document.getElementById('chat-messages');
    const question = input.value.trim();

    if (!question) return;

    addMessage(question, 'user');
    input.value = '';
    addMessage('Digitando...', 'bot');

    try {
        const response = await fetch('/chat', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ question })
        });
        const text = await response.text();
        messages.lastChild.textContent = text;
    } catch (e) {
        messages.lastChild.textContent = 'Erro ao obter resposta.';
    }

    messages.scrollTop = messages.scrollHeight;
}

function addMessage(text, type) {
    const messages = document.getElementById('chat-messages');
    const div = document.createElement('div');
    div.className = 'message ' + type;
    div.textContent = text;
    messages.appendChild(div);
    messages.scrollTop = messages.scrollHeight;
}