from geraldoyudo/mmadu-user-service:1.2.0-RELEASE

COPY application.yml ./

CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Xmx125m", "-jar","./mmadu-user-service-1.2.0-RELEASE.jar"]