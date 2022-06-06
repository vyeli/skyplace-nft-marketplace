<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="flex flex-col pt-4">
    <div class="flex flex-row items-center justify-between">
        <div class="flex items-center space-x-4">
            <img class="w-14 h-14 rounded-full" src="<c:url value='/resources/profile_picture.png'/>" alt="">
            <c:url value="/profile/${param.reviewerId}" var="reviewerProfileUrl"/>
            <div class="space-y-1 font-medium">
                <a href='<c:out value="${reviewerProfileUrl}"/>'><span class="text-lg text-cyan-600 decoration-current hover:text-cyan-800 hover:underline"><c:out value="${param.reviewerName}"/></span></a>
            </div>
        </div>
        <div class="flex flex-row">
            <c:forEach var="i" begin="${param.minScore}" end="${param.score}">
                <img class="h-9 w-9" src="<c:url value='/resources/filled_star.svg'/>" alt=""/>
            </c:forEach>
            <c:forEach var="j" begin="${param.score}" end="${param.maxScore-1}">
                <img class="h-9 w-9" src="<c:url value='/resources/empty_star.svg'/>" alt=""/>
            </c:forEach>
        </div>
    </div>
    <h3 class="font-semibold text-gray-500 mt-4">${param.title}</h3>
    <p class="mb-2 mt-1 font-light text-gray-500">${param.comments}</p>
    <c:if test="${param.isAdmin || param.isReviewer}">
        <c:url value='/review/${param.revieweeId}/delete' var="deletePath"/>
        <div class="flex flex-row items-center justify-end">
            <button id="open-delete-modal-${param.modalId}" class="shadow-md px-6 py-2.5 rounded-md transition duration-300 bg-red-500 hover:bg-red-900 text-white hover:shadow-xl cursor-pointer">
                <spring:message code="review.delete"/>
            </button>
        </div>
        <!-- Modal -->
        <spring:message code="review.delete" var="deleteReview"/>
        <spring:message code="review.deleteDesc" var="deleteReviewDesc"/>
        <dialog class="relative p-4 rounded-lg text-center max-w-md" id="delete-modal-${param.modalId}">
            <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-delete-modal-${param.modalId}">X</button>
            <h2 class="font-bold text-xl text-red-500"><c:out value="${deleteReview}"/></h2>
            <p class="py-6 text-slate-600"><c:out value="${deleteReviewDesc}"/></p>
            <form class="mb-0" action='<c:out value="${deletePath}"/>' method="post">
                <input type="hidden" name="reviewId" value='<c:out value="${param.reviewId}"/>'>
                <button class="px-4 py-2 rounded-md text-white bg-red-500 transition duration-300 hover:bg-red-800 text-white hover:shadow-xl" type="submit"><spring:message code="deleteModal.delete"/></button>
            </form>
        </dialog>
    </c:if>
</div>

<script defer>
    const modal${param.modalId} = document.querySelector("#delete-modal-<c:out value='${param.modalId}'/>");
    const openModal${param.modalId} = document.querySelector("#open-delete-modal-<c:out value='${param.modalId}'/>");
    const closeModal${param.modalId} = document.querySelector("#close-delete-modal-<c:out value='${param.modalId}'/>");

    openModal${param.modalId}.addEventListener("click", () => {
        modal${param.modalId}.showModal();
    });

    closeModal${param.modalId}.addEventListener("click", () => {
        modal${param.modalId}.close();
    });
</script>
