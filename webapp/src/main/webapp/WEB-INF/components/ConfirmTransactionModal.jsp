<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<dialog class="relative p-4 rounded-lg text-center w-[45rem]" id="confirm-modal">
    <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-confirm-modal">X</button>
    <div class="flex flex-col items-start divide-y w-full">
        <h1 class="text-xl font-bold pb-3"><spring:message code="buyoffer.txDetails" /></h1>
        <div class="flex flex-col items-start py-3 w-full">
            <span class="text-lg font-semibold pb-1"><spring:message code="buyoffer.from" /></span>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9 pb-1" src="<c:url value='/resources/user.svg'/>" alt="profile_image"/>
                <span id="buyerUsername"></span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9" src="<c:url value='/resources/wallet.svg'/>" alt="wallet_image"/>
                <span id="buyerWallet"></span>
            </div>
        </div>
        <div class="flex flex-col items-start py-3 w-full">
            <span class="text-lg font-semibold pb-1"><spring:message code="buyoffer.to" /></span>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9 pb-1" src="<c:url value='/resources/user.svg'/>" alt="profile_image"/>
                <span id="sellerUsername"></span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9" src="<c:url value='/resources/wallet.svg'/>" alt="wallet_image"/>
                <span id="sellerWallet"></span>
            </div>
        </div>
        <div class="flex flex-col items-start py-3 w-full">
            <span class="text-lg font-semibold pb-2"><spring:message code="buyoffer.nftTransferred" /></span>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9" src="<c:url value='/resources/box.svg'/>" alt="profile_image"/>
                <span id="nftName"></span>#<span id="nftCollectionId"></span>
            </div>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9" src="<c:url value='/resources/chain.svg'/>" alt="wallet_image"/>
                <span id="nftContractAddr"></span>
            </div>
        </div>
        <div class="flex flex-col items-start py-3 w-full">
            <span class="text-lg font-semibold pb-1"><spring:message code="buyoffer.value" /></span>
            <div class="flex flex-row items-center gap-2">
                <img class="h-9 w-9" src="<c:url value='/resources/price.svg'/>" alt="eth_logo">
                <span id="price"> </span>ETH
            </div>
        </div>
        <!-- FIXME: Capaz ver de hacer que esto y mas modales puedan mostrar mensajes de error y bindaerlos a un form:form -->
        <form class="flex flex-col items-start pt-3 mb-0 w-full" action='<c:url value="/buyorder/validate" />' method="post">
            <label for="transactionHash" class="text-lg font-semibold pb-1"><spring:message code="buyoffer.txHash" /></label>
            <div class="flex flex-row items-center gap-2 pb-3 w-full">
                <img class="h-9 w-9" src="<c:url value='/resources/hashtag.svg'/>" alt="eth_logo">
                <input type="hidden" id="productId" name="productId" value=""/>
                <input type="hidden" id="sellOrderId" name="sellOrderId" value=""/>
                <input type="hidden" id="buyerId" name="buyerId" value=""/>
                <input class="w-full p-0.5 rounded"
                       type="text"
                       id="transactionHash"
                       name="transactionHash"
                       minlength="66"
                       maxlength="66"
                       pattern="0x[a-fA-F0-9]{64}"
                       placeholder="Transaction hash"
                       required />
            </div>
            <button class="shadow-md px-5 py-2 ml-auto rounded-md transition duration-300 bg-cyan-600 hover:bg-cyan-800 text-white hover:shadow-xl" type="submit">
                <spring:message code="buyoffer.confirmTx" />
            </button>
        </form>
    </div>
</dialog>

<script defer>
    const confirmModal = document.querySelector("#confirm-modal")
    const closeConfirmModal = document.querySelector("#close-confirm-modal")

    closeConfirmModal.addEventListener("click", () => confirmModal.close())

    function openConfirmBuyOfferModal(buyerUsername, buyerWallet, sellerUsername, sellerWallet, nftName, nftContractAddr, price, nftCollectionId, productId, sellOrderId, buyerId) {
        confirmModal.querySelector("#buyerUsername").innerHTML = buyerUsername
        confirmModal.querySelector("#buyerWallet").innerHTML = buyerWallet
        confirmModal.querySelector("#sellerUsername").innerHTML = sellerUsername
        confirmModal.querySelector("#sellerWallet").innerHTML = sellerWallet
        confirmModal.querySelector("#nftName").innerHTML = nftName
        confirmModal.querySelector("#nftCollectionId").innerHTML = nftCollectionId
        confirmModal.querySelector("#nftContractAddr").innerHTML = nftContractAddr
        confirmModal.querySelector("#price").innerHTML = price
        confirmModal.querySelector("input[name='sellOrderId']").value = sellOrderId
        confirmModal.querySelector("input[name='buyerId']").value = buyerId
        confirmModal.querySelector("input[name='productId']").value = productId
        confirmModal.showModal()
    }
</script>
