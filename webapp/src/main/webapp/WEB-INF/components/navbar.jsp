<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="flex items-center w-full justify-around h-16 lg:h-20 shadow-md lg:shadow-none">
    <a href="<c:url value="/" />">
    <div class="cursor-pointer flex flex-row flex-start gap-2">
        <img src="<c:url value="/resources/logo.svg" />" alt="logo" class="w-10 md:w-14" />
        <h1 class="font-semibold text-xl md:text-2xl">Skyplace</h1>
    </div>
    </a>
    <div class="w-2/5 relative flex flex-row flex-start p-2 rounded border border-gray-300 text-cyan-800">
        <c:url value="/explore" var="explorePath"/>
        <form action="${explorePath}" method="get" class="w-full flex m-0">
            <img src="<c:url value='/resources/icsearch.svg' />" alt="icsearch" class="w-5" />
        <input name="search" class="pl-2 outline-none w-full border-none p-0 focus:border-none focus:ring-0" type="text"
            placeholder="Search items, collections and accounts" value="${param.search}" />
        </form>
        <!-- Search icon -->
    </div>
    <div class="hidden sm:flex flex-row justify-end items-center gap-12 pr-4 text-lg font-normal">
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/explore" />">Explore</a>
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/create" />">Create</a>
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
                            <span>My profile</span>
                        </a>
                    </li>
                    <li class="pb-1">
                        <a href="<c:url value="/current-user?tab=favorited"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-6 w-6" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.829a4 4 0 010-5.656z" clip-rule="evenodd"></path>
                            </svg>
                            <span>Favorites</span>
                        </a>
                    </li>
                    <li class="pt-1 border-t border-gray-300">
                        <a href="<c:url value="/logout"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-6 w-6" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 102 0V4a1 1 0 00-1-1zm10.293 9.293a1 1 0 001.414 1.414l3-3a1 1 0 000-1.414l-3-3a1 1 0 10-1.414 1.414L14.586 9H7a1 1 0 100 2h7.586l-1.293 1.293z" clip-rule="evenodd"></path>
                            </svg>
                            <span>Log out</span>
                        </a>
                    </li>
                </sec:authorize>
                <sec:authorize access="!isAuthenticated()">
                    <li class="z-20">
                        <a href="<c:url value="/login"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <span>Log in</span>
                        </a>
                    </li>
                    <li class="z-20">
                        <a href="<c:url value="/register"/>" class="block flex flex-row items-center justify-start py-2 px-4 hover:bg-gray-600 hover:text-white">
                            <span>Sign up</span>
                        </a>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>