# Coding Challenges

## Build Your Own Web Server

<https://codingchallenges.fyi/challenges/challenge-webserver>

This challenge is to build your own basic web server.

At its core a web server is actually quite simple. It’s a server that listens for connections from clients and responds to them. The clients make those requests using a protocol known as HTTP (and expect responses in the same protocol, obviously).

## How to Run this project

`$ clj -M:main config.edn`

Change the `config.edn` file to configure the port and the base-dir for the http server.
