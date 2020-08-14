docker tag geraldoyudo/mmadu-identity:2.0.3-SNAPSHOT geraldoyudo/mmadu-identity:2.0.3-r$TRAVIS_BUILD_NUMBER
docker tag geraldoyudo/mmadu-registration:2.0.3-SNAPSHOT geraldoyudo/mmadu-registration:2.0.3-r$TRAVIS_BUILD_NUMBER
docker tag geraldoyudo/mmadu-user-service:2.0.3-SNAPSHOT geraldoyudo/mmadu-user-service:2.0.3-r$TRAVIS_BUILD_NUMBER
docker tag geraldoyudo/mmadu-notifications:2.0.3-SNAPSHOT geraldoyudo/mmadu-notifications:2.0.3-r$TRAVIS_BUILD_NUMBER
docker tag geraldoyudo/mmadu-otp:2.0.3-SNAPSHOT geraldoyudo/mmadu-otp:2.0.3-r$TRAVIS_BUILD_NUMBER

docker push geraldoyudo/mmadu-identity:2.0.3-r$TRAVIS_BUILD_NUMBER
docker push geraldoyudo/mmadu-registration:2.0.3-r$TRAVIS_BUILD_NUMBER
docker push geraldoyudo/mmadu-user-service:2.0.3-r$TRAVIS_BUILD_NUMBER
docker push geraldoyudo/mmadu-notifications:2.0.3-r$TRAVIS_BUILD_NUMBER
docker push geraldoyudo/mmadu-otp:2.0.3-r$TRAVIS_BUILD_NUMBER