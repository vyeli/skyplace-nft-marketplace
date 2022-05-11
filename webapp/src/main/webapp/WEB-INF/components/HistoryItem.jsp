<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- History item -->
<a href="<c:url value="/product/${param.nftId}" />" class="border-jacarta-100 rounded-2.5xl relative flex items-center border bg-white p-4 transition-shadow hover:shadow-lg">
    <figure class="mr-5 self-start">
        <img src="<c:url value="/images/${param.nftImg}" />" class="w-[6rem] h-[6rem] rounded-lg aspect-square object-cover border border-gray-300" alt="avatar 2" loading="lazy">
    </figure>

    <div class="max-w-[34rem]">
        <h3 class="font-display text-jacarta-700 mb-1 text-base font-semibold truncate">
            ${param.nftName}
        </h3>
        <div class="text-jacarta-500 mb-3 block text-sm">
            <c:choose>
                <c:when test="${param.sold}">
                    <spring:message code="history.soldTo"/>
                    <span class="text-cyan-700">${param.buyerUsername}</span>
                </c:when>
                <c:otherwise>
                    <spring:message code="history.boughtFrom"/>
                    <span class="text-cyan-700">${param.sellerUsername}</span>
                </c:otherwise>
            </c:choose>
            <spring:message code="history.for"/>
            <span>${param.price} ETH</span>
        </div>
        <span class="text-jacarta-300 block text-xs">${param.date}</span>
    </div>

    <div class="border-jacarta-100 ml-auto rounded-full border p-3">
        <c:choose>
            <c:when test="${param.sold}">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-slate-700" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                </svg>
            </c:when>
            <c:otherwise>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-slate-700" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
            </c:otherwise>
        </c:choose>
    </div>
</a>
