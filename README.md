##使用
###1.maven引入pom，spring boot启动即可,注解
@Autowired
TaskFacade taskFacade;
###2.使用
| 目标对象              | 优点                                   | 缺陷                           |
| --------------------- | -------------------------------------- | ------------------------------ |
| targetObject          | 直接传入对象即可                       | 对象中所有类都需要实现序列化   |
| targetClass - spring  | 指定bean的name即可，便于序列化写文件   | 依赖spring                     |
| targetClazz  - invoke | 指定packag.class即可，便于序列化写文件 | 通过反射出类，需要给出构造变量 |
