<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>


<html>
<%@ include file="Head.jsp" %>
<body class=" font-body overflow-x-hidden">
<%@ include file="../components/navbar.jsp" %>
<main class="mt-24">
    <section class="flex justify-around">
        <div class="container">
            <div class="flex flex-row ">
                <!-- Image and tabs (first div)-->
                <div class="flex-col">
                    <!--Image-->
                    <figure class="mb-8">
                        <img src="https://i0.wp.com/www.criptotendencias.com/wp-content/uploads/2021/10/cryptopunks.png"
                             alt="item" class="rounded-2xl cursor-pointer">
                    </figure>
                    <!--TAGS-->
                    <div class="rounded-lg border-2">
                        <ul class="nav nav-tabs flex flex-col md:flex-row flex-wrap list-none border-b-0 pl-0 mb-4 "
                            id="tabs-tab" role="tablist">
                            <!--Offers-->
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
                                    <span class="font-display text-base font-medium">Offers</span>
                                </button>
                            </li>
                            <!-- Properties -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-jacarta-400 relative flex items-center whitespace-nowrap py-3 px-6 dark:hover:text-white"
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

                            <!-- Details -->
                            <li class="nav-item" role="presentation">
                                <button class="nav-link hover:text-cyan-700 text-cyan-400 relative flex items-center whitespace-nowrap py-3 px-6 dark:hover:text-white"
                                        id="tabs-messages-tab" data-bs-toggle="tab" data-bs-target="#tabs-messages"
                                        type="button" role="tab" aria-controls="tabs-messages" aria-selected="false">
                                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                         class="mr-1 h-5 w-5 fill-current">
                                        <path fill="none" d="M0 0h24v24H0z"></path>
                                        <path d="M20 22H4a1 1 0 0 1-1-1V3a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1v18a1 1 0 0 1-1 1zm-1-2V4H5v16h14zM7 6h4v4H7V6zm0 6h10v2H7v-2zm0 4h10v2H7v-2zm6-9h4v2h-4V7z"></path>
                                    </svg>
                                    <span class="font-display text-base font-medium">Details</span>
                                </button>
                            </li>
                        </ul>
                        <!-- TAB CONTENT-->
                        <div class="tab-content m-4" id="tabs-tabContent">
                            <div class="tab-pane fade show active" id="tabs-home" role="tabpanel"
                                 aria-labelledby="tabs-home-tab">
                                Tab 1 content
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
                            <a href="collection" class="text-accent mr-2 underline decoration-cyan-700 font-bold">Collection: CryptoPunksNFT</a>
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
                        <div class="ml-auto flex space-x-2">
                            <div class="flex items-center space-x-1 rounded-xl border bg-white py-2 px-4">
                              <span class="relative cursor-pointer before:absolute before:h-4 before:w-4 before:bg-[url(&#39;../img/heart-fill.svg&#39;)] before:bg-cover before:bg-center before:bg-no-repeat before:opacity-0">
                                <!--heart svg-->
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"
                                     class=" fill-black hover:fill-red h-4 w-4">
                                  <path fill="none" d="M0 0H24V24H0z"></path>
                                  <path d="M12.001 4.529c2.349-2.109 5.979-2.039 8.242.228 2.262 2.268 2.34 5.88.236 8.236l-8.48 8.492-8.478-8.492c-2.104-2.356-2.025-5.974.236-8.236 2.265-2.264 5.888-2.34 8.244-.228zm6.826 1.641c-1.5-1.502-3.92-1.563-5.49-.153l-1.335 1.198-1.336-1.197c-1.575-1.412-3.99-1.35-5.494.154-1.49 1.49-1.565 3.875-.192 5.451L12 18.654l7.02-7.03c1.374-1.577 1.299-3.959-.193-5.454z"></path>
                                </svg>
                              </span>
                              <span class="text-sm">188</span>
                            </div>
                        </div>
                    </div>

                    <!--Name and ID-->
                    <h1 class="font-display text-jacarta-700 mb-4 text-4xl font-semibold">Crypto punks<span>#69</span></h1>
                    <div class="mb-8 flex items-center space-x-4 whitespace-nowrap">
                        <div class="flex items-center">
                            <!--ETH SVG-->
                            <span class="-ml-1" data-tippy-content="ETH">
                              <svg x="0" y="0" viewBox="0 0 1920 1920"
                                   xml:space="preserve" class="mr-1 h-4 w-4">
                                <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                <path fill="#62688F" d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                              </svg>
                            </span>
                            <span class="text-green text-sm font-medium tracking-tight">4.7 ETH</span>
                        </div>
                        <span class="text-jacarta-400 text-sm">Highest bid</span>
                        <span class="text-jacarta-400 text-sm">1/1 available</span>
                    </div>

                    <p class="mb-10">
                        Neque aut veniam consectetur magnam libero, natus eius numquam reprehenderit hic at,
                        excepturi
                        repudiandae magni optio odio doloribus? Facilisi lobortisal morbi fringilla urna amet sed
                        ipsum.
                    </p>

                    <!-- Owner -->
                    <div class="mb-4 flex">
                        <figure class="w-1/6 pr-2">
                            <a href="owner" >
                                <img src="https://cryptoshitcompra.com/wp-content/uploads/2021/10/1634003354_307_10-proyectos-NFT-Avatar-con-casos-de-uso-para-juegos.png"
                                     alt="avatar 1" class="rounded-lg" loading="lazy">
                            </a>
                        </figure>
                        <div class="flex flex-col justify-center">
                            <span class="text-jacarta-400 block text-sm">Owned by</span>
                            <a href="owner" class="text-accent block">
                                <span class="text-sm font-bold">@051_Hart</span>
                            </a>
                        </div>
                    </div>

                    <!-- Bid -->
                    <div class=" border-gray-200 rounded-2xl border bg-white p-8 flex-col justify-between">
                        <div class="mb-8 sm:flex sm:flex-wrap">
                            <!-- Highest bid -->
                            <div class=""> <!--RESPONSIVE-->
                                <div class="block overflow-hidden text-ellipsis whitespace-nowrap">
                                    <span class="text-sm">Highest bid by </span>
                                    <a href="https://deothemes.com/envato/xhibiter/html/user.html"
                                       class="text-accent text-sm font-bold">0x695d2ef170ce69e794707eeef9497af2de25df82</a>
                                </div>
                                <div class="pt-8 flex">
                                    <figure class="mr-4 w-1/6">
                                        <a href="user">
                                            <img src="https://www.ultcube88.com/wp-content/uploads/2021/08/cat.jpg"
                                                 alt="avatar" class="rounded-lg"
                                                 loading="lazy">
                                        </a>
                                    </figure>
                                    <!--price-->
                                    <div class="flex items-center whitespace-nowrap">
                                    <span class="-ml-1">
                                    <!-- ETH SVG -->
                                      <svg version="1.1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 1920 1920"
                                           xml:space="preserve" class="h-5 w-5">
                                        <path fill="#8A92B2" d="M959.8 80.7L420.1 976.3 959.8 731z"></path>
                                        <path fill="#62688F"
                                              d="M959.8 731L420.1 976.3l539.7 319.1zm539.8 245.3L959.8 80.7V731z"></path>
                                        <path fill="#454A75" d="M959.8 1295.4l539.8-319.1L959.8 731z"></path>
                                        <path fill="#8A92B2" d="M420.1 1078.7l539.7 760.6v-441.7z"></path>
                                        <path fill="#62688F" d="M959.8 1397.6v441.7l540.1-760.6z"></path>
                                      </svg>
                                    </span>
                                        <span class=" text-lg font-medium leading-tight tracking-tight">4.7 ETH&nbsp</span>
                                        <span class="text-sm">~13217.34 USD</span>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <a href=""
                           class="bg-cyan-600 shadow-accent-volume hover:bg-cyan-700 inline-block w-full rounded-full py-3 px-8 text-center font-semibold text-white transition-all">Place
                            Bid</a>
                    </div> <!-- end bid -->
                </div> <!-- end description -->
            </div>
            </div>
    </section>
</main>
</body>
</html>
