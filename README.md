### From iteach/myzz

### 说明
1. com.swust.kelab.oauth包主要是用于向授权服务请求token，在用户登陆成功之后请求，UserController中202-209行
2. com.swust.kelab.httpClient包主要用于后台发送请求，例如向授权服务获取token，向自适应界面服务获取数据等
3. com.swust.kelab.adaptive_ui包主要用于自适应界面服务相关的逻辑处理
4. 自适应界面的请求Controller放在com.swust.kelab.controller包内
5. 请求的路径（授权服务路径、自适应界面服务路径）在resources目录下oauth.properties文件里面配置
6. 因为自适应界面的关系，可能没有修改所有的页面，导致有的页面不能使用，解决办法
	* a. 找到没有修改的页面，例如??.html
	* b. 新建一个html，任意取名，例如???.html
	* c. 把userCenter.html里面的所有内容复制到???.html里面
	* d. 把??.html里面有的而???.html里面没有的css、js引入语句复制到???.html里面
	* e. 把??.html里面<div class="row"></div>这个div里面所有的东西 替换 ???.html里面相应的地方
	* f. 改变???.html里面的面包屑导航路径
	* g. 删除??.html并改变???.html的名字为??.html