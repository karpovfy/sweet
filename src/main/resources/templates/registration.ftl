<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
add new user
${message}

<@l.login "/registration" />

</@c.page>