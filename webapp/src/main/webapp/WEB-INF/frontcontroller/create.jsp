<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col">
<%@ include file="../components/navbar.jsp" %>
<div class="w-full max-w-5xl mx-auto p-4 grow flex flex-col justify-center py-12">
    <h1 class="text-3xl text-center">
        Create an NFT
    </h1>
    <c:url value="/create" var="postPath"/>
    <form:form modelAttribute="createNftForm" action="${postPath}" method="post" class="grid grid-cols-2 gap-8 pt-8" enctype="multipart/form-data">
        <form:label path="name" class="flex flex-col gap-1">
            <span class="text-slate-600">Name</span>
            <form:input
                    type="text"
                    path="name"
                    required="true"
                    autoComplete="off"
                    placeholder="Nft name"
                    class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            />
            <form:errors path="name" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="nftId" class="flex flex-col gap-1">
            <span class="text-slate-600">Id</span>
            <form:input
                    type="number"
                    path="nftId"
                    min="0"
                    required="true"
                    autoComplete="off"
                    placeholder="Nft id"
                    class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            />
            <form:errors path="nftId" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="contractAddr" class="flex flex-col gap-1">
            <span class="text-slate-600">Contract</span>
            <form:input
                    type="text"
                    path="contractAddr"
                    required="true"
                    autoComplete="off"
                    placeholder="0xabcdef0123456789ABCDEF0123456789abcdef01"
                    class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            />
            <form:errors path="contractAddr" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="chain" class="flex flex-col gap-1">
            <span class="text-slate-600">Blockchain</span>
            <form:select
                    path="chain"
                    required="true"
                    class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            >
                <c:forEach var="chain" items="${chains}">
                    <form:option value="${chain}"><c:out value="${chain}" /></form:option>
                </c:forEach>
            </form:select>
            <form:errors path="chain" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="collection" class="flex flex-col gap-1">
            <span class="text-slate-600">Collection</span>
            <form:input
                    type="text"
                    path="collection"
                    required="true"
                    autoComplete="off"
                    placeholder="Crypto Ape"
                    class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            />
            <form:errors path="collection" element="p" cssStyle="color: tomato" />
        </form:label>
        <label class="flex flex-col gap-1">
            <span class="text-slate-600">Image</span>
            <form:input
                    path="image"
                    type="file"
                    required="true"
                    name="file"
                    class="mt-3 block text-sm text-slate-500 file:mr-4 file:py-2 file:px-4
            file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-cyan-50 file:text-cyan-700 hover:file:bg-cyan-100"
            />
            <form:errors path="image" element="p" cssStyle="color: tomato" />
        </label>
        <form:label path="description" class="flex flex-col gap-1">
            <span class="text-slate-600">Description</span>
            <form:textarea
                    path="description"
                    autoComplete="off"
                    placeholder="Nft description"
                    class="min-h-16 max-h-32 pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            />
        </form:label>
        <input
                type="submit"
                value="Publish"
                class="px-1 py-4 col-start-2 font-bold rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200"
        />
        <form:errors path="publish" element="p" cssStyle="color: tomato" />
    </form:form>
</div>
</body>
</html>
