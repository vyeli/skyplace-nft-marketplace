<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>



<html>
<%@ include file="Head.jsp" %>
<body class=" font-body overflow-x-hidden">
<%@ include file="../components/navbar.jsp" %>
<main class="mt-12 mb-8">
    <section class="flex justify-around">
        <div class="container">
            <div class="flex flex-row ">
                <!-- Image and tabs -->
                <div class="flex-col max-w-[50%]">
                    <!--Image-->
                    <figure class="mb-8 flex justify-center">
                        <img src="<c:url value="/images/${nft.idImage}" />"
                             alt="<c:out value="${nft.nftName}" />" class="rounded-2xl">
                    </figure>
                    <!--TAGS -->
                    <c:choose>
                        <c:when test="${showOfferTab}">
                            <c:set value="active" var="offerActive" />
                            <c:set value="show" var="showOffer" />
                        </c:when>
                        <c:otherwise>
                            <c:set value="active" var="detailActive" />
                            <c:set value="show" var="showDetail" />
                        </c:otherwise>
                    </c:choose>

                    <div class="rounded-lg border-2">
                        <ul class="nav nav-tabs flex flex-col md:flex-row flex-wrap list-none border-b-0 pl-0 border-b"
                            id="tabs-tab" role="tablist">
                            <!--Details -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-cyan-400 relative flex items-center whitespace-nowrap py-3 px-6 ${detailActive}"
                                        id="tabs-home-tab" data-bs-toggle="tab" data-bs-target="#tabs-home"
                                        type="button"
                                        role="tab" aria-controls="tabs-home" aria-selected="${detailActive != null}">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M8 4h13v2H8V4zm-5-.5h3v3H3v-3zm0 7h3v3H3v-3zm0 7h3v3H3v-3zM8 11h13v2H8v-2zm0 7h13v2H8v-2z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium"><spring:message code="product.details"/></span>
                                </button>
                            </li>

                            <!-- Offers -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-cyan-400 relative flex items-center whitespace-nowrap py-3 px-6 ${offerActive}"
                                        id="tabs-messages-tab" data-bs-toggle="tab" data-bs-target="#tabs-messages"
                                        type="button" role="tab" aria-controls="tabs-messages" aria-selected="${offerActive != null}">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M20 22H4a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1v18a1 1 0 0 1-1 1zm-1-2V4H5v16h14zM7 6h4v4H7V6zm0 6h10v2H7v-2zm0 4h10v2H7v-2zm6-9h4v2h-4V7z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium"><spring:message code="product.offers"/></span>
                                </button>
                            </li>
                        </ul>
                        <!-- TAB CONTENT-->
                        <div class="tab-content p-4 pt-6 bg-slate-50" id="tabs-tabContent">
                            <div class="tab-pane fade ${showDetail} ${detailActive}" id="tabs-home" role="tabpanel"
                                 aria-labelledby="tabs-home-tab">
                                <div class="flex flex-col gap-2">
                                    <div class="flex justify-between">
                                        <p><spring:message code="product.contractAddress"/> </p>
                                        <p class="w-3/5 break-words max-h-20 overflow-hidden text-right">
                                            <c:out value="${nft.contractAddr}" />
                                        </p>
                                    </div>
                                    <div class="flex justify-between">
                                        <p>Token ID</p>
                                        <p class="w-3/5 overflow-hidden text-right"><c:out value="${nft.nftId}" /></p>
                                    </div>
                                    <c:if test="${sellOrder != null}">
                                        <div class="flex justify-between">
                                            <p><spring:message code="product.category"/></p>
                                            <p><c:out value="${sellOrder.category}" /></p>
                                        </div>
                                    </c:if>
                                    <div class="flex justify-between">
                                        <p>Blockchain</p>
                                        <p><c:out value="${nft.chain}" /></p>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="tabs-profile" role="tabpanel"
                                 aria-labelledby="tabs-profile-tab">
                                <spring:message code="prodcut.noproperty"/>
                            </div>
                            <div class="tab-pane fade ${showOffer} ${offerActive}" id="tabs-messages" role="tabpanel"
                                 aria-labelledby="tabs-profile-tab">
                                <div class="flex flex-col divide-y">
                                    <c:set var="offerCount" value="1" scope="page" />
                                    <c:forEach items="${buyOffer}" var="offer">
                                        <div class="flex items-center justify-between py-3">
                                            <div class="flex flex-col">
                                                <div class="flex gap-1">
                                                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"></path></svg>
                                                    <p class="font-semibold">
                                                        <spring:message code="product.offer"/>
                                                        <span>#<c:out value="${offerCount+5*(offerPage-1)}"/></span>
                                                    </p>
                                                </div>
                                                <p class="text-sm break-words line-clamp-3 w-80">
                                                    <spring:message code="product.by"/>
                                                    <a href="<c:url value="/profile/${offer.buyer.id}" />" class="text-cyan-600">
                                                        <c:out value="${offer.buyer.email}"/>
                                                    </a>
                                                </p>
                                            </div>
                                            <div class="flex flex-col gap-1 items-end">
                                                <div class="flex">
                                                    <svg x="0" y="0" viewBox="0 0 1920 1920" xml:space="preserve" class="mr-1 h-5 w-5">
                                                        <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                                        <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                                        <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                                        <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                                        <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                                                    </svg>
                                                    <span class="text-slate-700">
                                                        <c:out value="${offer.buyOrder.amount}"/>
                                                    </span>
                                                </div>
                                                <c:if test="${sellOrder != null && owner.id == currentUser.id}">
                                                    <div class="flex gap-2">
                                                        <button onclick="handleReject(${offer.buyOrder.idSellOrder}, ${offer.buyOrder.idBuyer}, ${productId})" class="py-0.5 px-3 text-sm border rounded-lg bg-white border-slate-400">
                                                            <spring:message code="product.reject"/>
                                                        </button>
                                                        <button onclick="handleAccept(${offer.buyOrder.idSellOrder}, ${offer.buyOrder.idBuyer}, ${currentUser.id}, ${sellOrder.price}, ${productId})" class="py-0.5 px-3 text-sm border rounded-lg bg-cyan-600 text-white border-cyan-600">
                                                            <spring:message code="product.accept"/>
                                                        </button>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                        <c:set var="offerCount" value="${offerCount + 1}" scope="page"/>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${sellOrder == null}">
                                            <span><spring:message code="product.nosale"/></span>
                                        </c:when>
                                        <c:when test="${offerCount == 1}">
                                            <span><spring:message code="product.noOffers"/></span>
                                        </c:when>
                                    </c:choose>
                                    <c:if test="${amountOfferPages > 1 && offerPage <= amountOfferPages}">
                                    <div class="flex justify-center pt-4">
                                        <c:if test="${offerPage > 1}">
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get" class="pr-2">
                                                <input type="hidden" name="offerPage" value="${offerPage-1}" />
                                                <button type="submit" class="text-cyan-700 text-lg">
                                                    <spring:message code="prodcut.previous"/>
                                                </button>
                                            </form>
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get">
                                                <input type="hidden" name="offerPage" value="1" />
                                                <button type="submit" class="text-cyan-700 text-lg px-2">
                                                    1
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${offerPage == 1}">
                                            <span class="text-slate-400 text-lg pr-2"><spring:message code="prodcut.previous"/></span>
                                        </c:if>
                                        <c:if test="${offerPage-1 > 1}">
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get" >
                                                <input type="hidden" name="offerPage" value="${offerPage-1}" />
                                                <button type="submit" class="text-cyan-700 text-lg px-2">
                                                    ...<c:out value="${offerPage-1}"/>
                                                </button>
                                            </form>
                                        </c:if>
                                        <span class="text-lg px-2">${offerPage}</span>
                                        <c:if test="${offerPage+1 < amountOfferPages}">
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get">
                                                <input type="hidden" name="offerPage" value="${offerPage+1}" />
                                                <button type="submit" class="text-cyan-700 text-lg px-2">
                                                    <c:out value="${offerPage+1}"/>...
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${offerPage < amountOfferPages}">
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get">
                                                <input type="hidden" name="offerPage" value="${amountOfferPages}" />
                                                <button type="submit" class="text-cyan-700 text-lg px-2">
                                                    <c:out value="${amountOfferPages}"/>
                                                </button>
                                            </form>
                                            <form action="<c:url value="/product/${productId}#tabs-tab" />" method="get" class="px-2">
                                                <input type="hidden" name="offerPage" value="${offerPage+1}" />
                                                <button type="submit" class="text-cyan-700 text-lg">
                                                    <spring:message code="product.next"/>
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${offerPage == amountOfferPages}">
                                            <span class="text-slate-400 text-lg px-2"><spring:message code="product.next"/></span>
                                        </c:if>
                                    </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Description -->
                <div class="md:w-3/5 md:basis-auto md:pl-8 lg:w-1/2 lg:pl-[3.75rem]">
                    <!-- Collection / Likes -->
                    <div class="mb-3 flex">
                        <!-- Collection -->
                        <div class="flex items-center">
                            <c:url value="/explore" var="exploreUrl" />
                            <form action="${exploreUrl}" method="get" class="mb-0">
                                <input type="hidden" name="search" value="${nft.collection}" />
                                <input type="hidden" name="searchFor" value="collection" />
                                <button type="submit" class="text-accent mr-2 underline decoration-cyan-700 font-bold max-w-[24rem] truncate"><spring:message code="product.collection"/> <c:out value="${nft.collection}" /></button>
                            </form>
                            <span class=" bg-green inline-flex h-6 w-6 items-center justify-center rounded-full border-2 border-white"
                                  data-tippy-content="Verified Collection">
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                   class="h-[.875rem] w-[.875rem] fill-white">
                                <path fill="none" d="M0 0h24v24H0z"></path>
                                <path d="M10 15.172l9.192-9.193 1.415 1.414L10 18l-6.364-6.364 1.414-1.414z"></path>
                              </svg>
                            </span>
                        </div>
                        <!-- Likes -->
                        <div class="ml-auto flex">
                            <c:choose>
                                <c:when test="${isFaved}">
                                    <c:url value="/favorite/remove/${nft.id}" var="favPath" />
                                </c:when>
                                <c:otherwise>
                                    <c:url value="/favorite/add/${nft.id}" var="favPath" />
                                </c:otherwise>
                            </c:choose>

                            <div class="flex items-center space-x-1 rounded-l-xl border bg-white py-2 px-2">
                                <form action="${favPath}" method="post" class="m-auto">
                                    <button type="submit" class="flex">
                                        <c:choose>
                                            <c:when test="${isFaved}">
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
                                      <span class="pl-1 text-base"><c:out value="${favorites}" /></span>
                                    </button>
                                </form>
                            </div>
                            <c:if test="${currentUser.id == owner.id || isAdmin}">
                                <button id="productButton" data-dropdown-toggle="productMenu" type="button" class="flex items-center space-x-1 rounded-r-xl border bg-white py-2 px-2">
                                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z"></path></svg>
                                </button>
                            </c:if>
                            <div id="productMenu" class="hidden z-10 w-44 bg-white flex flex-col flex-grow rounded text-gray-700 border border-gray-300 text-sm divide-y divide-gray-300 shadow">
                                <ul class="py-1" aria-labelledby="productButton">
                                    <c:if test="${currentUser.id == owner.id || isAdmin}">
                                        <c:choose>
                                            <c:when test="${sellOrder != null}">
                                                <c:if test="${currentUser.id == owner.id}">
                                                <li class="z-20">
                                                    <a href="<c:url value="/sell/update/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                        <span><spring:message code="product.updateSell"/></span>
                                                    </a>
                                                </li>
                                                </c:if>
                                                <li class="z-20">
                                                    <span id="open-delete-modal" class="block cursor-pointer flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                        <spring:message code="product.deleteSell"/>
                                                    </span>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${currentUser.id == owner.id}">
                                                    <li class="z-20">
                                                        <a href="<c:url value="/sell/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                            <span><spring:message code="product.sell"/></span>
                                                        </a>
                                                    </li>
                                                </c:if>
                                                <li class="z-20">
                                                    <span id="open-delete-modal" class="block cursor-pointer flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                        <spring:message code="product.delete"/>
                                                    </span>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!--Name and ID-->
                    <h1 class="font-display text-jacarta-700 mb-2 text-4xl font-semibold break-words line-clamp-3"><c:out value="${nft.nftName}" /><span>#<c:out value="${nft.nftId}" /></span></h1>
                    <!-- Owner -->
                    <div class="flex pt-2 pb-4 truncate">
                        <span class="text-jacarta-400 block text-sm">
                            <spring:message code="product.own"/>
                        </span>
                        <a href="<c:url value="/profile/${owner.id}" />" class="text-accent block">
                            <span class="text-sm font-bold pl-2 text-cyan-600"><c:out value="${owner.email}" /></span>
                        </a>
                    </div>
                    <div class="mb-8 flex items-center space-x-4 whitespace-nowrap">
                        <div class="flex text-xl items-center">
                            <!--ETH SVG-->
                            <span class="-ml-1" data-tippy-content="ETH">
                              <svg x="0" y="0" viewBox="0 0 1920 1920"
                                   xml:space="preserve" class="mr-1 h-7 w-7">
                                <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                              </svg>
                            </span>
                            <c:choose>
                                <c:when test="${sellOrder != null}">
                                    <span class="text-green font-bold tracking-tight">${sellOrder.price}</span>
                                    <span id="priceTag" class="ml-4 text-slate-500 text-base"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-green text-[1.1rem] font-bold tracking-tight"><spring:message code="product.notforsale"/></span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class=" border-gray-200 rounded-2xl border bg-white pb-4 flex-col justify-between mb-8 bg-slate-50">
                        <div class="flex text-xl px-4  py-2 border-b bg-white rounded-t-2xl">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7"></path></svg>
                            <span class="pl-2 font-semibold"><spring:message code="product.description"/></span>
                        </div>
                        <p class="mb-4 px-4 pt-4 break-words line-clamp-18">
                            <c:out value="${nft.description}" />
                        </p>
                    </div>
                    <!-- Bid -->
                    <c:if test="${sellOrder != null && (currentUser == null || owner.id != currentUser.id)}">
                    <div class=" border-gray-200 rounded-2xl border bg-white pb-4 flex-col justify-between mb-8 bg-slate-50">
                        <div class="flex text-xl px-4  py-2 border-b bg-white rounded-t-2xl">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"></path></svg>
                            <span class="pl-2 font-semibold"><spring:message code="product.buy"/></span>
                        </div>
                        <div class="mb-4 px-4 pt-4">
                            <c:url value="/product/${nft.id}" var="postPath" />
                            <form:form modelAttribute="buyNftForm" action="${postPath}" method="post">
                                <label class="flex mb-4 items-center">
                                    <span class="text-lg pr-2"><spring:message code="product.yourOffer"/></span>
                                    <svg x="0" y="0" viewBox="0 0 1920 1920" xml:space="preserve" class="mr-1 h-6 w-6">
                                    <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                        <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                        <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                        <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                        <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                                    </svg>
                                    <form:input id="offerInput" type="number" value="0" class="rounded-lg border-slate-300" min="0" step="0.000000000000000001" path="price" />
                                </label>
                                <input type="submit" class="bg-cyan-600 shadow-accent-volume hover:bg-cyan-700 inline-block w-1/2 rounded-lg py-3 px-8 text-center font-semibold text-white transition-all cursor-pointer" value="<spring:message code="product.makeOffer"/>">
                                <span id="offerDisplay" class="ml-4 text-slate-500 text-base"></span>
                                <form:errors path="price" element="p" cssStyle="color: tomato" />
                            </form:form>
                        </div>
                    </div>
                    </c:if>
                </div>
            </div>
            </div>
    </section>
