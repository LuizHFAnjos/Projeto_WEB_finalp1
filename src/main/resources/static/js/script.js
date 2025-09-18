let menuIcon = document.querySelector("#menu-icon");
let navbar = document.querySelector(".navbar");
let sections = document.querySelectorAll("section");
let navLinks = document.querySelectorAll("header nav a");

if (menuIcon) {
    menuIcon.onclick = () => {
        menuIcon.classList.toggle("bx-x");
        navbar.classList.toggle("active");
    };
}

window.onscroll = () => {
    if (sections.length > 0 && navLinks.length > 0) {
        sections.forEach((sec) => {
            let top = window.scrollY;
            let offset = sec.offsetTop - 150;
            let height = sec.offsetHeight;
            let id = sec.getAttribute("id");

            if (top >= offset && top < offset + height) {
                navLinks.forEach((links) => {
                    links.classList.remove("active");
                    let activeLink = document.querySelector("header nav a[href*=" + id + "]");
                    if (activeLink) {
                        activeLink.classList.add("active");
                    }
                });
            }
        });
    }
};


// --- LÓGICA EXECUTADA APÓS O CARREGAMENTO DA PÁGINA ---
document.addEventListener("DOMContentLoaded", () => {
  
  // --- LÓGICA DO POPUP DE ENVIAR LIVRO ---
  const openPopupBtn = document.getElementById("open-popup-btn");
  const closePopupBtn = document.getElementById("close-popup-btn");
  const popupOverlay = document.getElementById("popup-overlay");
  const livroForm = document.getElementById("livro-form");

  if (openPopupBtn && closePopupBtn && popupOverlay && livroForm) {
    const openPopup = () => popupOverlay.classList.add("active");
    const closePopup = () => popupOverlay.classList.remove("active");

    openPopupBtn.addEventListener("click", (event) => {
      event.preventDefault();
      openPopup();
    });

    closePopupBtn.addEventListener("click", closePopup);

    popupOverlay.addEventListener("click", (event) => {
      if (event.target === popupOverlay) {
        closePopup();
      }
    });

    livroForm.addEventListener("submit", (event) => {
      event.preventDefault();
      closePopup();
      livroForm.reset();
      Swal.fire({
        title: "Enviado com sucesso!",
        text: "Agradecemos sua contribuição. Nossa equipe irá avaliar.",
        icon: "success",
      });
    });
  }


  // --- CÓDIGO DA BUSCA (COM OTIMIZAÇÕES) ---
  const searchInput = document.querySelector('.search-input');
  const searchButton = document.querySelector('.search-btn');
  const resultsContainer = document.getElementById('search-results-container');
  const defaultSections = document.querySelectorAll('.home, .emalta, .recentes, .terror, .romance');
  let debounceTimeout;

  // Função para "desenhar" os cards dos livros na tela
  const displayResults = (books) => {
    resultsContainer.innerHTML = ''; 

    if (!books || books.length === 0) {
        resultsContainer.innerHTML = '<h2 class="heading">Resultados da Busca</h2><p class="no-results">Nenhum livro encontrado.</p>';
        return;
    }

    const booksHtml = books.map(livro => `
        <div class="item">
          <div class="card">
            <div class="card-image-container">
              <img src="${livro.capaUrl || '/assets/img/placeholder.png'}" alt="Capa do livro ${livro.nome}" />
              <button class="like-btn" aria-label="Curtir">
                <svg width="24" height="24" viewBox="0 0 24 24"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" /></svg>
              </button>
            </div>
            <div class="card-content">
              <p class="card-title">${livro.nome}</p>
              <p class="card-author">${livro.autor}</p>
              <button class="details-btn">Detalhes</button>
            </div>
          </div>
        </div>
    `).join('');

    resultsContainer.innerHTML = `
        <h2 class="heading">Resultados da Busca</h2>
        <div class="slider">
            <div class="list">${booksHtml}</div>
        </div>
    `;
  };

  // Função para esconder/mostrar as seções padrão
  const toggleSections = (show) => {
    defaultSections.forEach(section => {
        section.style.display = show ? 'flex' : 'none';
    });
  };

  if (searchInput && searchButton && resultsContainer) {
      const realizarBusca = () => {
          const termo = searchInput.value.trim();

          if (termo.length < 2) {
              resultsContainer.innerHTML = ''; 
              toggleSections(true); 
              return;
          }

          toggleSections(false); 
          resultsContainer.innerHTML = '<div class="loader"></div>';

          fetch(`/api/livros/search?nome=${encodeURIComponent(termo)}`)
              .then(response => response.json())
              .then(data => {
                  displayResults(data);
              })
              .catch(error => {
                console.error('Erro ao buscar:', error);
                resultsContainer.innerHTML = '<p class="no-results">Ocorreu um erro ao buscar. Tente novamente.</p>';
              });
      };
      
      searchInput.addEventListener('input', () => {
          clearTimeout(debounceTimeout);
          debounceTimeout = setTimeout(() => {
              realizarBusca();
          }, 300); 
      });

      searchButton.addEventListener('click', realizarBusca);
  }
  
  // --- LÓGICA PARA INICIALIZAR OS CARROSSÉIS (DINÂMICOS) ---
  function initializeCarousel(slider) {
    const list = slider.querySelector(".list");
    const items = slider.querySelectorAll(".list .item");
    const nextBtn = slider.querySelector(".next-btn");
    const prevBtn = slider.querySelector(".prev-btn");

    if (!list || items.length === 0 || !nextBtn || !prevBtn) {
      return; 
    }

    let active = 0;
    let autoPlayInterval;

    function reloadSlider() {
      if (items.length === 0) return;
      const cardWidth = items[0].offsetWidth;
      const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
      const step = cardWidth + gap;
      const itemsPerScreen = Math.floor(slider.offsetWidth / step);
      const scrollLimit = Math.max(0, items.length - itemsPerScreen);

      if (active > scrollLimit) active = scrollLimit;
      if (active < 0) active = 0;

      let scrollDistance = active * step;
      list.style.transform = `translateX(-${scrollDistance}px)`;

      clearInterval(autoPlayInterval);
      autoPlayInterval = setInterval(() => nextBtn.click(), 5000);
    }

    nextBtn.onclick = function () {
        const cardWidth = items[0].offsetWidth;
        const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
        const step = cardWidth + gap;
        const itemsPerScreen = Math.floor(slider.offsetWidth / step);
        const scrollLimit = Math.max(0, items.length - itemsPerScreen);

        active = (active < scrollLimit) ? active + 1 : 0;
        reloadSlider();
    };

    prevBtn.onclick = function () {
        const cardWidth = items[0].offsetWidth;
        const gap = parseInt(window.getComputedStyle(list).getPropertyValue("gap")) || 0;
        const step = cardWidth + gap;
        const itemsPerScreen = Math.floor(slider.offsetWidth / step);
        const scrollLimit = Math.max(0, items.length - itemsPerScreen);

        active = (active > 0) ? active - 1 : scrollLimit;
        reloadSlider();
    };

    reloadSlider();
    window.addEventListener("resize", reloadSlider);
  }

  const allSliders = document.querySelectorAll(".slider");
  allSliders.forEach((slider) => {
    initializeCarousel(slider);
  });

  // --- LÓGICA DO BOTÃO DE CURTIR ---
  document.body.addEventListener('click', function(event) {
      const likeButton = event.target.closest('.like-btn');
      if (likeButton) {
          likeButton.classList.toggle("active");
          likeButton.classList.add("animating");
          setTimeout(() => {
              likeButton.classList.remove("animating");
          }, 300);
      }
  });

});