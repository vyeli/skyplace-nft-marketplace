<%@ page import="org.springframework.web.servlet.tags.Param" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <!DOCTYPE html>
            <html>
            <%@ include file="Head.jsp" %>

                <body class="h-screen flex flex-col">
                    <%@ include file="../components/navbar.jsp" %>
                        <div class="flex flex-row justify-center mt-12">
                            <!-- Container -->
                            <div class="border-2 w-80 mt-8 rounded-md">
                                <!-- Description box -->
                                <p class="text-2xl font-bold m-4 ">Description</p>
                                <hr>
                                <p class="m-3">Created by&nbsp;<span class="text-cyan-500">${nft.seller_email}</span>
                                    <br>
                                    <br>
                                    ${nft.descr}
                                </p>
                            </div> <!-- Description box -->
                            <div class="w-96 h-96">
                                <!-- center img -->
                                <img class="object-contain max-w-xl m-8 rounded-md"
                                    src="data:image/jpg;base64,${nft.img}" alt="${nft.name}">
                            </div> <!-- center img -->

                            <div class="flex flex-col">
                                <!-- Pricing box-->
                                <h1 class="py-8 font-bold text-2xl">${nft.name}</h1>
                                <div class="border-2 p-2 rounded-md h-64">
                                    <div class="flex mb-2">
                                        <!-- Price -->
                                        <img class="h-7 w-7 my-auto"
                                            src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Ethereum-icon-purple.svg/2048px-Ethereum-icon-purple.svg.png"
                                            alt="eth">
                                        <p class="my-auto text-sm"><span
                                                class="font-bold pl-4 text-lg">${nft.price}</span></p>
                                    </div> <!-- Price -->
                                    <hr>
                                    <div class="mt-2 py-2 h-52 grid grid-flow-row ">
                                        <!-- Specification -->
                                        <div class="flex justify-between">
                                            <p>Contract address </p>
                                            <p>
                                                ${nft.contract_addr}
                                            </p>
                                        </div>
                                        <div class="flex justify-between">
                                            <p>Token ID</p>
                                            <p>${nft.id_nft}</p>
                                        </div>
                                        <div class="flex justify-between">
                                            <p>Blockchain</p>
                                            <p>${nft.chain}</p>
                                        </div>
                                    </div> <!-- Specification -->
                                    <div class="p-4 flex flex-col">

                                        <c:url value="/product/${nft.id_product}" var="postPath" />
                                        <form:form modelAttribute="mailForm" action="${postPath}" method="post">
                                            <div class="flex">
                                                <form:input path="buyerMail" type="email" required="true" name="field_name"
                                                            class="border-2 rounded px-4 py-2 w-56"
                                                            placeholder="your@email.com" />
                                                <form:input path="sellerMail" type="hidden" value="${nft.seller_email}"/>
                                                <form:input path="nftName" type="hidden" value="${nft.name}"/>
                                                <form:input path="nftAddress" type="hidden" value="${nft.contract_addr}"/>
                                                <form:input path="nftPrice" type="hidden" value="${nft.price}"/>
                                                <input type="submit" value="Place bid" autocomplete="off"
                                                    class="ml-4 p-1 w-28 font-bold rounded-lg shadow-sm cursor-pointer
                                                            bg-cyan-100 text-cyan-700 hover:bg-cyan-200 hover:text-white" />
                                            </div>
                                            <p class="ml-2 italic mt-2">Enter your email to inform the seller</p>
                                            <form:errors path="buyerMail" element="p" cssStyle="color: tomato" />
                                        </form:form>
                                    </div>
                                </div>
                            </div> <!-- Pricing box-->
                        </div>
                </body>

            </html>