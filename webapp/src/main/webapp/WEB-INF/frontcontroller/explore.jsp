<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Explore | Skyplace</title>
    <style>
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
    </style>
</head>
<body>
<div class="min-h-screen flex flex-col">
    <%@ include file="../components/navbar.jsp" %>
    <div class="grow flex pt-16 max-h-[calc(100vh-5rem)] divide-x divide-slate-300">
        <div class="flex flex-col w-72 min-w-[250px] items-center">
            <span class="text-4xl"><c:out value="${category}"/></span>
            <span><c:out value="${nftAmount}" /> resultados</span>

            <div class="grow w-full">
                <div class="py-4 flex flex-col w-full px-4">
                    <div class="flex space-evenly">
                        <span class="text-2xl">Filter</span>
                    </div>

                    <div class="py-4 px-2 flex flex-col gap-4">
                        <c:url value="/explore" var="explorePath"/>
                        <form:form modelAttribute="exploreFilter" action="${explorePath}" method="get">

                            <form:label class="flex justify-between text-lg mb-6" path="category">
                                <span>Category</span>
                                <form:select id="categoryFilter" multiple="true"
                                        class="border-2 rounded-xl text-sm pl-2 py-1 pr-8 cursor-pointer w-1/2 text-ellipsis" path="category">
                                    <form:option value="all" selected="true">All</form:option>
                                    <c:forEach var="categories_i" items="${categories}">
                                        <form:option value="${fn:toLowerCase(categories_i)}"><c:out value="${categories_i}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </form:label>

                            <div class="flex justify-between mb-6">
                                <span>Price</span>
                                <div class="flex justify-end">
                                <form:label class="text-lg w-2/5" path="minPrice">
                                    <form:input type="number" step="0.00000001" placeholder="Min" min="0" id="minPriceFilter"
                                           class="border-2 rounded-xl py-1 px-2 text-sm w-full" path="minPrice"/>
                                </form:label>
                                <span class="mx-0.5">-</span>
                                <form:label class="text-lg w-2/5" path="maxPrice">
                                    <form:input type="number" step="0.00000001" placeholder="Max" value="" min="0" id="maxPriceFilter"
                                           class="border-2 rounded-xl py-1 px-2 text-sm w-full" path="maxPrice"/>
                                </form:label>
                                </div>
                            </div>

                            <form:label class="flex justify-between text-lg mb-6" path="chain">Chain
                                <form:select id="chainFilter" multiple="true"
                                        class="border-2 rounded-xl text-sm pl-2 pr-8 cursor-pointer py-1 text-ellipsis w-1/2" path="chain">
                                    <form:option value="all" selected="true">All</form:option>
                                    <c:forEach var="chain_i" items="${chains}">
                                        <form:option value="${fn:toLowerCase(chain_i)}"><c:out value="${chain_i}"/></form:option>
                                    </c:forEach>
                                </form:select>
                            </form:label>

                            <form:label class="flex justify-between text-lg mb-6" path="sort">Sort by
                                <form:select id="sortSelect"
                                        class="border-2 rounded-xl text-sm pl-2 pr-8 cursor-pointer py-1 text-ellipsis w-1/2" path="sort">
                                    <form:option value="default">Default</form:option>
                                    <form:option value="name">Name</form:option>
                                    <form:option value="price_asc">Price ascending</form:option>
                                    <form:option value="price_dsc">Price descending</form:option>
                                </form:select>
                            </form:label>

                            <input type="submit" value="Apply" class="rounded-lg border-2 border-slate-400 px-4 py-1 cursor-pointer" />
                        </form:form>
                    </div>
                </div>
            </div>
        </div>

        <div class="w-[80%] min-w-[500px] grow flex flex-col">
            <!-- header -->
            <%--      <div class="flex justify-between h-16">--%>
            <%--        <!-- pages -->--%>
            <%--        <div class="flex text-2xl pt-4">--%>
            <%--          <!-- icon previous -->--%>
            <%--&lt;%&ndash;          <span class=" text-gray-400 cursor-pointer mr-4">Previous</span>&ndash;%&gt;--%>
            <%--&lt;%&ndash;          <label>&ndash;%&gt;--%>
            <%--&lt;%&ndash;            <input type="number" min="1" value="1"&ndash;%&gt;--%>
            <%--&lt;%&ndash;                   class="w-10 border-2 border-slate-300 rounded-lg bg-slate-300 px-1 mx-1 h-10" />&ndash;%&gt;--%>
            <%--&lt;%&ndash;          </label>&ndash;%&gt;--%>
            <%--&lt;%&ndash;          <span class="ml-4"> of ${pages}</span>&ndash;%&gt;--%>
            <%--&lt;%&ndash;          <span class="text-cyan-400 cursor-pointer ml-4">Next</span>&ndash;%&gt;--%>
            <%--        </div>--%>
            <%--        <div class="pt-4 text-2xl mr-8">--%>
            <%--          <!----%>
            <%--          <label for="npages" class="">Show</label>--%>
            <%--          <select name="npages" id="npages" class="border-2 rounded-xl w-16 cursor-pointer">--%>
            <%--            <option value="12p">12</option>--%>
            <%--            <option value="24p">24</option>--%>
            <%--            <option value="48p">48</option>--%>
            <%--          </select>--%>
            <%--          -->--%>
            <%--        </div>--%>
            <%--      </div>--%>

            <div class="px-8 pb-8 grid grid-cols-auto-fit gap-8 place-items-start overflow-x-hidden overflow-y-scroll">
                <c:forEach items="${nfts}" var="nft">
                        <jsp:include page="../components/Card.jsp">
                            <jsp:param name="name" value="${nft.name}"/>
                            <jsp:param name="descr" value="${nft.descr}"/>
                            <jsp:param name="img" value="${nft.img}"/>
                            <jsp:param name="price" value="${nft.price}"/>
                            <jsp:param name="score" value="${nft.score}"/>
                            <jsp:param name="seller_email" value="${nft.seller_email}"/>
                            <jsp:param name="id_product" value="${nft.id_product}"/>
                        </jsp:include>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</body>
</html>
