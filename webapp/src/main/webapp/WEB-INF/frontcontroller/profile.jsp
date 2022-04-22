<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col max-w-[100vw]">
<!-- Header -->
<%@ include file="../components/navbar.jsp" %>
<div class="flex flex-col flex-grow mx-10 lg:mx-24 xl:mx-40">
    <!-- Profile -->
    <div class="flex flex-col flex-grow md:flex-row items-center mt-5 mb-2">
        <img class="rounded-full h-40 w-40" src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="profile_icon"/>
        <!-- Profile info -->
        <div class="flex flex-col flex-grow ml-5">
            <!-- Profile name, wallet and more options (aligned) -->
            <div class="flex flex-row flex-grow items-center justify-between">
                <div class="flex flex-row">
                    <span class="text-4xl font-semibold">space.invader01</span>
                    <button class="flex flex-row items-center ml-5 lg:ml-10 px-2 border rounded-2xl" onclick="copyToClipboard()" data-tooltip-target="tooltip-dark" data-tooltip-placement="bottom" type="button" id="walletButton">
                        <img class="w-6 h-6" src="<c:url value='/resources/utility_icon.svg' />" alt="wallet_icon" />
                        <!--
                        <svg class="fill-cyan-700 rounded-full" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
                            <path fill="none" d="M0 0h24v24H0z"/>
                            <path d="M22 7h1v10h-1v3a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h18a1 1 0 0 1 1 1v3zm-2 10h-6a5 5 0 0 1 0-10h6V5H4v14h16v-2zm1-2V9h-7a3 3 0 0 0 0 6h7zm-7-4h3v2h-3v-2z"/>
                        </svg>
                        -->
                        <!-- <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" viewBox="0 0 512 512"><rect x="48" y="144" width="416" height="288" rx="48" ry="48" fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="32"/><path d="M411.36 144v-30A50 50 0 00352 64.9L88.64 109.85A50 50 0 0048 159v49" fill="none" stroke="currentColor" stroke-linejoin="round" stroke-width="32"/><path d="M368 320a32 32 0 1132-32 32 32 0 01-32 32z"/></svg> -->
                        <span class="text-xl ml-1 text-gray-400 font-semibold truncate w-32 hover:text-gray-600" id="walletId">0xF00D0a7D9b36AeF74fddfA60A608e4770993Aff4</span>
                    </button>
                    <div id="tooltip-dark" role="tooltip" class="inline-block absolute invisible z-10 py-2 px-3 text-sm font-medium text-white bg-gray-900 rounded-lg shadow-sm opacity-0 tooltip dark:bg-gray-700">
                        Copy
                        <div class="tooltip-arrow" data-popper-arrow></div>
                    </div>
                </div>
                <button class="border border-gray-200 rounded-lg p-0.5 text-gray-400">
                    <img class="w-9 h-9" src="<c:url value='/resources/more_options.svg' />" alt="options_icon"/>
                    <!--
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-9 w-9" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 5v.01M12 12v.01M12 19v.01M12 6a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2zm0 7a1 1 0 110-2 1 1 0 010 2z" />
                    </svg>
                    -->
                </button>
            </div>
            <span class="text-lg font-light text-gray-400">space.invaderrr@gmail.com</span>
            <!-- Rating -->
            <div class="flex flex-row items-center mt-3">
                <svg class="w-9 h-9 text-yellow-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                </svg>
                <p class="text-xl font-bold text-gray-900 ml-1">4.95</p>
                <span class="w-1.5 h-1.5 mx-2 bg-gray-500 rounded-full dark:bg-gray-400"></span>
                <a href="#" class="text-xl font-medium text-cyan-600 underline hover:no-underline">17 Reviews</a>
            </div>
            <!--
            <div class="flex flex-row items-center mt-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-11 w-11 fill-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="0.6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-11 w-11 fill-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="0.6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-11 w-11 fill-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="0.6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-11 w-11 fill-cyan-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="0.6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
                <svg xmlns="http://www.w3.org/2000/svg" class="h-11 w-11" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="0.6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                </svg>
                <h1 class="text-cyan-600 text-xl ml-3">17 Reviews</h1>
            </div>
            -->
        </div>
    </div>
    <div class="flex flex-col flex-grow">
        <!-- Tabs -->
        <div class="flex flex-row flex-grow border-b border-gray-200">
            <ul class="flex flex-wrap flex-grow flex-row justify-evenly items-center font-medium text-center text-gray-500 dark:text-gray-400">
                <li>
                    <a href="/profile" class="inline-flex p-4 rounded-t-lg border-b-2 group" id="sellingTab">
                        <svg class="mr-2 h-6 w-6" viewBox="0 0 512 512">
                            <path d="M403.29 32H280.36a14.46 14.46 0 00-10.2 4.2L24.4 281.9a28.85 28.85 0 000 40.7l117 117a28.86 28.86 0 0040.71 0L427.8 194a14.46 14.46 0 004.2-10.2v-123A28.66 28.66 0 00403.29 32z" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></path>
                            <path d="M352 144a32 32 0 1132-32 32 32 0 01-32 32z"></path>
                            <path d="M230 480l262-262a13.81 13.81 0 004-10V80" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></path>
                        </svg>
                        Selling 3
                    </a>
                </li>
                <li>
                    <a href="/profile?tab=buyorders" class="inline-flex p-4 rounded-t-lg border-b-2 group" id="buyOrdersTab">
                        <svg class="mr-2 h-6 w-6" viewBox="0 0 512 512">
                            <circle cx="176" cy="416" r="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></circle>
                            <circle cx="400" cy="416" r="16" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></circle>
                            <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32" d="M48 80h64l48 272h256"></path>
                            <path d="M160 288h249.44a8 8 0 007.85-6.43l28.8-144a8 8 0 00-7.85-9.57H128" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="32"></path>
                        </svg>
                        Buy orders 2
                    </a>
                </li>
                <li>
                    <a href="/profile?tab=favorited" class="inline-flex p-4 rounded-t-lg border-b-2 group" id="favoritedTab">
                        <svg class="mr-2 h-6 w-6" width="30" height="30" viewBox="0 0 30 30" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M26.0502 5.76252C25.4117 5.12378 24.6537 4.61708 23.8193 4.27138C22.985 3.92568 22.0908 3.74774 21.1877 3.74774C20.2845 3.74774 19.3903 3.92568 18.556 4.27138C17.7216 4.61708 16.9636 5.12378 16.3252 5.76252L15.0002 7.08752L13.6751 5.76252C12.3855 4.47291 10.6364 3.74841 8.81265 3.74841C6.98886 3.74841 5.23976 4.47291 3.95015 5.76252C2.66053 7.05214 1.93604 8.80123 1.93604 10.625C1.93604 12.4488 2.66053 14.1979 3.95015 15.4875L5.27515 16.8125L15.0002 26.5375L24.7252 16.8125L26.0502 15.4875C26.6889 14.8491 27.1956 14.091 27.5413 13.2567C27.887 12.4224 28.0649 11.5281 28.0649 10.625C28.0649 9.72191 27.887 8.82765 27.5413 7.99333C27.1956 7.15901 26.6889 6.40097 26.0502 5.76252V5.76252Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                        </svg>
                        Favorited 0
                    </a>
                </li>
            </ul>
        </div>
        <!-- NFTs -->
        <div class="flex flex-col flex-grow my-5">
            <div class="grid grid-cols-auto-fit justify-center gap-8 items-center overflow-x-hidden">
                <c:forEach items="${nfts}" var="nft">
                    <div class="flex justify-center place-items-center">
                        <jsp:include page="../components/Card.jsp">
                            <jsp:param name="name" value="${nft.name}" />
                            <jsp:param name="descr" value="${nft.descr}" />
                            <jsp:param name="img" value="${nft.img}" />
                            <jsp:param name="price" value="${nft.price}" />
                            <jsp:param name="score" value="${nft.score}" />
                            <jsp:param name="seller_email" value="${nft.seller_email}" />
                            <jsp:param name="id_product" value="${nft.id_product}"/>
                        </jsp:include>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<script>
    const tabMap = new Map();       // tablink -> tab
    tabMap.set('buyorders', document.getElementById('buyOrdersTab'));
    tabMap.set('favorited', document.getElementById('favoritedTab'));
    tabMap.set('selling', document.getElementById('sellingTab'));

    window.onload = function setDefaultTab(){
        const params = new URLSearchParams(window.location.search);
        const tabName = params.get('tab');
        switch(tabName){
            case 'buyorders':
                setActiveTab(tabMap.get('buyorders'));
                setInactiveTab(tabMap.get('favorited'));
                setInactiveTab(tabMap.get('selling'));
                break;
            case 'favorited':
                setActiveTab(tabMap.get('favorited'));
                setInactiveTab(tabMap.get('selling'));
                setInactiveTab(tabMap.get('buyorders'));
                break;
            default:
                setActiveTab(tabMap.get('selling'));
                setInactiveTab(tabMap.get('buyorders'));
                setInactiveTab(tabMap.get('favorited'));
                break;
        }
    };

    function setActiveTab(tab){
        tab.classList.add('text-cyan-600', 'border-cyan-600', 'active');
    }

    function setInactiveTab(tab){
        tab.classList.add('border-transparent', 'hover:text-gray-600');
    }

    function copyToClipboard(){
        // Copy text
        const walletAddress = document.getElementById("walletId");
        navigator.clipboard.writeText(walletAddress.textContent);
        // Set text to Copied!
        const tooltipText = document.getElementById("tooltip-dark");
        tooltipText.firstChild.data = "Copied!";
        setTimeout(() => tooltipText.firstChild.data = 'Copy', 1000);
    }
</script>
</body>
</html>
