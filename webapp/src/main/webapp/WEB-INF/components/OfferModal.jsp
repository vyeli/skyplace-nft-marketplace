<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Confirm Sell Modal -->
<dialog class="relative p-4 rounded-lg text-center max-w-md" id="confirm-offer-modal">
    <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-confirm-offer-modal">X</button>
    <h2 class="font-bold text-xl text-cyan-700"><spring:message code="offerModal.acceptOffer"/></h2>
    <p class="py-6 text-slate-600"><spring:message code="offerModal.confirmAccept"/></p>

    <c:url value="/buyorder/accept" var="confirmBuyOrder" />
    <form action="${confirmBuyOrder}" method="post" class="mb-0">
        <input type="hidden" name="sellOrder" value="" />
        <input type="hidden" name="idBuyer" value="" />
        <input type="hidden" name="idNft" value="" />
        <input type="hidden" name="idSeller" value="" />
        <button class="px-4 py-2 rounded-md text-white bg-cyan-600 shadow-md hover:shadow-xl hover:bg-cyan-800 transition duration-300" type="submit"><spring:message code="offerModal.accept"/></button>
    </form>
</dialog>

<!-- Confirm Reject Modal -->
<dialog class="relative p-4 rounded-lg text-center max-w-md" id="reject-offer-modal">
  <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-reject-offer-modal">X</button>
  <h2 class="font-bold text-xl text-red-500"><spring:message code="offerModal.rejectOffer"/></h2>
  <p class="py-6 text-slate-600"><spring:message code="offerModal.confirmReject"/></p>

  <c:url value="/buyorder/delete" var="deleteBuyOrder" />
  <form action="${deleteBuyOrder}" method="post" class="mb-0">
      <input type="hidden" name="sellOrder" value="" />
      <input type="hidden" name="idBuyer" value="" />
      <input type="hidden" name="idNft" value="" />
      <button class="px-4 py-2 rounded-md text-white bg-red-500 shadow-md hover:shadow-xl transition duration-300 hover:bg-red-900" type="submit"><spring:message code="offerModal.reject"/></button>
  </form>
</dialog>

<script defer>
    const confirmOfferModal = document.querySelector("#confirm-offer-modal")
    const closeConfirmOfferModal = document.querySelector("#close-confirm-offer-modal")
    const rejectOfferModal = document.querySelector("#reject-offer-modal")
    const closeRejectOfferModal = document.querySelector("#close-reject-offer-modal")

    closeRejectOfferModal.addEventListener("click", () => rejectOfferModal.close())
    closeConfirmOfferModal.addEventListener("click", () => confirmOfferModal.close())

    function handleAccept(sellOrderId, buyerId, sellerId, productId) {
      setInputData(confirmOfferModal, sellOrderId, buyerId, productId)
      confirmOfferModal.querySelector("input[name='idSeller']").value = sellerId
      confirmOfferModal.showModal()
    }

    function handleReject(sellOrderId, buyerId, productId) {
      setInputData(rejectOfferModal, sellOrderId, buyerId, productId)
      rejectOfferModal.showModal()
    }

    function setInputData(modal, sellOrderId, buyerId, productId) {
      modal.querySelector("input[name='sellOrder']").value = sellOrderId
      modal.querySelector("input[name='idBuyer']").value = buyerId
      modal.querySelector("input[name='idNft']").value = productId
    }
</script>