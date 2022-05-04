<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!-- Confirm Sell Modal -->
<dialog class="relative p-4 rounded-lg text-center max-w-md" id="confirm-offer-modal">
    <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-confirm-offer-modal">X</button>
    <h2 class="font-bold text-xl text-cyan-700">Accept offer</h2>
    <p class="py-6 text-slate-600">Are you sure you want to accept the selected offer?</p>

    <c:url value="/buyorder/confirm" var="confirmBuyOrder" />
    <form action="${confirmBuyOrder}" method="post" class="mb-0">
        <input type="hidden" name="sellOrder" value="" />
        <input type="hidden" name="buyer" value="" />
        <input type="hidden" name="product" value="" />
        <button class="px-4 py-2 rounded-md text-white bg-cyan-700" type="submit">Accept</button>
    </form>
</dialog>

<!-- Confirm Reject Modal -->
<dialog class="relative p-4 rounded-lg text-center max-w-md" id="reject-offer-modal">
  <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-reject-offer-modal">X</button>
  <h2 class="font-bold text-xl text-red-500">Reject offer</h2>
  <p class="py-6 text-slate-600">Are you sure you want to reject the selected offer?</p>

  <c:url value="/buyorder/delete" var="deleteBuyOrder" />
  <form action="${deleteBuyOrder}" method="post" class="mb-0">
      <input type="hidden" name="sellOrder" value="" />
      <input type="hidden" name="buyer" value="" />
      <input type="hidden" name="product" value="" />
      <button class="px-4 py-2 rounded-md text-white bg-red-500" type="submit">Reject</button>
  </form>
</dialog>

<script>
    const confirmOfferModal = document.querySelector("#confirm-offer-modal")
    const closeConfirmOfferModal = document.querySelector("#close-confirm-offer-modal")
    const rejectOfferModal = document.querySelector("#reject-offer-modal")
    const closeRejectOfferModal = document.querySelector("#close-reject-offer-modal")

    closeRejectOfferModal.addEventListener("click", () => rejectOfferModal.close())
    closeConfirmOfferModal.addEventListener("click", () => confirmOfferModal.close())

    function handleAccept(sellOrderId, buyerId, productId) {
      setInputData(confirmOfferModal, sellOrderId, buyerId, productId)
      confirmOfferModal.showModal()
    }

    function handleReject(sellOrderId, buyerId, productId) {
      setInputData(rejectOfferModal, sellOrderId, buyerId, productId)
      rejectOfferModal.showModal()
    }

    function setInputData(modal, sellOrderId, buyerId, productId) {
      modal.querySelector("input[name='sellOrder']").value = sellOrderId
      modal.querySelector("input[name='buyer']").value = buyerId
      modal.querySelector("input[name='product']").value = productId
    }
</script>