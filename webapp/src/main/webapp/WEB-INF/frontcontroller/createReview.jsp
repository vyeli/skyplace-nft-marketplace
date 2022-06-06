<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<%@ include file="../frontcontroller/Head.jsp" %>
<body class="mx-5">
<div class="h-screen flex flex-col">
    <!-- Header -->
    <%@ include file="/WEB-INF/components/navbar.jsp" %>
    <!-- Text -->
    <div class="flex flex-col justify-center items-center flex-grow">
        <h1 class="text-3xl text-center mb-12"><spring:message code="review.create"/></h1>
        <c:url value="/review/${revieweeIdParam}/create" var="postPath"/>
        <form:form modelAttribute="reviewForm" action="${postPath}" method="post" class="flex flex-col justify-center items-center gap-4" enctype="multipart/form-data">
            <div class="flex flex-row gap-12">
                <div class="flex flex-col gap-4">
                    <form:hidden path="reviewerId" required="true" value="${reviewerIdParam}"/>
                    <form:hidden path="revieweeId" required="true" value="${revieweeIdParam}"/>
                    <div class="flex flex-col gap-1">
                        <span class="text-slate-600"><spring:message code="review.userReviewed"/></span>
                        <div class="flex flex-row items-center">
                            <img class="w-14 h-14 rounded-full" src="<c:url value='/resources/profile_picture.png'/>" alt="">
                            <span class="ml-2 text-lg">${revieweeUsername}</span>
                        </div>
                    </div>
                    <!-- Rating
                       TODO: Fix rating buttons
                       -->
                    <form:label path="score" class="flex flex-col gap-1">
                        <form:hidden id="ratingValue" path="score" value="1"/>
                        <span class="text-slate-600"><spring:message code="review.rating"/>*</span>
                        <div class="flex flex-row">
                            <svg id="star${minScore}" class="h-12 w-12" onmouseover="hoverStars(${minScore})" xmlns="http://www.w3.org/2000/svg" fill="#FBBF24" viewBox="0 0 24 24" stroke="#4B5563" stroke-width="0.5">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                            </svg>
                            <c:forEach var="i" begin="${minScore+1}" end="${maxScore}">
                                <svg id="star${i}" class="h-12 w-12" onmouseover="hoverStars(${i})" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="#4B5563" stroke-width="0.5">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
                                </svg>
                            </c:forEach>
                        </div>
                        <form:errors path="score" element="p" cssStyle="color: tomato" />
                    </form:label>
                </div>
                <div class="flex flex-col gap-4">
                    <!-- Review title -->
                    <form:label path="title" class="flex flex-col gap-1">
                        <span class="text-slate-600"><spring:message code="review.title"/></span>
                        <form:input
                                type="text"
                                path="title"
                                autoComplete="off"
                                class="pl-3 w-96 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"/>
                        <form:errors path="title" element="p" cssStyle="color: tomato" />
                    </form:label>
                    <!-- Review description -->
                    <form:label path="comments" class="flex flex-col gap-1">
                        <span class="text-slate-600"><spring:message code="review.description"/></span>
                        <form:textarea
                                type="text"
                                path="comments"
                                autoComplete="off"
                                class="h-60 max-h-60 w-96 pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"/>
                    </form:label>
                </div>
            </div>
            <input
                type="submit"
                value='<spring:message code="review.create"/>'
                class="px-6 py-4 font-bold w-fit rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200" />
        </form:form>
    </div>
</div>
<script>
    let stars = [];
    const ratingInput = document.getElementById("ratingValue");
    const minScore = parseInt(${minScore});
    const maxScore = parseInt(${maxScore});

    function hoverStars(hoveredStarNumber){
        if(stars.length === 0)
            fillStars();
        ratingInput.value = hoveredStarNumber.toString();
        for (let i = minScore; i <= maxScore; i++) {
            if (i <= hoveredStarNumber) {
                stars[i - 1].style.fill = "#FBBF24";
            } else {
                stars[i - 1].style.fill = "none";
            }
        }
    }

    function fillStars(){
        for(let i = minScore ; i <= maxScore ; i++){
            let name = "star" + i;
            stars.push(document.getElementById(name));
        }
    }
</script>
</body>
</html>