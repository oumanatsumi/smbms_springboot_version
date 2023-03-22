# SMBMS SpringBoot 版本


## 技术特点：使用Redis作为缓存

自定义注解RedisCahe。

不使用注解的情况：

![img.png](src/test/resources/img_0.png)

运行结果：

![img_1.png](src/test/resources/img_1.png)

使用注解的情况：

![img_2.png](src/test/resources/img_2.png)

运行结果：

![img.png](src/test/resources/img.png)

Redis中的情况：

![img_3.png](src/test/resources/img_3.png)

可以看到，成功存入。