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