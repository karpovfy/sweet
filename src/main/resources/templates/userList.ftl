<#import "parts/common.ftl" as c>

<@c.page>

List of users
<div class="table-responsive-md">
<table class="table table-striped table-bordered table-hover">
    <thead >
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Roles</th>
        <th scope="col">Email</th>
        <th scope="col">Operation</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td scope="row">${user.username}</td>
        <td><#list user.roles as role>${role}<#sep>, </#list></td>
        <td> ${user.email?if_exists}</td>
        <td><a href="/user/${user.id}">edit</a></td>
    </tr>
    </#list>
    </tbody>
</table>
</div>

</@c.page>