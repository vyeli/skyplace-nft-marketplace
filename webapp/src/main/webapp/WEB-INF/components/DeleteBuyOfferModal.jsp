<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<dialog class="relative p-4 rounded-lg text-center max-w-md" id="delete-offer-modal">
    <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-delete-offer-modal">X</button>
    <h2 class="font-bold text-xl text-red-500"><spring:message code="offerModal.rejectOffer"/></h2>
    <p class="py-6 text-slate-600"><spring:message code="offerModal.confirmReject"/></p>

    <c:url value="/buyorder/delete" var="deleteBuyOrder" />
    <form action="${deleteBuyOrder}" method="post" class="mb-0">
        <input type="hidden" name="sellOrderId" value="${param.sellOrderId}" />
        <input type="hidden" name="buyerId" value="${param.buyerId}" />
        <button class="px-4 py-2 rounded-md text-white bg-red-500 transition duration-300 shadow-md hover:shadow-xl hover:bg-red-800" type="submit">
            <spring:message code="offerModal.reject"/>
        </button>
    </form>
</dialog>

<script defer>
    const deleteBuyOfferModal = document.querySelector("#delete-offer-modal")
    const closeDeleteOfferModal = document.querySelector("#close-delete-offer-modal")

    closeDeleteOfferModal.addEventListener("click", () => deleteBuyOfferModal.close())

    function openDeleteOfferModal(sellOrderId, buyerId) {
        deleteBuyOfferModal.querySelector("input[name='sellOrderId']").value = sellOrderId
        deleteBuyOfferModal.querySelector("input[name='buyerId']").value = buyerId
        deleteBuyOfferModal.showModal()
    }
</script>