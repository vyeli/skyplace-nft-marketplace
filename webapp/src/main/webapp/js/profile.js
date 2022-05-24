function copyToClipboard(){
    const walletAddress = document.getElementById("walletId").textContent;
    const tooltipCopyMessage = document.getElementById("message-copy");
    const tooltipCopiedMessage = document.getElementById("message-copied");
    if (navigator.clipboard && window.isSecureContext) {
        navigator.clipboard.writeText(walletAddress);
        toggleTextOnTooltip(tooltipCopyMessage, tooltipCopiedMessage);
    } else {
        const walletAddressButton = document.getElementById("wallet-address-button");
        let textArea = document.createElement("textarea");
        textArea.value = walletAddress;
        textArea.style.position = "fixed";
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        new Promise((res, rej) => {
            document.execCommand('copy') ? res() : rej();
            walletAddressButton.focus();
            toggleTextOnTooltip(tooltipCopyMessage, tooltipCopiedMessage);
        });
    }
}

function toggleTextOnTooltip(tooltipCopyMessage, tooltipCopiedMessage){
    const hideClass = "hidden";
    tooltipCopyMessage.classList.add(hideClass);
    tooltipCopiedMessage.classList.remove(hideClass);
    setTimeout(() => {
        tooltipCopiedMessage.classList.add(hideClass);
        tooltipCopyMessage.classList.remove(hideClass);
    }, 1000);
}