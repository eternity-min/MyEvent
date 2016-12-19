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
                    <form name="searchForm">
                        기간:
                        <input type="date" name="searchStart" class="date-picker" value="${param.searchStart}"/> ~
                        <input type="date" name="searchEnd" class="date-picker" value="${param.searchEnd}"/>
                        <button >검색</button>
                        <button onclick="document.dataForm.submit(); return false;">저장</button>
                    </form>
                </p>
            </div>
            <div class="content table-responsive table-full-width">
                <form name="dataForm" action="/myevent/local/save" method="post">
                    <input type="hidden" name="searchStart" value="${param.searchStart}" readonly/>
                    <input type="hidden" name="searchEnd" value="${param.searchEnd}" readonly/>
                    <table class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>
                                <select id="isValidFilter" onchange="isValidFilterOnChange();">
                                    <option></option>
                                    <option>O</option>
                                    <option>X</option>
                                </select>
                            </th>
                            <th>summary</th>
                            <th>keyword</th>
                            <th>category</th>
                            <th>subsystem</th>
                            <th>srId</th>
                            <th>requester</th>
                            <th>content</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${myEvents}" var="item" varStatus="status">
                            <c:set var="isValid" value="${not empty item.category1 and not empty item.category2 and not empty item.category3}"/>
                            <tr>
                                <td>
                                    <input type="checkbox" name="isValid" onclick="return false;" <c:if test="${isValid}">checked</c:if>/>
                                    <button onclick="updateSummary(this);return false;">변경</button>
                                </td>
                                <td>
                                    <fmt:formatDate value="${item.start}" pattern="MM-dd HH:mm"/> -
                                    <fmt:formatDate value="${item.end}" pattern="MM-dd HH:mm"/>
                                    : ${item.location} <br/>
                                    <textarea name="summary">${item.summary}</textarea>
                                    <input type="hidden" name="id" value="${item.id}"/>
                                    <input type="hidden" name="icalUid" value="${item.iCalUid}"/>
                                </td>
                                <td>${item.keyword}</td>
                                <td>${item.category1} - ${item.category2} - ${item.category3}</td>
                                <td>${item.subsystem}</td>
                                <td>${item.srId}</td>
                                <td>${item.requester}</td>
                                <td><textarea name="summary">${item.content}</textarea></td>

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
    function isValidFilterOnChange() {
        var value = $('#isValidFilter').val();

        $('[name=isValid]').each(function() {
            var tr = $(this).get(0).parentNode.parentNode;

            $(tr).show();
            if(value == "O") {
                if(!$(this).prop('checked')) {
                    $(tr).hide();
                }
            }
            else if(value == "X") {
                if($(this).prop('checked')) {
                    $(tr).hide();
                }
            }
        });
    }

    function search() {

    }

    function updateSummary(obj) {
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