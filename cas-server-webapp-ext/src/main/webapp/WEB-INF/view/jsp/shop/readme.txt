https://apereo.github.io/cas/4.2.x/installation/User-Interface-Customization.html
RegisteredServiceThemeBasedViewResolver做主题定义，为不同的应用定制不同的登录页
ServiceThemeResolver 主要是用于国际化css\js ,如cas-theme-default.properties,根据以下配置自定义为theme-tmall.properties

根据cas-management的theme的配置custom,那么cas-management会进入到自己定义的custom/casLoginView.jsp
登录界面,其他页面也类似
{
  @class: org.jasig.cas.services.RegexRegisteredService
  serviceId: https://server.fighting.com:8443/cas-management/.*
  name: Services Management Web Application
  theme: tmall
  id: 10000004
  description: Services Management Web Application
  evaluationOrder: 1
  logoutType: BACK_CHANNEL
}