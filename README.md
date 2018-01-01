# netty-mq
基于netty的消息应用
这是以前的消息预研版本，生产版本的代码不公开,此版本未加入离线消息的逻辑，生产中使用的是mongodb以及ehcache来辅助实现，
预研不版本只支持文本消息，生产版本还支持二进制消息。

测试demo说明，本版本虽然基于springboot，但其实功能上未整合任何的spring功能（生产版本要整合）<br>

先执行com.wen.jun.mq.Application，
test包下面提供了两个测试的demo<br>
1、demo1<br>
    测试队列消息<br>
2、demo2<br>
    测试订阅消息<br>
    
