<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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