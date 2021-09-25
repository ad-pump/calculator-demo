** Adding dependency to external module **
```
maven {
    url 'http://68.183.15.160:8081/nexus/content/repositories/adpump'
}

dependencies {
     implementation 'com.adpump:bidmachine:0.2'
}
```

** publish to nexus **

```
Goto adpump folder
../gradlew clean build
../gradlew upload

```