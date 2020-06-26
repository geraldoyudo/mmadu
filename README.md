# mmadu

Mmadu (pronounced um-a-du) is derived from an Igbo word meaning people.

Have you ever found yourself in a state of deja-vu when ever you write user management logic? Mmadu provides
a set of tools that ensures that you never have to repeat user management logic. It helps to manage users, takes
care of registration work flows and exposes APIs for authentication.

With Mmadu, you can have a dedicated system for managing users across all your applications.

Mmadu is a set of tools and services that helps developers manage their application users. Applications are constantly
being built on mobile, web and other devices and with these bring their own security challenges in authentication and
authorization.

## Architecture

Mmadu is based on microservice architecture and is built using the Spring Framework.

## Vision

To help developers (*of all walks of life*) eliminate user-management coding in applications forever.

## Current Features

1. Multi-tenancy using Domains
2. User Management
3. Role and Authority Management
4. Group Management
5. Authentication
6. Dynamic User Registration Forms
7. Oauth 2.0
8. Jwt Token Security

## Project Road Map

We aim to provide an array of services and libraries to enable developers pick and choose to implement
their desired management flows.

Below are some of the features in the pipeline:

1. User Account flows: Forgot Password, Reset Password e.t.c
2. Oauth 2.0 [Implicit](https://tools.ietf.org/html/rfc6749#section-1.3.2) and
[Resource owner Password Credentials](https://tools.ietf.org/html/rfc6749#section-1.3.3) grant types
(currently only Authorization Code, Client Credentials and Refresh Token are supported)
3. Oauth 2.0 [Proof Key for Code Exchange](https://tools.ietf.org/html/rfc7636)
4. Oauth 2.0 [Device Code Grant Type](https://tools.ietf.org/html/rfc8628#section-3.4)
5. Oauth 2.0 [Authorization Server Metadata](https://tools.ietf.org/html/rfc8414)
6. Oauth 2.0 [Dynamic Client Registration](https://tools.ietf.org/html/rfc7591)
7. Oauth 2.0 [Dynamic Client Registration Management](https://tools.ietf.org/html/rfc7592)
8. [Open ID Connect](https://https://openid.net/connect/)
9. [UMA 2.0](https://docs.kantarainitiative.org/uma/wg/rec-oauth-uma-grant-2.0.html)
10. ... and many more as they come

## Getting Started

Click [here to get started with mmadu](https://geraldoyudo.github.io/mmadu)

## Contributing to Mmadu

If you want to contribute to eliminate user management coding forever, [send a mail to Mmadu](mailto:mmadu.auth@gmail.com)