</main>
<%@ include file="../components/OfferModal.jsp" %>
<spring:message code="product.deleteSellOrder" var="deleteSellOrder"/>
<spring:message code="product.deleteSellOrderConfirm" var="deleteSellOrderConfirm"/>
<spring:message code="product.deleteNft" var="deleteNft"/>
<spring:message code="product.deleteNftConfirm" var="deleteNftConfirm"/>


<c:if test="${currentUser.id == owner.id || isAdmin}">
    <c:choose>
        <c:when test="${sellOrder != null}">
            <jsp:include page="../components/DeleteModal.jsp">
                <jsp:param name="title" value="${deleteSellOrder}"/>
                <jsp:param name="description" value="${deleteSellOrderConfirm}"/>
                <jsp:param name="deletePath" value="/sell/delete/${nft.id}"/>
            </jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="../components/DeleteModal.jsp">
                <jsp:param name="title" value="${deleteNft}"/>
                <jsp:param name="description" value="${deleteNftConfirm}"/>
                <jsp:param name="deletePath" value="/product/delete/${nft.id}"/>
            </jsp:include>
        </c:otherwise>
    </c:choose>
</c:if>
<c:if test="${sellOrder != null}">
    <script type="module" defer>
        const coingeckoApiEndpoint = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd"

        const response = await fetch(coingeckoApiEndpoint).catch(e => console.log(e))
        const prices = await response.json()
        const usdPrice = ${sellOrder.price} * prices.ethereum.usd
        document.getElementById("priceTag").innerText = "~ " + usdPrice.toFixed(0) + " USD"

        const offerInput = document.getElementById("offerInput")
        const offerDisplay = document.getElementById("offerDisplay")

        offerInput.addEventListener("keyup", e => {
            const _usdPrice = e.target.value * prices.ethereum.usd
            offerDisplay.innerText = "~ " + _usdPrice.toFixed(0) + " USD"
        })
    </script>
</c:if>
</body>
</html>
