document.getElementById('recuperarForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('email').value;

    const resposta = await fetch('/api/recuperar/enviarCodigo',{
        method: 'POST',
        headers: {'Content-Type' : 'application/json'},
        body: JSON.stringify({email})
    });

    const mensagem = await resposta.text();
    alert(mensagem);

});