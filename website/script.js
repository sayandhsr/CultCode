// 1. Custom Cursor
const cursor = document.querySelector('.cursor');
const cursorFollower = document.querySelector('.cursor-follower');

document.addEventListener('mousemove', (e) => {
    cursor.style.left = e.clientX + 'px';
    cursor.style.top = e.clientY + 'px';
    
    gsap.to(cursorFollower, {
        x: e.clientX,
        y: e.clientY,
        duration: 0.15,
        ease: 'power2.out',
        xPercent: -50,
        yPercent: -50
    });
});

document.addEventListener('mousedown', () => {
    gsap.to(cursor, { scale: 0.5, duration: 0.1 });
    gsap.to(cursorFollower, { scale: 0.5, duration: 0.1 });
});

document.addEventListener('mouseup', () => {
    gsap.to(cursor, { scale: 1, duration: 0.1 });
    gsap.to(cursorFollower, { scale: 1, duration: 0.1 });
});

// 2. Navigation Smooth Scroll
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const targetId = this.getAttribute('href');
        gsap.to(window, {
            duration: 0.8,
            scrollTo: { y: targetId, offsetY: 0 },
            ease: "power4.inOut"
        });
    });
});

// 3. Fast & Smooth GSAP Animations
gsap.registerPlugin(ScrollTrigger);

// Hero load animation
const tl = gsap.timeline();
tl.from('.nav-item', { y: -20, opacity: 0, duration: 0.6, stagger: 0.05, ease: 'power3.out' })
  .from('.huge-title', { y: 40, opacity: 0, duration: 0.8, ease: 'power4.out' }, "-=0.3")
  .from('.hero-footer', { y: 20, opacity: 0, duration: 0.6, ease: 'power3.out' }, "-=0.4");

// Fast panel reveals
gsap.utils.toArray('.panel').forEach((panel, i) => {
    if(i === 0) return;
    gsap.from(panel, {
        scrollTrigger: {
            trigger: panel,
            start: "top 90%",
        },
        y: 40,
        opacity: 0,
        duration: 0.6,
        ease: 'power3.out'
    });
});

// Feature cards staggered reveal
gsap.from('.feature-item', {
    scrollTrigger: {
        trigger: '.features-grid',
        start: "top 85%",
    },
    y: 30,
    opacity: 0,
    duration: 0.5,
    stagger: 0.1,
    ease: 'power2.out'
});

// Language badges staggered reveal
gsap.from('.lang-badge', {
    scrollTrigger: {
        trigger: '.badges-container',
        start: "top 90%",
    },
    y: 20,
    opacity: 0,
    duration: 0.4,
    stagger: 0.05,
    ease: 'power2.out'
});

// 4. Chatbot Logic
const chatToggle = document.getElementById('chatToggle');
const chatWindow = document.getElementById('chatWindow');
const closeChat = document.getElementById('closeChat');
const chatMessages = document.getElementById('chatMessages');
const chatInput = document.getElementById('chatInput');
const sendBtn = document.getElementById('sendBtn');
const quickBtns = document.querySelectorAll('.quick-btn');

chatToggle.addEventListener('click', () => {
    chatWindow.classList.add('active');
});
closeChat.addEventListener('click', () => {
    chatWindow.classList.remove('active');
});

function addMessage(text, sender, isHTML = false) {
    const msgDiv = document.createElement('div');
    msgDiv.classList.add('message', sender);
    if (isHTML) {
        msgDiv.innerHTML = text;
    } else {
        msgDiv.textContent = text;
    }
    chatMessages.appendChild(msgDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function handleBotReply(userText) {
    const text = userText.toLowerCase().trim();
    let reply = "";
    let isHTML = false;

    if (text.includes("time") || text.includes("date") || text.includes("day")) {
        const now = new Date();
        reply = `The current date and time is ${now.toLocaleString()}.`;
    } else if (text.match(/^(hello|hi|hey|greetings|morning|afternoon|evening)/)) {
        reply = "Hello there! How can I help you with Unsullied today?";
    } else if (text.includes("download") || text.includes("get") || text.includes("install") || text.includes("apk")) {
        reply = `You can download the beta version right here: <br><br><a href="CultCode-v6.5-final.apk" download>📥 Download Unsullied APK</a><br><br>Or visit the <a href="#download" onclick="document.querySelector('#download').scrollIntoView({behavior: 'smooth'})">Download Section</a>.`;
        isHTML = true;
    } else if (text.includes("what is") || text.includes("define") || text.includes("about") || text === "what") {
        reply = "Unsullied is an elite, offline-first mobile coding ecosystem built with Kotlin. It brings complex software engineering challenges directly to your mobile device.";
    } else if (text.includes("why") || text.includes("use case") || text.includes("benefit") || text.includes("purpose")) {
        reply = "Use cases include: practicing algorithms offline, evaluating logic with our True AST Engine, and engaging in Code Review Arenas directly on your phone.";
    } else if (text.includes("how does") || text.includes("how to") || text.includes("how it") || text === "how") {
        reply = "Simply download the APK, install it on your Android device, and start coding! The app uses an embedded AST parser to evaluate your code completely offline.";
    } else if (text.includes("price") || text.includes("cost") || text.includes("free")) {
        reply = "The current v6.5 Beta version is completely free to download and use!";
    } else if (text.includes("feature") || text.includes("language") || text.includes("support")) {
        reply = "We support Python, JS, Java, C++, Docker, Kubernetes, SQL, and HTML. Features include offline AST evaluation, 3D motion layer, and a strict dark-mode design system.";
    } else {
        reply = "I'm still learning! You can ask me about what Unsullied is, how to download it, its features, or the current time.";
    }
    
    setTimeout(() => {
        addMessage(reply, 'bot', isHTML);
    }, 300); // Faster response
}

function handleSend() {
    const text = chatInput.value.trim();
    if (text) {
        addMessage(text, 'user');
        chatInput.value = '';
        handleBotReply(text);
    }
}

sendBtn.addEventListener('click', handleSend);
chatInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') handleSend();
});

quickBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const query = btn.getAttribute('data-query');
        addMessage(query, 'user');
        handleBotReply(query);
    });
});
