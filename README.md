### From iteach/myzz

###说明
1. com.swust.kelab.oauth包主要是用于向授权服务请求token，在用户登陆成功之后请求，UserController中202-209行
2. com.swust.kelab.httpClient包主要用于后台发送请求，例如向授权服务获取token，向自适应界面服务获取数据等
3. com.swust.kelab.adaptive_ui包主要用于自适应界面服务相关的逻辑处理
4. 自适应界面的请求Controller放在com.swust.kelab.controller包内
5. 请求的路径（授权服务路径、自适应界面服务路径）在resources目录下oauth.properties文件里面配置
