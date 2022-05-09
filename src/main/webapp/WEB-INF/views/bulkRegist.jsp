<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<meta charset="UTF-8">
<title>一括登録｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <form method="post" action="<%=request.getContextPath()%>/bulkRegistbook" enctype="multipart/form-data">
            <div class="bulk_form">
                <h2>CSVファイルをアップロードすることで書籍を一括で登録できます</h2>
                <div class="caution">
                    <p>「書籍名,著者名,出版社,出版日,ISBN,説明文」の形式で記載してください。</p>
                    <p>※サムネイル画像は一括登録できません。編集画面で編集してください。</p>
                </div>
                <p><input type="file" accept="image/*" name="file" id="csv"></p>
                <p>
                    <button type="submit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_bulkRegist">一括登録</button>
                </p>
            </div>
            <c:if test="${!empty errorMessage}">
                <div class="error">
                    <c:forEach var="error" items="${errorMessage}">
                        <p>${error}</p>
                    </c:forEach>
                </div>
            </c:if>
        </form>
    </main>
</body>
</html>