<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../../common/header.jsp"/>

<div class="row">
    <div class="col-md-12">
        <div class="card">
            <div class="header">
                <h4 class="title">이벤트 목록</h4>
                <p class="category">
                    기간:
                    <input id="fromDate" type="text">
                    <input type="date" name="searchStart" class="date-picker" value="${param.searchStart}"/> ~
                    <input type="date" name="searchEnd" class="date-picker" value="${param.searchEnd}"/>
                    <button >검색</button>
                    <button onclick="document.form.submit();">저장</button>
                </p>
            </div>
            <div class="content table-responsive table-full-width">
                <form name="form" action="/myevent/local/save" method="post">
                    <input type="hidden" name="searchStart" value="${param.searchStart}" readonly/>
                    <input type="hidden" name="searchEnd" value="${param.searchEnd}" readonly/>
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>summary</th>
                            <th></th>
                            <th>keyword</th>
                            <th>category1</th>
                            <th>category2</th>
                            <th>category3</th>
                            <th>requester</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${myEvents}" var="item" varStatus="status">
                            <c:set var="isValid" value="${not empty item.category1 and not empty item.category2 and not empty item.category3}"/>
                            <tr>
                                <td>
                                    <fmt:formatDate value="${item.start}" pattern="MM-dd HH:mm"/> -
                                    <fmt:formatDate value="${item.end}" pattern="MM-dd HH:mm"/>
                                    : ${item.location} <br/>
                                    <textarea name="summary">${item.summary}</textarea>
                                    <input type="hidden" name="id" value="${item.id}"/>
                                    <input type="hidden" name="icalUid" value="${item.iCalUid}"/>
                                </td>
                                <td>
                                    <c:if test="${not isValid}">
                                        <button onclick="change(this);return false;">변경</button>
                                    </c:if>
                                </td>
                                <td>${item.keyword}</td>
                                <td>${item.category1}</td>
                                <td>${item.category2}</td>
                                <td>${item.category3}</td>
                                <td>${item.requester}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
        //$('.date-picker').datepicker();
        $('#fromDate').datetimepicker({
            language : 'ko', // 화면에 출력될 언어를 한국어로 설정한다.
            pickTime : false, // 사용자로부터 시간 선택을 허용하려면 true를 설정하거나 pickTime 옵션을 생략한다.
            defalutDate : new Date() // 기본값으로 오늘 날짜를 입력한다. 기본값을 해제하려면 defaultDate 옵션을 생략한다.
        });

    function change(obj) {
        var tr = obj.parentNode.parentNode;
        var data = {
              id: $(tr).find('[name=id]').val()
            , iCalUid: $(tr).find('[name=icalUid]').val()
            , summary: $(tr).find('[name=summary]').val()
        };
        console.log(data);

        $.post( "/myevent/google/updateSummary", data)
            .done(function( data ) {
                alert( "Data Loaded: " + data );
                console.log(data);
            });
    }
</script>

<c:import url="../../common/footer.jsp"/>