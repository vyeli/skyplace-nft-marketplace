<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>


<html>
<%@ include file="Head.jsp" %>
<body class=" font-body overflow-x-hidden">
<%@ include file="../components/navbar.jsp" %>
<main class="mt-12 mb-8">
    <section class="flex justify-around">
        <div class="container">
            <div class="flex flex-row ">
                <!-- Image and tabs (first div)-->
                <div class="flex-col max-w-[50%]">
                    <!--Image-->
                    <figure class="mb-8">
                        <img src="<c:url value="/images/${nft.id_image}" />"
                             alt="<c:out value="${nft.nft_name}" />" class="rounded-2xl">
                    </figure>
                    <!--TAGS-->
                    <div class="rounded-lg border-2">
                        <ul class="nav nav-tabs flex flex-col md:flex-row flex-wrap list-none border-b-0 pl-0 border-b"
                            id="tabs-tab" role="tablist">
                            <!--Details -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active hover:text-cyan-700 text-cyan-400 relative flex items-center whitespace-nowrap py-3 px-6"
                                        id="tabs-home-tab" data-bs-toggle="tab" data-bs-target="#tabs-home"
                                        type="button"
                                        role="tab" aria-controls="tabs-home" aria-selected="true">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M8 4h13v2H8V4zm-5-.5h3v3H3v-3zm0 7h3v3H3v-3zm0 7h3v3H3v-3zM8 11h13v2H8v-2zm0 7h13v2H8v-2z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium">Details</span>
                                </button>
                            </li>
                            <!-- Properties -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-jacarta-400 relative flex items-center whitespace-nowrap py-3 px-6"
                                        id="tabs-profile-tab" data-bs-target="#tabs-profile" data-bs-toggle="tab"
                                        type="button" role="tab" aria-controls="tabs-profile" aria-selected="false">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M6.17 18a3.001 3.001 0 0 1 5.66 0H22v2H11.83a3.001 3.001 0 0 1-5.66 0H2v-2h4.17zm6-7a3.001 3.001 0 0 1 5.66 0H22v2h-4.17a3.001 3.001 0 0 1-5.66 0H2v-2h10.17zm-6-7a3.001 3.001 0 0 1 5.66 0H22v2H11.83a3.001 3.001 0 0 1-5.66 0H2V4h4.17z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium">Properties</span>
                                </button>
                            </li>

                            <!-- Offers -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-cyan-400 relative flex items-center whitespace-nowrap py-3 px-6"
                                        id="tabs-messages-tab" data-bs-toggle="tab" data-bs-target="#tabs-messages"
                                        type="button" role="tab" aria-controls="tabs-messages" aria-selected="false">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M20 22H4a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1v18a1 1 0 0 1-1 1zm-1-2V4H5v16h14zM7 6h4v4H7V6zm0 6h10v2H7v-2zm0 4h10v2H7v-2zm6-9h4v2h-4V7z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium">Offers</span>
                                </button>
                            </li>
                        </ul>
                        <!-- TAB CONTENT-->
                        <div class="tab-content p-4 pt-6 bg-slate-50" id="tabs-tabContent">
                            <div class="tab-pane fade show active" id="tabs-home" role="tabpanel"
                                 aria-labelledby="tabs-home-tab">
                                <div class="flex flex-col gap-2">
                                    <div class="flex justify-between">
                                        <p>Contract address </p>
                                        <p class="w-3/5 break-words max-h-20 overflow-hidden text-right">
                                            <c:out value="${nft.contract_addr}" />
                                        </p>
                                    </div>
                                    <div class="flex justify-between">
                                        <p>Token ID</p>
                                        <p class="w-3/5 overflow-hidden text-right"><c:out value="${nft.nft_id}" /></p>
                                    </div>
                                    <c:if test="${sellorder != null}">
                                        <div class="flex justify-between">
                                            <p>Category</p>
                                            <p><c:out value="${sellorder.category}" /></p>
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
                                Tab 2 content
                            </div>
                            <div class="tab-pane fade" id="tabs-messages" role="tabpanel"
                                 aria-labelledby="tabs-profile-tab">
                                Tab 3 content
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Description (second div) -->
                <div class="md:w-3/5 md:basis-auto md:pl-8 lg:w-1/2 lg:pl-[3.75rem]">
                    <!-- Collection / Likes -->
                    <div class="mb-3 flex">
                        <!-- Collection -->
                        <div class="flex items-center">
                            <a href="collection" class="text-accent mr-2 underline decoration-cyan-700 font-bold">Collection: <c:out value="${nft.collection}" /></a>
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
                            <c:if test="${currentUser.id == owner.id}">
                                <button id="productButton" data-dropdown-toggle="productMenu" type="button" class="flex items-center space-x-1 rounded-r-xl border bg-white py-2 px-2">
                                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z"></path></svg>
                                </button>
                            </c:if>
                            <div id="productMenu" class="hidden z-10 w-44 bg-white flex flex-col flex-grow rounded text-gray-700 border border-gray-300 text-sm divide-y divide-gray-300 shadow">
                                <ul class="py-1" aria-labelledby="productButton">
                                    <c:if test="${currentUser.id == owner.id}">
                                        <c:if test="${sellorder == null}">
                                            <li class="z-20">
                                                <a href="<c:url value="/sell/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                    <span>Sell nft</span>
                                                </a>
                                            </li>
                                        </c:if>
                                        <c:if test="${sellorder != null}">
                                            <li class="z-20">
                                                <a href="<c:url value="/sell/update/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                    <span>Update sell</span>
                                                </a>
                                            </li>
                                            <li class="z-20">
                                                <c:url value="/sell/delete/${productId}" var="deleteSellPath" />
                                                <form:form action="${deleteSellPath}" method="post" class="mb-0">
                                                    <button type="submit" class="block flex flex-row items-center w-full justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                        <span>Delete sell</span>
                                                    </button>
                                                </form:form>
                                            </li>
                                            <li class="z-20">
                                                <a href="<c:url value="/sell/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                    <span>Confirm sell</span>
                                                </a>
                                            </li>
                                        </c:if>
                                        <li class="z-20">
                                            <a href="<c:url value="/sell/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                <span>Update nft</span>
                                            </a>
                                        </li>
                                        <li class="z-20">
                                            <a href="<c:url value="/sell/${productId}" />" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                                                <span>Delete nft</span>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!--Name and ID-->
                    <h1 class="font-display text-jacarta-700 mb-2 text-4xl font-semibold"><c:out value="${nft.nft_name}" /><span>#<c:out value="${nft.nft_id}" /></span></h1>
                    <!-- Owner -->
                    <div class="mb-4 flex">
                        <div class="flex">
                            <span class="text-jacarta-400 block text-sm">Owned by </span>
                            <a href="<c:url value="/profile/${owner.id}" />" class="text-accent block">
                                <span class="text-sm font-bold pl-2"><c:out value="${owner.email}" /></span>
                            </a>
                        </div>
                    </div>
                    <div class="mb-8 flex items-center space-x-4 whitespace-nowrap">
                        <div class="flex text-xl items-center">
                            <!--ETH SVG-->
                            <span class="-ml-1" data-tippy-content="ETH">
                              <svg x="0" y="0" viewBox="0 0 1920 1920"
                                   xml:space="preserve" class="mr-1 h-8 w-8">
                                <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                              </svg>
                            </span>
                            <c:choose>
                                <c:when test="${sellorder != null}">
                                    <span class="text-green font-bold tracking-tight">${sellorder.price}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-green font-bold tracking-tight">Not for sale</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class=" border-gray-200 rounded-2xl border bg-white pb-4 flex-col justify-between mb-8 bg-slate-50">
                        <div class="flex text-xl px-4  py-2 border-b bg-white rounded-t-2xl">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7"></path></svg>
                            <span class="pl-2 font-semibold">Description</span>
                        </div>
                        <p class="mb-4 px-4 pt-4">
                            <c:out value="${nft.description}" />
                        </p>
                    </div>
                    <!-- Bid -->
                    <c:if test="${sellorder != null && (currentUser == null || owner.email != currentUser.email)}">
                    <div class=" border-gray-200 rounded-2xl border bg-white pb-4 flex-col justify-between mb-8 bg-slate-50">
                        <div class="flex text-xl px-4  py-2 border-b bg-white rounded-t-2xl">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"></path></svg>
                            <span class="pl-2 font-semibold">Buy</span>
                        </div>
                        <div class="mb-4 px-4 pt-4">
                            <c:url value="/product/${nft.id}" var="postPath" />
                            <form:form modelAttribute="buyNftForm" action="${postPath}" method="post">
                                <label class="flex mb-4 items-center">
                                    <span class="text-lg pr-2">Offer</span>
                                    <svg x="0" y="0" viewBox="0 0 1920 1920" xml:space="preserve" class="mr-1 h-6 w-6">
                                    <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                        <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                        <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                        <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                        <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                                    </svg>
                                    <form:input type="number" value="${sellorder.price}" class="rounded-lg border-slate-300" min="0" step="0.000000000000000001" path="price" />
                                </label>
                                <input type="submit" class="bg-cyan-600 shadow-accent-volume hover:bg-cyan-700 inline-block w-1/2 rounded-lg py-3 px-8 text-center font-semibold text-white transition-all" value="Make offer">
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
</body>
</html>
