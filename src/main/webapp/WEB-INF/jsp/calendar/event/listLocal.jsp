<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../../common/header.jsp"/>

<!--div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="header">
                <h4 class="title">MH</h4>
                <p class="category">24 Hours performance</p>
            </div>
            <div class="content pre-scrollable">
                <iframe id="frame" src="http://www.shilladfs.com" width="100%" height="300"></iframe>
            </div>
        </div>
    </div>
</div-->
<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="header">
                <h4 class="title">이벤트 목록</h4>
                <p class="category">
                    기간:
                    <input type="text" name="searchStart" value="${param.searchStart}"/> ~
                    <input type="text" name="searchEnd" value="${param.searchEnd}"/>
                    <button >검색</button>
                    <button onclick="document.form.submit();">저장</button>
                </p>
            </div>
            <div class="pre-scrollable_">
                <div class="content table-responsive table-full-width">
                    <form name="form" action="/calendar/event/local/save" method="post">
                        <input type="hidden" name="searchStart" value="${param.searchStart}"/>
                        <input type="hidden" name="searchEnd" value="${param.searchEnd}"/>
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr>
                                <th>일시</th>
                                <th>분</th>
                                <th>분류</th>
                                <th>SR/CR</th>
                                <th>요청자</th>
                                <th>내용</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${myEvents}" var="item" varStatus="status">
                                <c:set var="isValid" value="${not empty item.category1 and not empty item.category2 and not empty item.category3}"/>
                                <tr title="${item.summary}">
                                    <td>
                                        <fmt:formatDate value="${item.start}" pattern="MM-dd HH:mm"/> - <br/>
                                        <fmt:formatDate value="${item.end}" pattern="MM-dd HH:mm"/>
                                    </td>
                                    <td>${item.minutes}</td>
                                    <td>${item.category1} - ${item.category2} - ${item.category3}</td>
                                    <td>${item.srId}</td>
                                    <td>${item.requester}</td>
                                    <td><textarea>${item.content}</textarea></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    function change(obj) {
        var tr = obj.parentNode.parentNode;
        var data = {
            id: $(tr).find('[name=id]').val()
            , iCalUid: $(tr).find('[name=icalUid]').val()
            , summary: $(tr).find('[name=summary]').val()
        };
        console.log(data);

        $.post( "/calendar/event/google/updateSummary", data)
                .done(function( data ) {
                    alert( "Data Loaded: " + data );
                    console.log(data);
                });
    }
</script>
<c:import url="../../common/footer.jsp"/>