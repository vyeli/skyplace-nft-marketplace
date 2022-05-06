<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!-- Modal -->
<dialog class="relative p-4 rounded-lg text-center max-w-md" id="delete-modal">
    <button class="absolute top-4 right-6 font-bold text-slate-600" id="close-delete-modal">X</button>
    <h2 class="font-bold text-xl text-red-500">${param.title}</h2>
    <p class="py-6 text-slate-600">${param.description}</p>

    <c:url value="${param.deletePath}" var="deletePath"/>
    <form class="mb-0" action="${deletePath}" method="post">
        <button class="px-4 py-2 rounded-md text-white bg-red-500" type="submit"><spring:message code="deleteModal.delete"/></button>
    </form>
</dialog>

<script>
    const modal = document.querySelector("#delete-modal");
    const openModal = document.querySelector("#open-delete-modal");
    const closeModal = document.querySelector("#close-delete-modal");

    openModal.addEventListener("click", () => {
        modal.showModal();
    });

    closeModal.addEventListener("click", () => {
        modal.close();
    });
</script>
