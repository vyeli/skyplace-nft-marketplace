<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<nav class="flex items-center w-full justify-around h-16 lg:h-20 shadow-md lg:shadow-none">
    <a href="<c:url value="/" />">
    <div class="cursor-pointer flex flex-row flex-start gap-2">
        <img src="<c:url value="/resources/logo.svg" />" alt="logo" class="w-10 md:w-14" />
        <h1 class="font-semibold text-xl md:text-2xl">Skyplace</h1>
    </div>
    </a>
    <div class="w-2/5 relative flex flex-row flex-start rounded border border-gray-300 text-cyan-800">
        <c:url value="/explore" var="explorePath"/>
        <form action="${explorePath}" method="get" class="w-full flex m-0 focus-within:shadow-[0px_3px_20px_4px_rgba(0,0,0,0.05)]">
            <select name="searchFor" class="border-0 border-r-[1px] pl-2 pr-7 border-gray-300 rounded-l cursor-pointer outline-none focus:outline-none">
                <c:choose>
                    <c:when test="${param.searchFor != null && param.searchFor == 'collection'}">
                        <option value="nft" class="text-slate-500">Nft</option>
                        <option value="collection" class="text-slate-500" selected><spring:message code="navbar.collection"/></option>
                    </c:when>
                    <c:otherwise>
                        <option value="nft" class="text-slate-500" selected>Nft</option>
                        <option value="collection" class="text-slate-500"><spring:message code="navbar.collection"/></option>
                    </c:otherwise>
                </c:choose>
            </select>
            <input name="search" class="pl-2 outline-none w-full border-none p-0 focus:border-none focus:ring-0" type="text"
                    placeholder="<spring:message code="navbar.search"/>" value="${param.search}" />
            <button type="submit" class="border-l cursor-pointer border-gray-300 px-2 flex items-center">
                <img src="<c:url value="/resources/icsearch.svg" />" alt="icsearch" class="w-8">
            </button>
        </form>
        <!-- Search icon -->
    </div>
    <div class="hidden sm:flex flex-row justify-end items-center gap-12 pr-4 text-lg font-normal">
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/explore" />"><spring:message code="navbar.explore"/></a>
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/create" />"><spring:message code="navbar.create"/></a>
        <button id="profileButton" data-dropdown-toggle="profileMenu" type="button">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
        </button>
        <!-- Dropdown menu -->
        <div id="profileMenu" class="hidden z-10 w-44 bg-white flex flex-col flex-grow rounded text-gray-700 border border-gray-300 text-sm divide-y divide-gray-300 shadow">
            <ul class="py-1" aria-labelledby="profileButton">
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <a href="<c:url value="/current-user"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-6 w-6" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path>
                            </svg>
                            <span><spring:message code="navbar.profile"/></span>
                        </a>
                    </li>
                    <li class="pb-1">
                        <a href="<c:url value="/current-user?tab=favorited"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-6 w-6" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clip-rule="evenodd"></path>
                            </svg>
                            <span><spring:message code="navbar.favorites"/></span>
                        </a>
                    </li>
                    <li class="pt-1 border-t border-gray-300">
                        <a href="<c:url value="/logout"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-6 w-6" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"></path>
                            </svg>
                            <span><spring:message code="navbar.logout"/></span>
                        </a>
                    </li>
                </sec:authorize>
                <sec:authorize access="!isAuthenticated()">
                    <li class="z-20">
                        <a href="<c:url value="/login"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <span><spring:message code="navbar.login"/></span>
                        </a>
                    </li>
                    <li class="z-20">
                        <a href="<c:url value="/register"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <span><spring:message code="navbar.singup"/></span>
                        </a>
                    </li>
                </sec:authorize>
            </ul>
        </div>
