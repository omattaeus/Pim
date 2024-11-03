document.addEventListener('DOMContentLoaded', function () {
    const submitButton = document.getElementById('submitButton');
    const loadingIcon = document.getElementById('loadingIcon');
    const buttonText = document.getElementById('buttonText');
    const errorMessage = document.createElement('p');  // Para mensagem de erro
    errorMessage.className = 'text-red-500 mt-4';  // Estilo para o texto de erro
    errorMessage.style.display = 'none';  // Inicialmente escondido
    document.getElementById('loginForm').appendChild(errorMessage);  // Adiciona ao formulário

    document.getElementById('loginForm').addEventListener('submit', async function (event) {
        event.preventDefault(); // Impedir o recarregamento da página

        // Exibir o ícone de carregamento, desativar o botão e esconder mensagem de erro anterior
        buttonText.classList.add('hidden');
        loadingIcon.classList.remove('hidden');
        submitButton.disabled = true;
        errorMessage.style.display = 'none';

        // Obter valores dos campos de entrada
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            // Fazer requisição ao endpoint de login no ambiente de produção
            const response = await fetch('https://produto-gaa2a9gfbvenbaaf.brazilsouth-01.azurewebsites.net/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                // Login bem-sucedido, obter o token
                const token = await response.text();

                // Armazenar o token no localStorage (ou sessionStorage)
                localStorage.setItem('jwtToken', token);

                // Redirecionar para a página menu-inicial.html
                window.location.href = 'pages/menu-inicial.html';
            } else {
                // Exibir mensagem de erro e voltar ao estado normal
                const errorText = await response.text();
                errorMessage.textContent = `Login falhou: ${errorText}`;
                errorMessage.style.display = 'block';
            }
        } catch (error) {
            console.error('Erro ao fazer login:', error);
            errorMessage.textContent = 'Ocorreu um erro ao tentar fazer login.';
            errorMessage.style.display = 'block';
        } finally {
            // Assegurar que o ícone de carregamento desapareça e o botão seja reativado
            buttonText.classList.remove('hidden');
            loadingIcon.classList.add('hidden');
            submitButton.disabled = false;
        }
    });
});