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
    <div class="flex flex-col flex-grow md:flex-row items-center mt-10">
        <img class="rounded-full h-40 w-40" src="<c:url value='/resources/profile_picture.png' />" alt="<spring:message code="profile.profileIcon"/>"/>
        <!-- Profile info -->
        <div class="flex flex-col mt-5 md:ml-5 md:mt-0 items-start justify-center gap-3">
            <!-- Profile name, wallet and more options (aligned) -->
            <div class="flex flex-row items-center justify-between">
                <div class="flex flex-row items-center">
                    <span class="text-4xl font-semibold truncate max-w-[18rem] lg:max-w-[28rem] xl:max-w-[36rem] 2xl:max-w-[44rem]"><c:out value="${user.username}" /></span>
                    <button id="wallet-address-button" class="flex flex-row items-center ml-5 lg:ml-10 px-2 py-1 border rounded-2xl" onclick="copyToClipboard()" data-tooltip-target="tooltip-copy" data-tooltip-placement="bottom" type="button" id="walletButton">
                        <img class="w-6 h-6" src="<c:url value='/resources/utility_icon.svg' />" alt="<spring:message code="profile.walletIcon"/>" />
                        <span class="text-xl ml-1 text-gray-400 font-semibold truncate w-28 lg:w-40 hover:text-gray-600" id="walletId"><c:out value="${user.wallet}"/></span>
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
            <span class="text-lg font-light text-gray-400 break-words w-96 lg:w-[36rem] xl:w-[44rem] 2xl:w-[52rem]"><c:out value="${user.email}" /></span>
            <div class="flex flex-row items-center">
                <img class="h-9 w-9" src='<c:url value="/resources/filled_star.svg"/>' alt="filled_star">
                    <p class="text-lg font-bold text-gray-900 ml-1">${userScore}</p>
                <span class="w-1.5 h-1.5 mx-2 bg-gray-500 rounded-full dark:bg-gray-400"></span>
                <a href='<c:url value="/profile/${userId}?tab=reviews"/>' class="text-lg font-medium text-cyan-600 hover:text-cyan-800 hover:underline"><spring:message code="review.totalAmount" arguments="${reviewAmount}"/></a>
            </div>
        </div>
    </div>

    <!-- Tabs -->
    <div class="flex border-b border-gray-200">
        <ul class="flex flex-wrap flex-grow justify-evenly items-center font-medium text-lg text-center text-gray-500">
            <c:set var="activeClasses" value="border-b-2 border-cyan-600 text-cyan-600 active"/>
            <c:set var="inactiveClasses" value="border-transparent hover:text-gray-700"/>
            <c:choose>
                <c:when test="${tabName == 'inventory'}">
                    <c:set var="inventoryClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="inventoryClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tabName == 'selling'}">
                    <c:set var="sellingClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="sellingClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tabName == 'buyOrders'}">
                    <c:set var="buyOrderClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="buyOrderClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tabName == 'favorited'}">
                    <c:set var="favoritedClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="favoritedClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tabName == 'history'}">
                    <c:set var="historyClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="historyClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tabName == 'reviews'}">
                    <c:set var="reviewsClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="reviewsClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>

            <!-- Inventory -->
            <li class="${inventoryClasses}">
                <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                    <form:button type="submit" path="tab" name="tab" value="inventory" class="flex">
                        <svg class="mr-2 w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
                        </svg>
                        <spring:message code="profile.inventory"/>
                    </form:button>
                    <form:hidden path="sort" value="${sortValue}"/>
                    <form:hidden path="page" value="1"/>
                </form:form>
            </li>
            <!-- Selling -->
            <li class="${sellingClasses}">
                <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                    <form:button type="submit" path="tab" name="tab" value="selling" class="flex">
                        <svg class="mr-2 h-6 w-6" viewBox="0 0 512 512">
                            <circle cx="176" cy="416" r="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></circle>
                            <circle cx="400" cy="416" r="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></circle>
                            <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32" d="M48 80h64l48 272h256"></path>
                            <path d="M160 288h249.44a8 8 0 007.85-6.43l28.8-144a8 8 0 00-7.85-9.57H128" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></path>
                        </svg>
                        <spring:message code="profile.selling"/>
                    </form:button>
                    <form:hidden path="sort" value="${sortValue}"/>
                    <form:hidden path="page" value="1"/>
                </form:form>
            </li>
            <c:if test="${isOwner == true}">
                <!-- Buy orders -->
                <li class="${buyOrderClasses}">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:button type="submit" path="tab" name="tab" value="buyOrders" class="flex">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mr-2 h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                            </svg>
                            <spring:message code="profile.bidded"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="1"/>
                    </form:form>
                </li>
                <!-- Favorited -->
                <li class="${favoritedClasses}">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:button type="submit" path="tab" name="tab" value="favorited" class="flex">
                            <svg class="mr-2 h-6 w-6" width="30" height="30" viewBox="0 0 30 30" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M26.0502 5.76252C25.4117 5.12378 24.6537 4.61708 23.8193 4.27138C22.985 3.92568 22.0908 3.74774 21.1877 3.74774C20.2845 3.74774 19.3903 3.92568 18.556 4.27138C17.7216 4.61708 16.9636 5.12378 16.3252 5.76252L15.0002 7.08752L13.6751 5.76252C12.3855 4.47291 10.6364 3.74841 8.81265 3.74841C6.98886 3.74841 5.23976 4.47291 3.95015 5.76252C2.66053 7.05214 1.93604 8.80123 1.93604 10.625C1.93604 12.4488 2.66053 14.1979 3.95015 15.4875L5.27515 16.8125L15.0002 26.5375L24.7252 16.8125L26.0502 15.4875C26.6889 14.8491 27.1956 14.091 27.5413 13.2567C27.887 12.4224 28.0649 11.5281 28.0649 10.625C28.0649 9.72191 27.887 8.82765 27.5413 7.99333C27.1956 7.15901 26.6889 6.40097 26.0502 5.76252V5.76252Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                            </svg>
                            <spring:message code="profile.favorited"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="1"/>
                    </form:form>
                </li>
                <!-- History -->
                <li class="${historyClasses}">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:button type="submit" path="tab" name="tab" value="history" class="flex">
                            <svg class="mr-2 h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            <spring:message code="profile.history"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="1"/>
                    </form:form>
                </li>
            </c:if>
            <!-- Reviews -->
            <li class="${reviewsClasses}">
                <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                    <form:button type="submit" path="tab" name="tab" value="reviews" class="flex">
                        <svg class="h-6 w-6 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"></path>
                        </svg>
                        <spring:message code="profile.reviews"/>
                    </form:button>
                    <form:hidden path="sort" value="${sortValue}"/>
                    <form:hidden path="page" value="1"/>
                </form:form>
            </li>
        </ul>
    </div>

    <!-- Paginator and dropdown -->
    <div class="flex flex-row items-center justify-between">
        <!-- Paginator -->
        <div class="flex flex-row text-xl items-start">
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <form:hidden path="tab" value="${tabName}"/>
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
                <form:hidden path="tab" value="${tabName}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <form:input path="page" type="number" min="1" max="${pages}" value="${currentPage}"
                            class="w-10 border-2 border-slate-300 rounded-lg bg-slate-300 px-1 mx-1 py-0.5" />
                <span><spring:message code="profile.of" arguments="${pages}"/></span>
            </form:form>
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <form:hidden path="tab" value="${tabName}"/>
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
        <c:if test="${tabName != 'history' && tabName != 'reviews'}">
            <div class="flex flex-row text-2xl">
                <button id="sortDropdownDefault" data-dropdown-toggle="sortDropdown" class="border border-slate-400 font-medium rounded-lg text-sm px-4 py-2.5 text-center inline-flex items-center" type="button">
                    <c:out value="${sortName}" />
                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>
                <div id="sortDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-48">
                    <form:form class="mb-0" modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:hidden path="tab" value="${tabName}"/>
                        <form:hidden path="page" value="1" />
                        <ul class="py-1 text-sm text-gray-700" aria-labelledby="sortDropdownDefault">
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="name" class="block px-4 py-2 hover:bg-gray-100 flex w-full" ><spring:message code="explore.name"/></form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceAsc" class="block px-4 py-2 hover:bg-gray-100 flex w-full" ><spring:message code="explore.PriceAs"/></form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceDsc" class="block px-4 py-2 hover:bg-gray-100 flex w-full" ><spring:message code="explore.PriceDe"/></form:button>
                            </li>
                        </ul>
                    </form:form>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Objects (NFTs, history or review cards) -->
    <c:choose>
        <c:when test="${tabName != 'history' && tabName != 'buyOrders' && tabName != 'reviews'}">
            <div class="flex flex-wrap justify-center gap-8">
                <c:forEach items="${publications}" var="publication">
                    <c:if test="${publication.nft.sellOrder != null}">
                        <c:set value="${publication.nft.sellOrder.price}" var="sellPrice" />
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
        </c:when>
        <c:when test="${tabName == 'buyOrders'}">
            <div class="flex flex-col gap-2 w-3/4 max-w-4xl self-center">
                <c:forEach items="${buyOrderItems}" var="item">
                    <jsp:include page="../components/BuyOrderItem.jsp">
                        <jsp:param name="nftName" value="${item.offeredFor.nft.nftName}"/>
                        <jsp:param name="nftId" value="${item.offeredFor.nft.id}"/>
                        <jsp:param name="nftImg" value="${item.offeredFor.nft.idImage}"/>
                        <jsp:param name="price" value="${item.amount}"/>
                    </jsp:include>
                </c:forEach>
                <c:if test="${buyOrderItemsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="bidded.noBidsPlaced"/></span>
                </c:if>
            </div>
        </c:when>
        <c:when test="${tabName == 'history'}">
            <div class="flex flex-col gap-2 w-3/4 max-w-4xl self-center">
                <c:forEach items="${historyItems}" var="item">
                    <jsp:include page="../components/HistoryItem.jsp">
                        <jsp:param name="nftName" value="${item.nftsByIdNft.nftName}"/>
                        <jsp:param name="nftId" value="${item.nftsByIdNft.id}"/>
                        <jsp:param name="nftImg" value="${item.nftsByIdNft.idImage}"/>
                        <jsp:param name="price" value="${item.price}"/>
                        <jsp:param name="date" value="${item.buyDate}"/>
                        <jsp:param name="sellerUsername" value="${item.seller.username}"/>
                        <jsp:param name="sellerId" value="${item.seller.id}"/>
                        <jsp:param name="buyerUsername" value="${item.buyer.username}"/>
                        <jsp:param name="buyerId" value="${item.buyer.id}"/>
                        <jsp:param name="sold" value="${user.id == item.seller.id}"/>
                    </jsp:include>
                </c:forEach>
                <c:if test="${historyItemsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="profile.noActivity"/></span>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Reviews -->
            <div class="flex flex-row gap-6">
                <!-- Mean score -->
                <div class="border rounded-md h-fit p-5">
                    <h2 class="text-lg">
                        <spring:message code="profile.ratings"/>
                    </h2>
                    <h3 class="mb-6 text-gray-400"><spring:message code="profile.totalReviews" arguments="${reviewAmount}"/></h3>
                    <div class="flex flex-col">
                        <div class="flex flex-row items-center w-64 xl:w-80 2xl:w-96">
                            <div class="flex flex-col gap-3">
                                <c:forEach var="i" begin="0" end="3">
                                    <span class="text-sm font-medium text-blue-700">
                                        <spring:message code="profile.stars" arguments="${5-i}"/>
                                    </span>
                                </c:forEach>
                                <span class="text-sm font-medium text-blue-700">
                                    <spring:message code="profile.star" arguments="1"/>
                                </span>
                            </div>
                            <div class="flex flex-col grow gap-3">
                                <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                                    <div class="h-5 bg-amber-500 rounded" style="width: ${percentageStars5}%; height: 99%"></div>
                                </div>
                                <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                                    <div class="h-5 bg-amber-500 rounded" style="width: ${percentageStars4}%; height: 99%"></div>
                                </div>
                                <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                                    <div class="h-5 bg-amber-500 rounded" style="width: ${percentageStars3}%; height: 99%"></div>
                                </div>
                                <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                                    <div class="h-5 bg-amber-500 rounded" style="width: ${percentageStars2}%; height: 99%"></div>
                                </div>
                                <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                                    <div class="h-5 bg-amber-500 rounded" style="width: ${percentageStars1}%; height: 99%"></div>
                                </div>
                            </div>
                            <div class="flex flex-col gap-3">
                                <span class="text-sm font-medium text-blue-700"><c:out value="${percentageStars5}"/>%</span>
                                <span class="text-sm font-medium text-blue-700"><c:out value="${percentageStars4}"/>%</span>
                                <span class="text-sm font-medium text-blue-700"><c:out value="${percentageStars3}"/>%</span>
                                <span class="text-sm font-medium text-blue-700"><c:out value="${percentageStars2}"/>%</span>
                                <span class="text-sm font-medium text-blue-700"><c:out value="${percentageStars1}"/>%</span>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Detailed reviews -->
                <div class="flex flex-col grow divide-y gap-4 mx-8">
                    <div class="flex flex-row items-center justify-between">
                        <h2 class="text-2xl"><spring:message code="profile.reviews"/></h2>
                        <c:if test="${!isOwner && !hasReviewByUser}">
                            <a href="<c:url value='/review/${user.id}/create'/>">
                                <button type="button" class="shadow-md px-6 py-2.5 rounded-md transition duration-300 bg-cyan-600 hover:bg-cyan-800 text-white hover:shadow-xl">
                                    <spring:message code="profile.addReview"/>
                                </button>
                            </a>
                        </c:if>
                    </div>
                    <c:choose>
                        <c:when test="${reviewAmount == 0}">
                            <span class="pt-5 text-center"><spring:message code="profile.noReviews"/></span>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${reviews}" var="review">
                                <jsp:include page="../components/ReviewItem.jsp">
                                    <jsp:param name="reviewerName" value="${review.usersByIdReviewer.username}"/>
                                    <jsp:param name="reviewerId" value="${review.usersByIdReviewer.id}"/>
                                    <jsp:param name="revieweeId" value="${review.usersByIdReviewee.id}"/>
                                    <jsp:param name="title" value="${review.title}"/>
                                    <jsp:param name="comments" value="${review.comments}"/>
                                    <jsp:param name="score" value="${review.score}"/>
                                    <jsp:param name="reviewId" value="${review.id}"/>
                                    <jsp:param name="isAdmin" value="${isAdmin}"/>
                                    <jsp:param name="isReviewer" value="${currentUserId == review.usersByIdReviewer.id}"/>
                                    <jsp:param name="modalId" value="${review.id}"/>
                                </jsp:include>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script>
    <%@ include file="/js/profile.js" %>
</script>
</body>
</html>