<%--        DROPDOWN FOR SELECT LENGUAGE --%>
<%--        <div class="flex justify-center">--%>
<%--            <div>--%>
<%--                <div class="dropdown relative">--%>
<%--                    <a  class="dropdown-toggle px-4 py-2 bg-cyan-600 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-cyan-700 hover:shadow-lg focus:bg-cyan-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-cyan-800 active:shadow-lg active:text-white transition duration-150 ease-in-out flex items-center whitespace-nowrap"--%>
<%--                        href="#"--%>
<%--                        type="button"--%>
<%--                        id="dropdownMenuButton2"--%>
<%--                        data-bs-toggle="dropdown"--%>
<%--                        aria-expanded="false">--%>
<%--                        <svg width="24px" height="24px" viewBox="0 0 24 24" version="1.1" xmlns="http://www.w3.org/2000/svg">--%>
<%--                            <g id="🔍-Product-Icons" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">--%>
<%--                                <g id="ic_fluent_globe_24_regular" fill="#ffffff" fill-rule="nonzero">--%>
<%--                                    <path d="M12.0000002,1.99896738 C17.523704,1.99896738 22.0015507,6.47681407 22.0015507,12.0005179 C22.0015507,17.5242217 17.523704,22.0020684 12.0000002,22.0020684 C6.47629639,22.0020684 1.99844971,17.5242217 1.99844971,12.0005179 C1.99844971,6.47681407 6.47629639,1.99896738 12.0000002,1.99896738 Z M14.9389045,16.5002571 L9.06109593,16.5002571 C9.71313678,18.9143799 10.8464883,20.5020684 12.0000002,20.5020684 C13.1535121,20.5020684 14.2868636,18.9143799 14.9389045,16.5002571 Z M7.50830976,16.5008106 L4.78582644,16.5006803 C5.744167,18.0337454 7.17761035,19.2393418 8.87999355,19.9113065 C8.35771536,19.0914835 7.92672586,18.0651949 7.60972411,16.8958162 L7.50830976,16.5008106 L7.50830976,16.5008106 Z M19.214174,16.5006803 L16.4916907,16.5008106 C16.167923,17.8345819 15.700316,19.000392 15.1189501,19.9113639 C16.7159251,19.2813431 18.0755983,18.1823607 19.0289676,16.7842422 L19.214174,16.5006803 L19.214174,16.5006803 Z M7.09342418,9.9998686 L3.73581796,9.9998686 L3.73581796,9.9998686 L3.73106043,10.0170734 C3.57897914,10.6534279 3.49844971,11.3175685 3.49844971,12.0005179 C3.49844971,13.0566718 3.69103899,14.0678442 4.04301473,15.000832 L7.21617552,15.0004667 C7.07387589,14.0513637 6.99844971,13.0431468 6.99844971,12.0005179 C6.99844971,11.3165055 7.03091285,10.6473039 7.09342418,9.9998686 Z M15.3969276,9.99993255 L8.60307281,9.99993255 C8.53505119,10.640197 8.49844971,11.3099099 8.49844971,12.0005179 C8.49844971,13.0600224 8.5845969,14.070347 8.73818201,15.000607 L15.2618184,15.000607 C15.4154035,14.070347 15.5015507,13.0600224 15.5015507,12.0005179 C15.5015507,11.3099099 15.4649492,10.640197 15.3969276,9.99993255 Z M20.2646076,9.999033 L16.9065762,9.99988793 C16.9690876,10.6473039 17.0015507,11.3165055 17.0015507,12.0005179 C17.0015507,13.0431468 16.9261245,14.0513637 16.7838249,15.0004667 L19.9569857,15.000832 C20.3089614,14.0678442 20.5015507,13.0566718 20.5015507,12.0005179 C20.5015507,11.3111121 20.4194915,10.6408723 20.2646076,9.999033 Z M8.88105029,4.0896719 L8.85814489,4.09838857 C6.81083065,4.91309834 5.15481653,6.500408 4.25014894,8.50027116 L7.29826946,8.50063757 C7.61162388,6.74784533 8.15846945,5.22192073 8.88105029,4.0896719 Z M12.0000002,3.49896738 L11.8843481,3.50426666 C10.6189068,3.62002448 9.39642826,5.62198962 8.82871306,8.50021862 L15.1712874,8.50021862 C14.6051632,5.63005613 13.3879407,3.63125276 12.1262908,3.50528435 L12.0000002,3.49896738 Z M15.1200069,4.08972931 L15.2268438,4.26382329 C15.8960528,5.37628985 16.4041675,6.83617446 16.701731,8.50063757 L19.7498515,8.50027116 C18.8852005,6.58886886 17.3342021,5.05432991 15.4112068,4.21100469 L15.1200069,4.08972931 Z" id="🎨-Color"></path>--%>
<%--                                </g>--%>
<%--                            </g>--%>
<%--                        </svg>--%>
<%--                        <svg aria-hidden="true" focusable="false" data-prefix="fas" data-icon="caret-down" class="w-2 ml-2" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512">--%>
<%--                            <path fill="currentColor"--%>
<%--                            d="M31.3 192h257.3c17.8 0 26.7 21.5 14.1 34.1L174.1 354.8c-7.8 7.8-20.5 7.8-28.3 0L17.2 226.1C4.6 213.5 13.5 192 31.3 192z"></path>--%>
<%--                        </svg>--%>
<%--                    </a>--%>
<%--                    <ul class="dropdown-menu min-w-max absolute hidden bg-white text-base z-50 float-left py-2 list-none text-left rounded-lg shadow-lg mt-1 hidden m-0 bg-clip-padding border-none"--%>
<%--                        aria-labelledby="dropdownMenuButton2">--%>
<%--                        <h6 class="text-gray-500 font-semibold text-sm py-2 px-4 block w-full whitespace-nowrap bg-transparent">--%>
<%--                            <spring:message code="navbar.languages"/>--%>
<%--                        </h6>--%>
<%--                        <li>--%>
<%--                            <a  class="dropdown-item text-sm py-2 px-4 font-normal block w-full whitespace-nowrap bg-transparent text-gray-700 hover:bg-gray-100"--%>
<%--                                href="<c:url value="?lang=en"/>">English</a>--%>
<%--                        </li>--%>
<%--                        <li>--%>
<%--                            <a  class="dropdown-item text-sm py-2 px-4 font-normal block w-full whitespace-nowrap bg-transparent text-gray-700 hover:bg-gray-100"--%>
<%--                                href="<c:url value="?lang=es"/>"><spring:message code="navbar.spanish"/></a>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
    </div>
<%--    <span>Current Locale : ${pageContext.response.locale}</span>--%>
</nav>