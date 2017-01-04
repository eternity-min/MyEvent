<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../../common/header.jsp"/>

<style>
    tr.event_not_valid textarea {color: red;}
</style>

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
                        <button type="submit" >검색</button>
                        <button type="button" onclick="document.dataForm.submit(); return false;">저장</button>
                    </form>
                </p>
            </div>
            <div class="content table-responsive table-full-width">
                <form name="dataForm" action="/calendar/event/local/save" method="post">
                    <input type="hidden" name="searchStart" value="${param.searchStart}" readonly/>
                    <input type="hidden" name="searchEnd" value="${param.searchEnd}" readonly/>

                    <select id="isValidFilter">
                        <option>전체</option>
                        <option>O</option>
                        <option>X</option>
                    </select>
                    <table style="font-size:0.8em;" class="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>date</th>
                            <th>summary</th>
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
                            <c:set var="startDate"><fmt:formatDate value="${item.start}" pattern="MM-dd"/></c:set>
                            <c:set var="endDate"><fmt:formatDate value="${item.end}" pattern="MM-dd"/></c:set>
                            <tr class="event_<c:if test="${not isValid}">not_</c:if>valid">
                                <td style="vertical-align: top;">
                                    ${startDate}
                                    <c:if test="${startDate ne endDate}">
                                        <br/>-  ${endDate}
                                    </c:if>
                                </td>
                                <td style="vertical-align: top;">
                                    <fmt:formatDate value="${item.start}" pattern="HH:mm"/> -
                                    <fmt:formatDate value="${item.end}" pattern="HH:mm"/>
                                    <br/>

                                    <textarea name="summary" style="width:100%;">${item.summary}</textarea>
                                    <span name="keyword">${item.keyword}</span>
                                    <br/>
                                    <button type="button" name="btnUpdateSummary" class="btn-block pe-7s-cloud-upload "></button>

                                    <c:if test="${not empty item.location}">${item.location}</c:if>

                                    <input type="hidden" name="id" value="${item.id}"/>
                                    <input type="hidden" name="icalUid" value="${item.iCalUid}"/>
                                </td>
                                <td name="category">${item.category1} - ${item.category2} - ${item.category3}</td>
                                <td name="subsystem">${item.subsystem}</td>
                                <td name="srId">${item.srId}</td>
                                <td name="requester">${item.requester}</td>
                                <td><textarea name="content" style="width:100%;">${item.content}</textarea></td>
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
    $(document).ready(function() {
        $('[name=btnUpdateSummary]').click(function() {
            $tr = $(this).parents('tr');

            var data = {
                id: $tr.find('[name=id]').val()
                , iCalUid: $tr.find('[name=icalUid]').val()
                , summary: $tr.find('[name=summary]').val()
            };
            console.log(data);

            $.post( "/calendar/event/google/updateSummary", data)
                    .done(function( data ) {
                        alert( "Success!!");
                        $tr.find('[name=keyword]').text(data.keyword);
                        $tr.find('[name=category]').text(data.category1 + '-' + data.category2 + '-' + data.category3);
                        $tr.find('[name=subsystem]').text(data.subsystem);
                        $tr.find('[name=srId]').text(data.srId);
                        $tr.find('[name=requester]').text(data.requester);
                        $tr.find('[name=summary]').text(data.content);
                    });
        });

        $('#isValidFilter').change(function() {
            $('tr.event_valid').show();
            $('tr.event_not_valid').show();

            if($(this).val() == "O") {
                $('tr.event_not_valid').hide();
            }
            else if($(this).val() == "X") {
                $('tr.event_valid').hide();
            }
        });
    });

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
</script>

<c:import url="../../common/footer.jsp"/>