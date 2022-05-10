<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE HTML>
<html>
<head>
    <title><spring:message code="explore.title"/></title>
</head>
<body>
<div class="min-h-screen flex flex-col">
    <%@ include file="../components/navbar.jsp" %>
    <div class="grow flex pt-16 max-h-[calc(100vh-5rem)] divide-x divide-slate-300">
        <div id="smallFilterBar" class="flex hidden mx-2">
            <svg class="w-10 h-8 cursor-pointer rounded-full px-2 hover:bg-slate-200" id="openFilter" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
        </div>
        <div class="flex flex-col w-72 min-w-[250px] items-center" id="filterBar">
            <span class="text-4xl"><c:out value="${category}"/></span>
            <span><c:out value="${publicationsAmount}" /> <spring:message code="explore.results"/></span>

            <div class="grow w-full overflow-y-scroll mt-2">
                <div class="py-4 flex flex-col w-full">
                    <div class="flex items-center justify-between pb-4 px-4">
                        <div class="flex items-center">
                            <svg class="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"></path></svg>
                            <span class="text-2xl"><spring:message code="explore.filter"/></span>
                        </div>
                        <svg class="w-10 h-8 cursor-pointer rounded-full px-2 hover:bg-slate-200" id="closeFilter" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M7.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l2.293 2.293a1 1 0 010 1.414z" clip-rule="evenodd"></path></svg>
                    </div>

                    <c:url value="/explore" var="explorePath"/>
                    <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">
                        <form:hidden path="page" value="1" />
                        <form:hidden path="sort" value="${sortValue}" />
                        <form:hidden path="search" value="${searchValue}"/>
                        <form:hidden path="searchFor" value="${searchForValue}"/>
                        <div id="accordionStatus" data-accordion="open" data-active-classes="text-black bg-white">
                            <h2 id="accordionStatusHeader">
                                <button type="button" class="flex justify-between items-center p-5 w-full font-medium text-left border border-x-0 border-gray-200" data-accordion-target="#accordionStatusBody" aria-expanded="true" aria-controls="accordionStatusBody" >
                                    <span><spring:message code="explore.status"/></span>
                                    <svg data-accordion-icon class="w-6 h-6 rotate-180 shrink-0" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
                                </button>
                            </h2>
                            <div id="accordionStatusBody" class="hidden" aria-labelledby="accordionStatusHeader">
                                <div class="px-5 py-2 space-y-2 flex flex-col">
                                    <div>
                                        <form:checkbox class="w-5 h-5 border-gray-300 rounded mr-2 cursor-pointer" path="status" value="onSale"/>
                                        <form:label path="status">
                                            <spring:message code="explore.onSale"/>
                                        </form:label>
                                    </div>
                                    <div>
                                        <form:checkbox class="w-5 h-5 border-gray-300 rounded mr-2 cursor-pointer" path="status" value="notSale"/>
                                        <form:label path="status">
                                            <spring:message code="explore.NotOnSale"/>
                                        </form:label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="accordionCategory" data-accordion="open" data-active-classes="text-black bg-white">
                            <h2 id="accordionCategoryHeader">
                                <button type="button" class="flex justify-between items-center p-5 w-full font-medium text-left border border-x-0 border-gray-200" data-accordion-target="#accordionCategoryBody" aria-expanded="true" aria-controls="accordionCategoryBody" >
                                    <span><spring:message code="explore.category"/></span>
                                    <svg data-accordion-icon class="w-6 h-6 rotate-180 shrink-0" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
                                </button>
                            </h2>
                            <div id="accordionCategoryBody" class="hidden" aria-labelledby="accordionCategoryHeader">
                                <div class="px-5 py-2 space-y-2 flex flex-col">
                                    <form:checkboxes class="w-5 h-5 border-gray-300 rounded mr-2 cursor-pointer" path="category" items="${categories}" />
                                </div>
                            </div>
                        </div>

                        <div id="accordionChain" data-accordion="open" data-active-classes="text-black bg-white">
                            <h2 id="accordionChainHeader">
                                <button type="button" class="flex justify-between items-center p-5 w-full font-medium text-left border border-x-0 border-gray-200" data-accordion-target="#accordionChainBody" aria-expanded="true" aria-controls="accordionChainBody" >
                                    <span>Chain</span>
                                    <svg data-accordion-icon class="w-6 h-6 rotate-180 shrink-0" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
                                </button>
                            </h2>
                            <div id="accordionChainBody" class="hidden" aria-labelledby="accordionChainHeader">
                                <div class="px-5 py-2 space-y-2 flex flex-col">
                                    <form:checkboxes class="w-5 h-5 border-gray-300 rounded mr-2 cursor-pointer" path="chain" items="${chains}" />
                                </div>
                            </div>
                        </div>

                        <div id="accordionPrice" data-accordion="open" data-active-classes="text-black bg-white">
                            <h2 id="accordionPriceHeader">
                                <button type="button" class="flex justify-between items-center p-5 w-full font-medium text-left border border-x-0 border-gray-200" data-accordion-target="#accordionPriceBody" aria-expanded="true" aria-controls="accordionPriceBody" >
                                    <span><spring:message code="explore.price"/></span>
                                    <svg data-accordion-icon class="w-6 h-6 rotate-180 shrink-0" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
                                </button>
                            </h2>
                            <div id="accordionPriceBody" class="hidden flex items-end" aria-labelledby="accordionChainHeader">
                                <div class="relative z-0  px-2 w-full group">
                                    <form:label path="minPrice" class="font-mono font-bold w-1/3 text-[11px] text-gray-300 bg-white relative px-1  top-2 left-3 w-auto group-focus-within:text-black ">
                                        Min
                                    </form:label>
                                    <form:input type="number" min="0" class="h-8 text-10  bg-gray-50 border py-55-rem border-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="Min"  path="minPrice"/>
                                </div>
                                <span class="pb-1"><spring:message code="explore.to"/></span>
                                <div class="relative z-0  px-2 w-full group">
                                    <form:label path="maxPrice" class="font-mono font-bold w-1/3 text-[11px]  text-gray-300  bg-white relative px-1  top-2 left-3 w-auto group-focus-within:text-black ">
                                        Max
                                    </form:label>
                                    <form:input type="number" min="0" class="h-8 text-10  bg-gray-50 border py-55-rem border-gray-300 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" placeholder="Min"  path="maxPrice"/>
                                </div>
                            </div>
                        </div>


                        <input type="submit" value="Apply" class="rounded-lg flex px-4 py-1 mx-auto mt-4 cursor-pointer text-white bg-cyan-600 hover:bg-cyan-700" />


                    </form:form>
                </div>
            </div>
        </div>

        <div class="w-[80%] min-w-[500px] grow flex flex-col">
            <!-- header -->

            <div class="flex justify-between h-16 mb-2 px-8">
            <!-- pages -->
                <div class="flex text-xl items-start pt-4">
                    <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">
                        <form:hidden path="search" value="${searchValue}"/>
                        <form:hidden path="searchFor" value="${searchForValue}"/>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="minPrice" value="${minPriceValue}"/>
                        <form:hidden path="maxPrice" value="${maxPriceValue}"/>
                        <form:hidden path="category" value="${categoryValue}"/>
                        <form:hidden path="chain" value="${chainValue}"/>
                        <form:hidden path="status" value="${statusValue}"/>
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <form:button type="submit" path="page" name="page" value="${currentPage-1}" class="text-cyan-400 cursor-pointer mr-2" ><spring:message code="explore.previous"/></form:button>
                            </c:when>
                            <c:otherwise>
                                <span class=" text-gray-400 cursor-default mr-2"><spring:message code="explore.previous"/></span>
                            </c:otherwise>
                        </c:choose>
                    </form:form>
                    <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">
                        <form:hidden path="search" value="${searchValue}"/>
                        <form:hidden path="searchFor" value="${searchForValue}"/>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="minPrice" value="${minPriceValue}"/>
                        <form:hidden path="maxPrice" value="${maxPriceValue}"/>
                        <form:hidden path="category" value="${categoryValue}"/>
                        <form:hidden path="chain" value="${chainValue}"/>
                        <form:hidden path="status" value="${statusValue}"/>
                        <form:label path="page">
                            <form:input path="page" type="number" min="1" max="${pages}" value="${currentPage}"
                                class="w-10 border-2 border-slate-300 rounded-lg bg-slate-300 px-1 mx-1 py-0.5" />
                        </form:label>
                        <span> <spring:message code="explore.of"/> ${pages}</span>
                    </form:form>
                    <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">
                        <form:hidden path="search" value="${searchValue}"/>
                        <form:hidden path="searchFor" value="${searchForValue}"/>
                        <form:hidden path="sort" value="${sortValue}"/>
                        <form:hidden path="minPrice" value="${minPriceValue}"/>
                        <form:hidden path="maxPrice" value="${maxPriceValue}"/>
                        <form:hidden path="category" value="${categoryValue}"/>
                        <form:hidden path="chain" value="${chainValue}"/>
                        <form:hidden path="status" value="${statusValue}"/>
                        <c:choose>
                            <c:when test="${currentPage < pages}">
                                <form:button type="submit" path="page" name="page" value="${currentPage+1}" class="text-cyan-400 cursor-pointer ml-2" ><spring:message code="explore.next"/></form:button>
                            </c:when>
                            <c:otherwise>
                                <span class=" text-gray-400 cursor-default ml-2"><spring:message code="explore.next"/></span>
                            </c:otherwise>
                        </c:choose>
                    </form:form>
                </div>
                <div class="flex text-2xl pt-4">
                    <button id="sortDropdownDefault" data-dropdown-toggle="sortDropdown" class="border border-slate-400 font-medium rounded-lg text-sm px-4 py-2.5 text-center inline-flex items-center" type="button"><c:out value="${sortName}" /><svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg></button>
                    <!-- Dropdown menu -->
                    <div id="sortDropdown" class="z-10 hidden bg-white divide-y divide-gray-100 rounded shadow w-44">
                        <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">
                            <form:hidden path="page" value="1" />
                            <form:hidden path="search" value="${searchValue}"/>
                            <form:hidden path="searchFor" value="${searchForValue}"/>
                            <form:hidden path="minPrice" value="${minPriceValue}"/>
                            <form:hidden path="maxPrice" value="${maxPriceValue}"/>
                            <form:hidden path="category" value="${categoryValue}"/>
                            <form:hidden path="chain" value="${chainValue}"/>
                            <form:hidden path="status" value="${statusValue}"/>
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
                                <li>
                                    <form:button type="submit" path="sort" name="sort" value="collection" class="block px-4 py-2 hover:bg-gray-100 flex w-full" ><spring:message code="explore.collection"/></form:button>
                                </li>
                            </ul>
                        </form:form>
                    </div>
                </div>
            </div>

            <div class="px-8 pb-8 flex flex-wrap gap-8 overflow-y-scroll justify-center">
                <c:if test="${publicationsAmount == 0}">
                    <span class="pt-4 text-2xl"><spring:message code="explore.noNfts"/></span>
                </c:if>
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
            </div>
        </div>
    </div>
</div>

<script>
    const params = new URLSearchParams(window.location.search)

    if(params.get("category") != null && params.get("category") !== "") {
        document.querySelector("input[value='onSale']").checked = true
    }

    for(const param of params.values()) {
        const paramList = param.split(",")
        for (const item of paramList) {
            document.querySelector("input[value='" + item + "']").checked = true
        }
    }

    document.getElementById("closeFilter").addEventListener('click', () => {
        document.getElementById("smallFilterBar").classList.remove("hidden");
        document.getElementById("filterBar").classList.add("hidden");
    })

    document.getElementById("openFilter").addEventListener('click', () => {
        document.getElementById("smallFilterBar").classList.add("hidden");
        document.getElementById("filterBar").classList.remove("hidden");
    })
</script>
</body>
</html>
