<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- Buy Order item -->
<a href="<c:url value="/product/${param.nftId}" />" class="border-jacarta-100 rounded-2.5xl relative flex items-center border bg-white p-4 transition-shadow hover:shadow-lg">
    <figure class="mr-5 self-start">
        <img src="<c:url value="/images/${param.nftImg}" />" class="w-[6rem] h-[6rem] rounded-lg aspect-square object-cover border border-gray-300" alt="avatar 2" loading="lazy">
    </figure>

    <div class="max-w-[34rem]">
        <h3 class="font-display text-jacarta-700 mb-1 text-base font-semibold truncate">
            <c:out value="${param.nftName}" />
        </h3>
        <div class="text-jacarta-500 block text-sm">
            <spring:message code="bidded.bidded" arguments="${param.price}"/>
        </div>
        <span class="text-jacarta-300 block text-xs">${param.date}</span>
    </div>

    <c:url value="/buyorder/delete" var="deleteBuyOrder" />
    <form class="mb-0 ml-auto" action="${deleteBuyOrder}" method="post">
        <input type="hidden" name="sellOrder" value="${param.sellOrderId}" />
        <input type="hidden" name="idBuyer" value="${param.buyerId}" />
        <input type="hidden" name="idNft" value="${param.nftId}" />
        <button type="submit" class="border-jacarta-100 rounded-full border border-red-200 transition duration-300 text-red-700 hover:text-white hover:bg-red-700 hover:shadow-xl p-3">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1">
                <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
            </svg>
        </button>
    </form>
</a>
