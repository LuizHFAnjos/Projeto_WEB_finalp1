// Seleciona os elementos do DOM
const loginForm = document.querySelector(".login-wrap");
const signupForm = document.querySelector(".signup-wrap");
const title = document.querySelector("title");
const signupToggleBtn = document.querySelector("#toggle-signup");
const loginToggleBtn = document.querySelector("#toggle-login");
const signupFormElement = document.querySelector(".signup-wrap form");

// Função para alternar para a tela de Cadastro
signupToggleBtn.onclick = () => {
    loginForm.classList.remove("active");
    signupForm.classList.add("active");
    title.textContent = "Cadastrar";
};

// Função para alternar para a tela de Login
loginToggleBtn.onclick = () => {
    signupForm.classList.remove("active");
    loginForm.classList.add("active");
    title.textContent = "Entrar";
};

// =========================================================================
// LÓGICA DE CADASTRO
// =========================================================================
// Adiciona um evento de 'submit' ao formulário de cadastro.
signupFormElement.addEventListener("submit", async (e) => {
    // Previne o comportamento padrão do formulário (que recarregaria a página)
    e.preventDefault(); 

    // Pega os valores dos campos de input
    const nome = document.querySelector("#nome-cadastro").value;
    const email = document.querySelector("#email-cadastro").value;
    const senha = document.querySelector("#password-cadastro").value;

    try {
        // Faz a requisição POST para a API de cadastro
        const response = await fetch("/api/cadastro", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email, senha }),
        });

        // Se o email já existe (status 409 - Conflict)
        if (response.status === 409) {
            alert("Este e-mail já está cadastrado!");
            return;
        }

        // Se a resposta não for bem-sucedida, lança um erro
        if (!response.ok) {
            throw new Error("Ocorreu um erro no cadastro. Tente novamente.");
        }
        
        // Se o cadastro foi bem-sucedido
        alert("Cadastro realizado com sucesso! Faça o login para continuar.");
        
        // Simula um clique no botão para voltar para a tela de login
        loginToggleBtn.click();
        
    } catch (err) {
        // Exibe qualquer erro que ocorrer durante o processo
        alert(err.message);
    }
});

// =========================================================================
// OBSERVAÇÃO SOBRE O LOGIN
// =========================================================================
// Não há um 'fetch' para o formulário de login aqui.
// Isso é intencional e correto, pois o login é gerenciado pelo
// Spring Security através de um envio de formulário padrão para o
// endpoint '/perform_login', conforme definido no seu HTML e na sua
// configuração de segurança.
// =========================================================================