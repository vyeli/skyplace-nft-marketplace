<%@ page import="ar.edu.itba.paw.model.StatusPurchase" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col max-w-[100vw]">
<!-- Header -->
<%@ include file="../components/navbar.jsp" %>
<div class="flex flex-col flex-wrap pb-8 gap-8 mx-10 lg:mx-24 xl:mx-40">
    <c:url value='/profile/${userId}' var="profilePath"/>
    <!-- Profile -->
    <jsp:include page="../components/ProfileDescription.jsp">
        <jsp:param name="userId" value="${user.id}"/>
        <jsp:param name="username" value="${user.username}"/>
        <jsp:param name="email" value="${user.email}"/>
        <jsp:param name="wallet" value="${user.wallet}"/>
        <jsp:param name="score" value="${userScore}"/>
        <jsp:param name="reviewAmount" value="${reviewAmount}"/>
    </jsp:include>

    <!-- Tabs -->
    <div class="flex border-b border-gray-200">
        <ul class="flex flex-wrap flex-grow justify-evenly items-center font-medium text-lg text-center text-gray-500">
            <c:forEach var="tab" items="${tabs}">
                <c:choose>
                    <c:when test="${tab.active}">
                        <c:set var="tabClass" value="border-b-2 border-cyan-600 text-cyan-600 active"/>
                        <c:set var="iconUrl" value="/resources/tabs/active/${tab.name}.svg"/>
                        <c:set var="activeTab" value="${tab}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="tabClass" value="border-transparent hover:text-gray-700"/>
                        <c:set var="iconUrl" value="/resources/tabs/inactive/${tab.name}.svg"/>
                    </c:otherwise>
                </c:choose>
                <li class="${tabClass} pb-3">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get" class="flex flex-row gap-2">
                        <form:button type="submit" name="tab" value="${tab.name}" class="flex">
                            <img class="h-6 w-6 mr-2" src='<c:url value="${iconUrl}"/>' alt="tab_icon"/>
                            <spring:message code="profile.${tab.name}"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="1"/>
                    </form:form>
                </li>
            </c:forEach>
        </ul>
    </div>

    <!-- Paginator and dropdown -->
    <div class="flex flex-row items-center justify-between">
        <!-- Paginator -->
        <div class="flex flex-row text-lg items-center">
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <input type="hidden" name="tab" value="${activeTab.name}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <form:button type="submit" path="page" name="page" value="${currentPage-1}" class="text-cyan-400 cursor-pointer mr-2" ><spring:message code="profile.previous"/></form:button>
                    </c:when>
                    <c:otherwise>
                        <span class="text-gray-400 mr-2 cursor-default"><spring:message code="profile.previous"/></span>
                    </c:otherwise>
                </c:choose>
            </form:form>
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <input type="hidden" name="tab" value="${activeTab.name}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <form:input path="page" type="number" min="1" max="${pages}" value="${currentPage}"
                            class="w-9 border-2 border-slate-400 text-[1.1rem] rounded-lg pl-2 mx-1 py-0.5 focus:ring-cyan-700 focus:border-cyan-700" />
                <span><spring:message code="profile.of" arguments="${pages}"/></span>
            </form:form>
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get" class="flex items-center">
                <input type="hidden" name="tab" value="${activeTab.name}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <c:choose>
                    <c:when test="${currentPage < pages}">
                        <form:button type="submit" path="page" name="page" value="${currentPage+1}" class="text-cyan-400 cursor-pointer ml-2" ><spring:message code="profile.next"/></form:button>
                    </c:when>
                    <c:otherwise>
                        <span class="text-gray-400 ml-2 cursor-default"><spring:message code="profile.next"/></span>
                    </c:otherwise>
                </c:choose>
            </form:form>
        </div>

        <!-- Dropdown menu -->
        <c:if test="${activeTab.name == 'inventory' || activeTab.name == 'selling' || activeTab.name == 'favorited'}">
            <div class="flex flex-row text-2xl">
                <button id="sortDropdownDefault" data-dropdown-toggle="sortDropdown" class="border-2 border-cyan-600 font-medium rounded-lg text-base px-3 py-2 text-center inline-flex items-center" type="button">
                    <p class="text-cyan-700"><c:out value="${sortName}" /></p>
                    <svg class="w-4 h-4 ml-2 stroke-cyan-700" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
                </button>
                <div id="sortDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-48">
                    <form:form class="mb-0" modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <input type="hidden" name="tab" value="${activeTab.name}"/>
                        <form:hidden path="page" value="1" />
                        <ul class="text-sm leading-normal text-gray-700 border border-slate-100 divide-y divide-slate-100" aria-labelledby="sortDropdownDefault">
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="name" class="block px-4 py-3 hover:bg-gray-100 flex w-full" ><spring:message code="explore.name"/></form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceAsc" class="block px-4 py-3 hover:bg-gray-100 flex w-full" ><spring:message code="explore.PriceAs"/></form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceDsc" class="block px-4 py-3 hover:bg-gray-100 flex w-full" ><spring:message code="explore.PriceDe"/></form:button>
                            </li>
                        </ul>
                    </form:form>
                </div>
            </div>
        </c:if>
        <c:if test="${activeTab.name == 'buyorders'}">
            <form:form class="flex flex-row gap-2 mb-0" modelAttribute="profileFilter" action="${profilePath}" method="get">
                <c:set var="inactiveTagClasses" value="block px-4 py-2 bg-slate-100 hover:bg-slate-400 hover:text-white rounded-lg flex w-full text-slate-700 border border-slate-400 transition duration-300 hover:shadow-xl"/>
                <c:set var="activeTagClasses" value="block px-4 py-2 bg-cyan-600 rounded-lg flex w-full text-white"/>
                <input type="hidden" name="tab" value="${activeTab.name}"/>
                <form:hidden path="page" value="1" />
                    <c:forEach items="${buyOrderItemTypes}" var="item">
                        <c:choose>
                            <c:when test="${item.active}">
                                <c:set var="itemClasses" value="${activeTagClasses}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="itemClasses" value="${inactiveTagClasses}"/>
                            </c:otherwise>
                        </c:choose>
                        <form:button type="submit" path="items" name="items" value="${item.name}" class="${itemClasses} w-max">
                            <spring:message code="buyorders.${item.name}"/>
                        </form:button>
                    </c:forEach>
            </form:form>
        </c:if>
    </div>

    <!-- Objects (NFTs, history or review cards) -->
    <c:choose>
        <c:when test="${activeTab.name == 'buyorders'}">
            <div class="flex flex-col gap-2 w-3/4 max-w-4xl self-center">
                <c:forEach items="${buyOrderItems}" var="buyOrderItem">
                    <jsp:include page="../components/BuyOrderItem.jsp">
                        <jsp:param name="nftName" value="${buyOrderItem.offeredFor.nft.nftName}"/>
                        <jsp:param name="productId" value="${buyOrderItem.offeredFor.nft.id}"/>
                        <jsp:param name="nftCollectionId" value="${buyOrderItem.offeredFor.nft.nftId}"/>
                        <jsp:param name="nftContractAddr" value="${buyOrderItem.offeredFor.nft.contractAddr}"/>
                        <jsp:param name="nftImg" value="${buyOrderItem.offeredFor.nft.idImage}"/>
                        <jsp:param name="price" value="${buyOrderItem.amount.toPlainString()}"/>
                        <jsp:param name="sellOrderId" value="${buyOrderItem.offeredFor.id}"/>
                        <jsp:param name="buyerId" value="${buyOrderItem.offeredBy.id}"/>
                        <jsp:param name="buyerUsername" value="${buyOrderItem.offeredBy.username}"/>
                        <jsp:param name="offerDate" value="${buyOrderItem.pendingDate}" />
                        <jsp:param name="buyerWallet" value="${buyOrderItem.offeredBy.wallet}"/>
                        <jsp:param name="sellerUsername" value="${buyOrderItem.offeredFor.nft.owner.username}"/>
                        <jsp:param name="sellerWallet" value="${buyOrderItem.offeredFor.nft.owner.wallet}"/>
                        <jsp:param name="status" value="${buyOrderItem.status}"/>
                        <jsp:param name="isOwner" value="${isOwner}"/>
                        <jsp:param name="isAdmin" value="${isAdmin}"/>
                        <jsp:param name="isMySale" value="${isMySale}" />
                    </jsp:include>
                </c:forEach>
                <c:if test="${buyOrderItemsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="bidded.noBidsPlaced"/></span>
                </c:if>
            </div>
        </c:when>
        <c:when test="${activeTab.name == 'history'}">
            <div class="flex flex-col gap-2 w-3/4 max-w-4xl self-center">
                <c:forEach items="${historyItems}" var="item">
                    <jsp:include page="../components/HistoryItem.jsp">
                        <jsp:param name="nftName" value="${item.nftsByIdNft.nftName}"/>
                        <jsp:param name="id" value="${item.nftsByIdNft.id}"/>
                        <jsp:param name="nftId" value="${item.nftsByIdNft.nftId}"/>
                        <jsp:param name="nftImg" value="${item.nftsByIdNft.idImage}"/>
                        <jsp:param name="isNftDeleted" value="${item.nftsByIdNft.deleted}"/>
                        <jsp:param name="price" value="${item.price.toPlainString()}"/>
                        <jsp:param name="date" value="${item.buyDate}"/>
                        <jsp:param name="sellerUsername" value="${item.seller.username}"/>
                        <jsp:param name="sellerId" value="${item.seller.id}"/>
                        <jsp:param name="buyerUsername" value="${item.buyer.username}"/>
                        <jsp:param name="buyerId" value="${item.buyer.id}"/>
                        <jsp:param name="sold" value="${user.id == item.seller.id}"/>
                        <jsp:param name="status" value="${item.status.statusName}"/>
                    </jsp:include>
                </c:forEach>
                <c:if test="${historyItemsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="profile.noActivity"/></span>
                </c:if>
            </div>
        </c:when>
        <c:when test="${activeTab.name == 'reviews'}">
            <jsp:include page="../components/ReviewsContent.jsp">
                <jsp:param name="reviewAmount" value="${reviewAmount}"/>
                <jsp:param name="minScore" value="${minScore}"/>
                <jsp:param name="maxScore" value="${maxScore}"/>
                <jsp:param name="canReview" value="${canReview}"/>
                <jsp:param name="userId" value="${user.id}"/>
                <jsp:param name="currentUserId" value="${currentUserId}"/>
            </jsp:include>
        </c:when>
        <c:otherwise>
            <div class="flex flex-wrap justify-center gap-8">
                <c:forEach items="${publications}" var="publication">
                    <c:if test="${publication.nft.sellOrder != null}">
                        <c:set value="${publication.nft.sellOrder.price.toPlainString()}" var="sellPrice" />
                        <c:set value="${publication.nft.sellOrder.category}" var="sellCategory" />
                    </c:if>
                    <jsp:include page="../components/Card.jsp">
                        <jsp:param name="name" value="${publication.nft.nftName}"/>
                        <jsp:param name="nftId" value="${publication.nft.nftId}" />
                        <jsp:param name="descr" value="${publication.nft.description}"/>
                        <jsp:param name="img" value="${publication.nft.idImage}"/>
                        <jsp:param name="onSale" value="${publication.nft.sellOrder != null}"/>
                        <jsp:param name="price" value="${sellPrice}" />
                        <jsp:param name="category" value="${sellCategory}"/>
                        <jsp:param name="sellerUsername" value="${publication.nft.owner.username}"/>
                        <jsp:param name="idProduct" value="${publication.nft.id}"/>
                        <jsp:param name="isFaved" value="${publication.isFaved != null}" />
                    </jsp:include>
                </c:forEach>
                <c:if test="${publicationsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="profile.noNft"/></span>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${activeTab.name == 'buyorders'}">
<jsp:include page="../components/ConfirmTransactionModal.jsp" />

<jsp:include page="../components/DeleteBuyOfferModal.jsp" />
</c:if>
</body>
</html>
