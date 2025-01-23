# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.3-cds
#FROM findepi/graalvm:java17-native

LABEL maintainer="Lion Li"

RUN mkdir -p /kp-cloud/kp-protocol/logs \
    /kp-cloud/kp-protocol/temp \
    /kp-cloud/skywalking/agent

WORKDIR /kp-cloud/kp-protocol

ENV SERVER_PORT=8001 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS="-XX:+UseContainerSupport -XX:InitialRAMPercentage=10 -XX:MaxRAMPercentage=70 \
                                                                                             -XX:+HeapDumpOnOutOfMemoryError \
                                                                                             -XX:+UseTLAB -XX:+ResizeTLAB -XX:+PerfDisableSharedMem -XX:+UseCondCardMark \
                                                                                             -XX:+UseG1GC -XX:MaxGCPauseMillis=500 -XX:+UseStringDeduplication -XX:+ParallelRefProcEnabled -XX:MaxTenuringThreshold=10 \
                                                                                             -Xss512k -XX:G1ReservePercent=20 \
                                                                                             -XX:-OmitStackTraceInFastThrow"
EXPOSE ${SERVER_PORT}

ADD kp-protocol-server.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           #-Dskywalking.agent.service_name=ruoyi-system \
           #-javaagent:/ruoyi/skywalking/agent/skywalking-agent.jar \
           -XX:+HeapDumpOnOutOfMemoryError  ${JAVA_OPTS} \
           -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
           -jar app.jar --logging.level.root=DEBUG --logging.level.org.springframework=INFO
