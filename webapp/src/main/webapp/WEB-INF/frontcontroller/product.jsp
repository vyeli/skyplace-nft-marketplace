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
            <p class="mx-3 mt-3 break-words">Created by</p>
            <p class="mx-3 mb-3 text-cyan-500 truncate"><c:out value="${owner.email}" /></p>
            <p class="m-3 break-words line-clamp-9">
                <c:out value="${nft.description}" />
            </p>
        </div> <!-- Description box -->
        <div class="w-96 h-96">
            <!-- center img -->
            <img class="w-full h-full object-contain"
                src="<c:url value="/images/${nft.id_image}" />" alt="<c:out value="${nft.nft_name}" />">
        </div> <!-- center img -->

        <div class="flex flex-col gap-6 w-96">
            <!-- Pricing box-->
            <h1 class="font-bold text-2xl truncate"><c:out value="${nft.nft_name}" /></h1>
            <div class="border-2 p-2 rounded-md">
                <div class="flex mb-2">
                <c:choose>
                    <c:when test="${sellorder != null}">
                        <!-- Price -->
                        <img class="h-8 w-8 my-auto"
                            src="<c:url value="/resources/eth_logo.svg" />"
                            alt="eth">
                        <p class="my-auto text-sm"><span
                                class="font-bold text-lg"><c:out value="${sellorder.price}" /></span></p>
                    </c:when>
                    <c:otherwise>
                        <p class="my-auto text-sm"><span
                                class="font-bold text-lg">Not for sale</span></p>
                    </c:otherwise>
                </c:choose>
                </div> <!-- Price -->
                <hr>
                <div class="mt-2 py-2 flex flex-col gap-2">
                    <!-- Specification -->
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
                </div> <!-- Specification -->
            </div>
            <div class="flex flex-col">
                <c:if test="${sellorder != null}">
                    <c:url value="/product/${nft.id}" var="postPath" />
                    <form:form modelAttribute="buyNftForm" action="${postPath}" method="post">
                        <div class="flex">
                            <form:input path="price" type="number" value="${sellorder.price}" step="0.000000000000000001" />
                            <input type="submit" value="Place bid" class="px-1 py-3 w-28 font-bold rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200" />
                        </div>
                        <form:errors path="price" element="p" cssStyle="color: tomato" />
                    </form:form>
                </c:if>
            </div>
            <c:if test="${emailSent != null}">
                <p class="text-lime-600 text-lg">Email sent!</p>
            </c:if>
        </div> <!-- Pricing box-->
    </div>
</body>

</html>