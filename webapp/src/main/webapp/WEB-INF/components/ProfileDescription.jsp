<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="flex flex-col flex-grow md:flex-row items-center mt-10">
    <img class="rounded-full h-40 w-40" src="<c:url value='/resources/profile_picture.png' />" alt="<spring:message code="profile.profileIcon"/>"/>
    <!-- Profile info -->
    <div class="flex flex-col mt-5 md:ml-5 md:mt-0 items-start justify-center gap-3">
        <!-- Profile name, wallet and more options (aligned) -->
        <div class="flex flex-row items-center justify-between">
            <div class="flex flex-row items-center">
                <span class="text-4xl font-semibold truncate max-w-[18rem] lg:max-w-[28rem] xl:max-w-[36rem] 2xl:max-w-[44rem]"><c:out value="${param.username}" /></span>
                <button id="wallet-address-button" class="flex flex-row items-center ml-5 lg:ml-10 px-2 py-1 border rounded-2xl" onclick="copyToClipboard()" data-tooltip-target="tooltip-copy" data-tooltip-placement="bottom" type="button" id="walletButton">
                    <img class="w-6 h-6" src="<c:url value='/resources/utility_icon.svg' />" alt="<spring:message code="profile.walletIcon"/>" />
                    <span class="text-xl ml-1 text-gray-400 font-semibold truncate w-28 lg:w-40 hover:text-gray-600" id="walletId"><c:out value="${param.wallet}"/></span>
                </button>
                <div id="tooltip-copy" role="tooltip" class="inline-block absolute invisible z-10 py-2 px-3 text-sm font-medium text-white bg-gray-900 rounded-lg shadow-sm opacity-0 tooltip">
                        <span id="message-copy">
                            <spring:message code="profile.copy"/>
                        </span>
                    <span id="message-copied" class="hidden">
                            <spring:message code="profile.copied"/>
                        </span>
                    <div class="tooltip-arrow" data-popper-arrow></div>
                </div>
            </div>
        </div>
        <span class="text-lg font-light text-gray-400 break-words w-96 lg:w-[36rem] xl:w-[44rem] 2xl:w-[52rem]"><c:out value="${param.email}" /></span>
        <div class="flex flex-row items-center">
            <img class="h-9 w-9" src='<c:url value="/resources/filled_star.svg"/>' alt="filled_star">
            <p class="text-lg font-bold text-gray-900 ml-1">${param.score}</p>
            <span class="w-1.5 h-1.5 mx-2 bg-gray-500 rounded-full dark:bg-gray-400"></span>
            <a href='<c:url value="/profile/${param.userId}?tab=reviews"/>' class="text-lg font-medium text-cyan-600 hover:text-cyan-800 hover:underline"><spring:message code="review.totalAmount" arguments="${param.reviewAmount}"/></a>
        </div>
    </div>
</div>

<script defer>
    <%@ include file="/js/ProfileDescription.js" %>
</script>
