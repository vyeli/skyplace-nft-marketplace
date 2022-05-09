<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <img class="rounded-full h-40 w-40" src="<c:url value='/resources/profile_picture.png' />" alt="profile_icon"/>
        <!-- Profile info -->
        <div class="flex flex-col mt-5 md:ml-5 md:mt-0 items-start justify-center gap-3">
            <!-- Profile name, wallet and more options (aligned) -->
            <div class="flex flex-row items-center justify-between">
                <div class="flex flex-row">
                    <span class="text-4xl font-semibold"><c:out value="${user.username}" /></span>
                    <button class="flex flex-row items-center ml-5 lg:ml-10 px-2 border rounded-2xl" onclick="copyToClipboard()" data-tooltip-target="tooltip-dark" data-tooltip-placement="bottom" type="button" id="walletButton">
                        <img class="w-6 h-6" src="<c:url value='/resources/utility_icon.svg' />" alt="wallet_icon" />
                        <!--
                        <svg class="fill-cyan-700 rounded-full" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
                            <path fill="none" d="M0 0h24v24H0z"/>
                            <path d="M22 7h1v10h-1v3a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h18a1 1 0 0 1 1 1v3zm-2 10h-6a5 5 0 0 1 0-10h6V5H4v14h16v-2zm1-2V9h-7a3 3 0 0 0 0 6h7zm-7-4h3v2h-3v-2z"/>
                        </svg>
                        -->
                        <span class="text-xl ml-1 text-gray-400 font-semibold truncate w-28 lg:w-40 hover:text-gray-600" id="walletId">${user.wallet}</span>
                    </button>
                    <div id="tooltip-dark" role="tooltip" class="inline-block absolute invisible z-10 py-2 px-3 text-sm font-medium text-white bg-gray-900 rounded-lg shadow-sm opacity-0 tooltip">
                        <spring:message code="profile.copy"/>
                        <div class="tooltip-arrow" data-popper-arrow></div>
                    </div>
                </div>
                <!--
                <button class="border border-gray-200 rounded-lg p-0.5 text-gray-400">
                    <img class="w-9 h-9" src="<c:url value='/resources/more_options.svg' />" alt="options_icon"/>
                </button>
                -->
            </div>
            <span class="text-lg font-light text-gray-400"><c:out value="${user.email}" /></span>
            <!-- Rating -->
            <!--
            <div class="flex flex-row items-center mt-3">
                <svg class="w-9 h-9 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                </svg>
                <p class="text-xl font-bold text-gray-900 ml-1">4.95</p>
                <span class="w-1.5 h-1.5 mx-2 bg-gray-500 rounded-full dark:bg-gray-400"></span>
                <a href="#" class="text-xl font-medium text-cyan-600 underline hover:no-underline">17 Reviews</a>
            </div>
            -->
        </div>
    </div>

    <!-- Tabs -->
    <div class="flex border-b border-gray-200">
        <ul class="flex flex-wrap flex-grow justify-evenly items-center font-medium text-lg text-center text-gray-500">
            <!-- Inventory -->
            <c:set var="activeClasses" value="border-b-2 border-cyan-600 text-cyan-600 active"/>
            <c:set var="inactiveClasses" value="border-transparent hover:text-gray-600"/>
            <c:choose>
                <c:when test="${showInventory != null}">
                    <c:set var="inventoryClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="inventoryClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${showSelling != null}">
                    <c:set var="sellingClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="sellingClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${showFavorited != null}">
                    <c:set var="favoritedClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="favoritedClasses" value="${inactiveClasses}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${showHistory != null}">
                    <c:set var="historyClasses" value="${activeClasses}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="historyClasses" value="${inactiveClasses}"/>
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
                    <form:hidden path="page" value="${currentPage}"/>
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
                    <form:hidden path="page" value="${currentPage}"/>
                </form:form>
            </li>
            <!-- Favorited -->
            <c:if test="${isOwner == true}">
                <li class="${favoritedClasses}">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:button type="submit" path="tab" name="tab" value="favorited" class="flex">
                            <svg class="mr-2 h-6 w-6" width="30" height="30" viewBox="0 0 30 30" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M26.0502 5.76252C25.4117 5.12378 24.6537 4.61708 23.8193 4.27138C22.985 3.92568 22.0908 3.74774 21.1877 3.74774C20.2845 3.74774 19.3903 3.92568 18.556 4.27138C17.7216 4.61708 16.9636 5.12378 16.3252 5.76252L15.0002 7.08752L13.6751 5.76252C12.3855 4.47291 10.6364 3.74841 8.81265 3.74841C6.98886 3.74841 5.23976 4.47291 3.95015 5.76252C2.66053 7.05214 1.93604 8.80123 1.93604 10.625C1.93604 12.4488 2.66053 14.1979 3.95015 15.4875L5.27515 16.8125L15.0002 26.5375L24.7252 16.8125L26.0502 15.4875C26.6889 14.8491 27.1956 14.091 27.5413 13.2567C27.887 12.4224 28.0649 11.5281 28.0649 10.625C28.0649 9.72191 27.887 8.82765 27.5413 7.99333C27.1956 7.15901 26.6889 6.40097 26.0502 5.76252V5.76252Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                            </svg>
                            <spring:message code="profile.favorited"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="${currentPage}"/>
                    </form:form>
                </li>
                <li class="${historyClasses}">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:button type="submit" path="tab" name="tab" value="history" class="flex">
                            <svg class="mr-2 h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            <spring:message code="profile.history"/>
                        </form:button>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="page" value="${currentPage}"/>
                    </form:form>
                </li>
            </c:if>

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
                        <form:button type="submit" path="page" name="page" value="${currentPage-1}" class="text-cyan-400 cursor-pointer mr-2" >Previous</form:button>
                    </c:when>
                    <c:otherwise>
                        <span class="text-gray-400 mr-2 cursor-default">Previous</span>
                    </c:otherwise>
                </c:choose>
            </form:form>
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <form:hidden path="tab" value="${tabName}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <form:input path="page" type="number" min="1" max="${pages}" value="${currentPage}"
                            class="w-10 border-2 border-slate-300 rounded-lg bg-slate-300 px-1 mx-1 py-0.5" />
                <span> of ${pages}</span>
            </form:form>
            <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                <form:hidden path="tab" value="${tabName}"/>
                <form:hidden path="sort" value="${sortValue}"/>
                <c:choose>
                    <c:when test="${currentPage < pages}">
                        <form:button type="submit" path="page" name="page" value="${currentPage+1}" class="text-cyan-400 cursor-pointer ml-2" >Next</form:button>
                    </c:when>
                    <c:otherwise>
                        <span class="text-gray-400 ml-2 cursor-default">Next</span>
                    </c:otherwise>
                </c:choose>
            </form:form>
        </div>

        <!-- Dropdown menu -->
        <c:if test="${showHistory == null}">
            <div class="flex flex-row text-2xl">
                <button id="sortDropdownDefault" data-dropdown-toggle="sortDropdown" class="border border-slate-400 font-medium rounded-lg text-sm px-4 py-2.5 text-center inline-flex items-center" type="button">
                    <c:out value="${sortName}" />
                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>
                <div id="sortDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-48">
                    <form:form modelAttribute="profileFilter" action="${profilePath}" method="get">
                        <form:hidden path="tab" value="${tabName}"/>
                        <form:hidden path="page" value="${currentPage}" />
                        <ul class="py-1 text-sm text-gray-700" aria-labelledby="sortDropdownDefault">
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="name" class="block px-4 py-2 hover:bg-gray-100 flex w-full" >Name</form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceAsc" class="block px-4 py-2 hover:bg-gray-100 flex w-full" >Price Ascending</form:button>
                            </li>
                            <li>
                                <form:button type="submit" path="sort" name="sort" value="priceDsc" class="block px-4 py-2 hover:bg-gray-100 flex w-full" >Price Descending</form:button>
                            </li>
                        </ul>
                    </form:form>
                </div>
            </div>
        </c:if>
    </div>

    <!-- NFTs -->
    <c:choose>
        <c:when test="${showHistory == null}">
            <div class="flex flex-wrap justify-center gap-8">
                <c:forEach items="${publications}" var="publication">
                    <c:if test="${publication.nft.sellOrder != null}">
                        <c:set value="${publication.sellOrder.price}" var="sellPrice" />
                        <c:set value="${publication.sellOrder.category}" var="sellCategory" />
                    </c:if>
                    <jsp:include page="../components/Card.jsp">
                        <jsp:param name="name" value="${publication.nft.nftName}"/>
                        <jsp:param name="isFaved" value="${publication.isFaved}"/>
                        <jsp:param name="nftId" value="${publication.nft.nftId}" />
                        <jsp:param name="descr" value="${publication.nft.description}"/>
                        <jsp:param name="img" value="${publication.nft.idImage}"/>
                        <jsp:param name="onSale" value="${publication.nft.sellOrder != null}"/>
                        <jsp:param name="price" value="${sellPrice}" />
                        <jsp:param name="category" value="${sellCategory}"/>
                        <jsp:param name="sellerEmail" value="${publication.user.email}"/>
                        <jsp:param name="idProduct" value="${publication.nft.id}"/>
                        <jsp:param name="isFaved" value="false" />
                    </jsp:include>
                </c:forEach>
                <c:if test="${publicationsSize == 0}">
                    <span class="text-center text-slay-700"><spring:message code="profile.noNft"/></span>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="flex flex-col gap-2 w-3/4 max-w-4xl self-center">
                <c:forEach items="${historyItems}" var="item">
                    <jsp:include page="../components/HistoryItem.jsp">
                        <jsp:param name="nftName" value="${item.nft.nftName}"/>
                        <jsp:param name="nftId" value="${item.nft.id}"/>
                        <jsp:param name="nftImg" value="${item.nft.idImage}"/>
                        <jsp:param name="price" value="${item.price}"/>
                        <jsp:param name="date" value="${item.date}"/>
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
        </c:otherwise>
    </c:choose>
</div>
<script>
    <%@ include file="/js/profile.js" %>
</script>
</body>
</html>
