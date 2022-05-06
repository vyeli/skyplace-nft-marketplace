const tabs = {
    inventory: document.getElementById('inventoryTab'),
    selling: document.getElementById('sellingTab'),
    favorited: document.getElementById('favoritedTab'),
    history: document.getElementById('historyTab'),
}

window.onload = function setDefaultTab() {
    const params = new URLSearchParams(window.location.search)
    const currentTab = params.get('tab') || "inventory"
    Object.entries(tabs).forEach(([tab, elem]) => tab === currentTab ? setActiveTab(tabs[currentTab]) : setInactiveTab(elem))
}

function setActiveTab(tab){
    tab.classList.add('text-cyan-600', 'border-cyan-600', 'active')
}

function setInactiveTab(tab){
    tab.classList.add('border-transparent', 'hover:text-gray-600')
}

function copyToClipboard(){
    // Copy text
    const walletAddress = document.getElementById("walletId").textContent;
    if (navigator.clipboard && window.isSecureContext) {
        // navigator clipboard api method'
        navigator.clipboard.writeText(walletAddress);
        const tooltipText = document.getElementById("tooltip-dark");
        tooltipText.firstChild.data = "Copied to clipboard!";
        setTimeout(() => tooltipText.firstChild.data = 'Copy', 1000);
    } else {
        // text area method
        let textArea = document.createElement("textarea");
        textArea.value = walletAddress;
        // make the textarea out of viewport
        textArea.style.position = "fixed";
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        new Promise((res, rej) => {
            // here the magic happens
            document.execCommand('copy') ? res() : rej();
            const tooltipText = document.getElementById("tooltip-dark");
            tooltipText.firstChild.data = "Copied to clipboard!";
            setTimeout(() => tooltipText.firstChild.data = 'Copy', 1000);
            textArea.remove();
        });
    }
}