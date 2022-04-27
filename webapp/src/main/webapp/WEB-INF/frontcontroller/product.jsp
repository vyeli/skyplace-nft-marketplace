<%@ page import="org.springframework.web.servlet.tags.Param" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>

<body class="min-h-screen flex flex-col">
    <%@ include file="../components/navbar.jsp" %>
    <div class="flex justify-center flex-wrap grow gap-8 pt-24 pb-12">
        <!-- Container -->
        <div class="border-2 w-80 rounded-md max-h-96">
            <!-- Description box -->
            <p class="text-2xl font-bold m-4 ">Description</p>
            <hr>
            <p class="p-3 break-words">Created by&nbsp;<span class="text-cyan-500">${nft.seller_email}</span></p>
            <p class="p-3 break-words">
                ${nft.descr}
            </p>
        </div> <!-- Description box -->
        <div class="w-96 h-96">
            <!-- center img -->
            <img class="w-full h-full object-contain"
                src="<c:url value="/images/${nft.img}" />" alt="${nft.name}">
        </div> <!-- center img -->

        <div class="flex flex-col gap-6 w-96">
            <!-- Pricing box-->
            <h1 class="font-bold text-2xl">${nft.name}</h1>
            <div class="border-2 p-2 rounded-md">
                <div class="flex mb-2">
                    <!-- Price -->
                    <img class="h-8 w-8 my-auto"
                        src="<c:url value="/resources/eth_logo.svg" />"
                        alt="eth">
                    <p class="my-auto text-sm"><span
                            class="font-bold text-lg">${nft.price}</span></p>
                </div> <!-- Price -->
                <hr>
                <div class="mt-2 py-2 flex flex-col gap-2">
                    <!-- Specification -->
                    <div class="flex justify-between">
                        <p>Contract address </p>
                        <p class="w-3/5 break-words max-h-20 overflow-hidden text-right">
                            ${nft.contract_addr}
                        </p>
                    </div>
                    <div class="flex justify-between">
                        <p>Token ID</p>
                        <p class="w-3/5 overflow-hidden text-right">${nft.id_nft}</p>
                    </div>
                    <div class="flex justify-between">
                        <p>Blockchain</p>
                        <p>${nft.chain}</p>
                    </div>
                </div> <!-- Specification -->
            </div>
            <div class="flex flex-col">
                <c:choose>
                    <c:when test="${userEmail == nft.seller_email}">
                        <c:url value="/product/delete/${nft.id_product}" var="deletePath" />
                        <div class="flex gap-4">
                            <a href="<c:url value="/product/update/${nft.id_product}" />" class="px-2 py-3 font-bold rounded-lg shadow-sm bg-cyan-100 text-cyan-700 hover:bg-cyan-200">
                                Update order
                            </a>
                            <form action="${deletePath}" method="post">
                                <input type="submit" value="Delete order" class="px-2 py-3 font-bold rounded-lg shadow-sm cursor-pointer border border-cyan-700 text-cyan-700 hover:bg-cyan-700 hover:text-white" />
                            </form>
                        </div>
                    </c:when>
                    <c:when test="${userEmail == null}">
                        <p class="font-bold text-cyan-700">Login to place a bid</p>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/product/${nft.id_product}" var="postPath" />
                        <form:form modelAttribute="mailForm" action="${postPath}" method="post">
                            <div class="flex">
                                <form:input path="buyerMail" type="email" hidden="hidden" value="${userEmail}" />
                                <form:input path="sellerMail" type="hidden" value="${nft.seller_email}"/>
                                <form:input path="nftName" type="hidden" value="${nft.name}"/>
                                <form:input path="nftAddress" type="hidden" value="${nft.contract_addr}"/>
                                <form:input path="nftPrice" type="hidden" value="${nft.price}"/>
                                <input type="submit" value="Place bid" class="px-1 py-3 w-28 font-bold rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200" />
                            </div>
                            <form:errors path="buyerMail" element="p" cssStyle="color: tomato" />
                        </form:form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div> <!-- Pricing box-->
    </div>
</body>

</html>