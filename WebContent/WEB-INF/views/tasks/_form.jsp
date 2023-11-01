<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${errors != null }">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors }">
            ・<c:out value="${error }" /><br />
        </c:forEach>
    </div>
</c:if>

<label for="content_task">タスクの内容</label><br />
<input type="text" name="content" id="content_task" value="${task.content}" />
<br /><br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>

<%--
 value="${task.content}"  と書くことで、
 リクエストスコープの task オブジェクトからデータを参照し、
 入力内容の初期値として表示するようしている。
 入力値エラーがあってフォームのページを再度表示する際に役立つ
--%>