<#import "parts/common.ftl" as c>

User editor
<form method="post" action="/user">



    <input type="text" value="${user.username}" name="userNAME">
    <#list roles as role>
        <div>
            <select><option value="${role}"></option> </select>
            <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
        </div>


    </#list>


    <!-- <select id="roles" name="roles">
    <#list roles as role>
        <option value="${role}" selected="selected">${role}</option>
    </#list>
    </select>-->
    <input type="hidden" value="${user.id}" name="userID">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit">save</button>
</form>

<@c.page>

</@c.page>