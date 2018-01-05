# Wickomp

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.devocative/wickomp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.devocative/wickomp)

The `Wickomp` project is an extension to Apache Wicket with collection of class as follows:
- **Form components** like `WDateInput`, `WBooleanInput`, `WCodeInput`, `WSelectionInput`, ...
- **Grid components** based on [EasyUI](http://www.jeasyui.com) which are `WDataGrid` and `WTreeGrid`
- **HTML components** like `WAjaxLink`, `WWizardPanel`, ...
- **An async mechanism** which is some wrapper classes around `WebSocket`
- **An HTTP filter** to handle `BASIC` & `DIGEST` authentications
-Some utility classes

The components hierarchy is depicted in the following picture:

![Wickomp Hierarchy](/src/main/uml/Class_Diagram__wickomp__Components.png)

The wrapper for async, developed around Wicket WebSocket:
![Wickomp Async](/src/main/uml/Class_Diagram__wickomp__Async.png)

And finally, the class diagram for HTTP:
![Wickomp Http](/src/main/uml/Class_Diagram__wickomp__HTTP.png)

In the test directory, there is a test application which tries to show applications of developed components.
The test application can be run with `Jetty Maven Plugin`.

**TIP** Classes name convention: initial character maps it to a category:
- W -> Components
- O -> Options for a component
- R -> Result of a request as callback on a component


