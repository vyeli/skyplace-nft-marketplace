<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Card -->
<div class="relative rounded-lg group shadow-sm hover:shadow-xl w-full max-w-xs">
    <!-- NFT image -->
    <img src="<c:url value="/images/${param.img}" />" alt="<c:out value="${param.name}" />" class="aspect-[4/3] w-full rounded-t-lg object-center object-cover group-hover:opacity-80 border-slate-300 border-x border-t" />
    <!-- Name and price -->
    <div class="pt-4 px-4 flex items-center justify-between text-base font-medium text-gray-900 border-x border-slate-300 space-x-8">
        <h3 class="max-w-[20ch] truncate text-lg">
            <a href="<c:url value="/product/${param.idProduct}" />">
                <span aria-hidden="true" class="absolute inset-0"></span>
                <c:out value="${param.name}" /> #<c:out value="${param.nftId}" />
            </a>
        </h3>
        <c:choose>
            <c:when test="${param.onSale}">
                <div class="flex items-center">
                    <img class="w-8" src="<c:url value="/resources/eth_logo.svg" />" alt="eth">
                    <span class="truncate"><c:out value="${param.price}" /></span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="flex h-8 items-center">
                    <span class="min-w-max"><spring:message code="card.noSale"/></span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <!-- Seller username-->
    <p class="flex justify-between pt-1 pb-4 px-4 text-sm rounded-b-lg text-slate-500 border-x border-b border-gray-300">
        <span class="max-w-[40ch] inline-block truncate"><c:out value="${param.sellerUsername}" /></span>
        <c:if test="${param.onSale}">
            <span class="min-w-max">${param.category}</span>
        </c:if>
    </p>
    <!-- Fav button -->
    <c:if test="${param.isFaved != null}">

    <c:url value="/favorite/${param.idProduct}/${param.isFaved ? 'remove' : 'add'}" var="favPath" />

    <form action="${favPath}" method="post" class="absolute top-4 right-4 hidden group-hover:block bg-white p-2 rounded-full cursor-pointer border shadow border-gray-300">
        <button type="submit">
            <c:choose>
            <c:when test="${param.isFaved}">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-cyan-700 fill-current hover:fill-transparent" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                </svg>
            </c:when>
            <c:otherwise>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-cyan-700 hover:fill-current" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                </svg>
            </c:otherwise>
            </c:choose>
        </button>
    </form>
    </c:if>
</div>
