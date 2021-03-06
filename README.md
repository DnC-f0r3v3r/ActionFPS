# ActionFPS

[![Build Status](https://travis-ci.org/ScalaWilliam/ActionFPS.svg)](https://travis-ci.org/ScalaWilliam/ActionFPS)
[![Join the chat at https://gitter.im/ScalaWilliam/ActionFPS](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ScalaWilliam/actionfps?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Workflow](https://badge.waffle.io/ScalaWilliam/actionfps.png?label=ready&title=Ready)](https://waffle.io/ScalaWilliam/actionfps)

* https://actionfps.com/

# Developing

Use IntelliJ Community Edition: https://www.jetbrains.com/idea/download/

Simply import the directory as an 'SBT' project

Install SBT: http://www.scala-sbt.org/download.html

```
sbt web/run
```

# Running tests

```
sbt clean test
```

# Dev endpoints

As defined in [web/app/controllers/Dev.scala#L27](https://github.com/ScalaWilliam/ActionFPS/blob/master/web/app/controllers/Dev.scala#L27).

So you can edit templates without having to have the true data.

* https://actionfps.com/dev/live-template/
* https://actionfps.com/dev/clanwars/
* https://actionfps.com/dev/sig.svg
* https://actionfps.com/dev/sig/
* https://actionfps.com/dev/player/

# Issue history

[![Throughput Graph](https://graphs.waffle.io/ScalaWilliam/actionfps/throughput.svg)](https://waffle.io/ScalaWilliam/actionfps/metrics)

# Technology Choices

* __Scala__ for data processing and Play framework: solid, stable toolkit for dealing with complex data.
* __jsoup__ for rendering templates: dynamic, works well with HTML5 and XML.

## DevOps
Continuous Deployment: master --> <https://git.watch/> --> build & restart. Simple monolithic deployment.
