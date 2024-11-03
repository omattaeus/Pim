document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('loginForm').addEventListener('submit', async function (event) {
        event.preventDefault(); // Impedir o recarregamento da página

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
                console.log("Login failed:", response.status);
                alert('Login falhou: ' + (await response.text()));
            }
        } catch (error) {
            console.error('Erro ao fazer login:', error);
            alert('Ocorreu um erro ao tentar fazer login.');
        }
    });
});