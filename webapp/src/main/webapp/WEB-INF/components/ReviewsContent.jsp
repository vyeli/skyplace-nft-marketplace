<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Reviews -->
<div class="flex flex-row gap-6">
    <!-- Mean score -->
    <div class="border rounded-md h-fit p-5">
        <h2 class="text-lg">
            <spring:message code="profile.ratings"/>
        </h2>
        <h3 class="mb-6 text-gray-400"><spring:message code="profile.totalReviews" arguments="${param.reviewAmount}"/></h3>
        <div class="flex flex-col">
            <div class="flex flex-row items-center w-64 xl:w-80 2xl:w-96">
                <div class="flex flex-col gap-3">
                    <c:forEach var="i" begin="${param.minScore-1}" end="${param.maxScore-2}">
                        <span class="text-sm font-medium text-blue-700">
                            <spring:message code="profile.stars" arguments="${param.maxScore-i}"/>
                        </span>
                    </c:forEach>
                    <span class="text-sm font-medium text-blue-700">
                        <spring:message code="profile.star" arguments="1"/>
                    </span>
                </div>
                <div class="flex flex-col grow gap-3">
                    <c:forEach items="${ratings}" var="rating">
                        <div class="h-5 mx-4 bg-gray-100 rounded border border-gray-300">
                            <div class="h-5 bg-amber-500 rounded" style="width: <c:out value="${rating}"/>%; height: 99%"></div>
                        </div>
                    </c:forEach>
                </div>
                <div class="flex flex-col gap-3">
                    <c:forEach items="${ratings}" var="rating">
                        <span class="text-sm font-medium text-blue-700"><c:out value="${rating}"/>%</span>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <!-- Detailed reviews -->
    <div class="flex flex-col grow divide-y gap-4 mx-8">
        <div class="flex flex-row items-center justify-between">
            <h2 class="text-2xl"><spring:message code="profile.reviews"/></h2>
            <c:if test="${param.canReview == true}">
                <a href="<c:url value='/review/${param.userId}/create'/>">
                    <button type="button" class="shadow-md px-6 py-2.5 rounded-md transition duration-300 bg-cyan-600 hover:bg-cyan-800 text-white hover:shadow-xl">
                        <spring:message code="profile.addReview"/>
                    </button>
                </a>
            </c:if>
        </div>
        <c:choose>
            <c:when test="${param.reviewAmount == 0}">
                <span class="pt-5 text-center"><spring:message code="profile.noReviews"/></span>
            </c:when>
            <c:otherwise>
                <c:forEach items="${reviews}" var="review">
                    <jsp:include page="./ReviewItem.jsp">
                        <jsp:param name="reviewerName" value="${review.usersByIdReviewer.username}"/>
                        <jsp:param name="reviewerId" value="${review.usersByIdReviewer.id}"/>
                        <jsp:param name="revieweeId" value="${review.usersByIdReviewee.id}"/>
                        <jsp:param name="title" value="${review.title}"/>
                        <jsp:param name="comments" value="${review.comments}"/>
                        <jsp:param name="score" value="${review.score}"/>
                        <jsp:param name="reviewId" value="${review.id}"/>
                        <jsp:param name="isAdmin" value="${param.isAdmin}"/>
                        <jsp:param name="isReviewer" value="${param.currentUserId == review.usersByIdReviewer.id}"/>
                        <jsp:param name="modalId" value="${review.id}"/>
                        <jsp:param name="minScore" value="${param.minScore}"/>
                        <jsp:param name="maxScore" value="${param.maxScore}"/>
                    </jsp:include>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
