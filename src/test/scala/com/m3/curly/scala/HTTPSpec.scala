package com.m3.curly.scala

import org.specs2.mutable.Specification
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{ Request => BaseRequest }
import javax.servlet.http._

class HTTPSpec extends Specification {

  "HTTP" should {

    // --------
    // GET

    "get" in {
      val server = new org.eclipse.jetty.server.Server(8077)
      try {
        server.setHandler(getHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.get("http://localhost:8077/?foo=bar")
        response.status must equalTo(200)
        response.asString.length must be_>(0)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "get with queryParams" in {
      val server = new org.eclipse.jetty.server.Server(8177)
      try {
        server.setHandler(getHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.get("http://localhost:8177/", "foo" -> "bar")
        response.status must equalTo(200)
        response.asString.length must be_>(0)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "get using queryParams method" in {
      val server = new org.eclipse.jetty.server.Server(8277)
      try {
        server.setHandler(getHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.get(Request("http://localhost:8277/").queryParams("foo" -> "bar"))
        response.status must equalTo(200)
        response.asString.length must be_>(0)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "get with charset" in {
      val server = new org.eclipse.jetty.server.Server(8377)
      try {
        server.setHandler(getHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.get("http://localhost:8377/?foo=bar", "UTF-8")
        response.status must equalTo(200)
        response.asString.length must be_>(0)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    // --------
    // POST

    "post with data string" in {
      val server = new org.eclipse.jetty.server.Server(8187)
      try {
        server.setHandler(postHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.post("http://localhost:8187/", "foo=bar")
        response.status must equalTo(200)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "post with Map" in {
      val server = new org.eclipse.jetty.server.Server(8287)
      try {
        server.setHandler(postHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.post("http://localhost:8287/", Map("foo" -> "bar"))
        response.status must equalTo(200)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "post with TextInput" in {
      val server = new _root_.server.PostFormdataServer
      try {
        new Thread(new Runnable() {
          def run() {
            server.start()
          }
        }).start()
        Thread.sleep(300L)

        val response = HTTP.post("http://localhost:8888/", FormData(name = "toResponse", text = TextInput("bar")))
        response.status must equalTo(200)
        response.asString must equalTo("bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }


    // --------
    // PUT

    "put with data string" in {
      val server = new org.eclipse.jetty.server.Server(8186)
      try {
        server.setHandler(putHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.put("http://localhost:8186/", "foo=bar")
        response.status must equalTo(200)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }

    "put with data Map" in {
      val server = new org.eclipse.jetty.server.Server(8286)
      try {
        server.setHandler(putHandler)
        new Thread(runnable(server)).start()
        Thread.sleep(300L)

        val response = HTTP.put("http://localhost:8286/", Map("foo" -> "bar"))
        response.status must equalTo(200)
        response.asString must equalTo("foo:bar")
      } finally {
        server.stop
        Thread.sleep(300L)
      }
    }
  }

  def runnable(server: Server) = {
    val _server = server
    new Runnable() {
      def run() {
        try {
          _server.start();
        } catch {
          case e => e.printStackTrace
        }
      }
    }
  }

  val getHandler = new AbstractHandler {
    def handle(target: String, baseReq: BaseRequest, req: HttpServletRequest, resp: HttpServletResponse) = {
      try {
        if (req.getMethod().equals("GET")) {
          val foo = req.getParameter("foo")
          val result = "foo:" + foo
          resp.setCharacterEncoding("UTF-8")
          resp.getWriter().print(result)
          baseReq.setHandled(true)
          resp.setStatus(HttpServletResponse.SC_OK)
        } else {
          resp.setStatus(HttpServletResponse.SC_FORBIDDEN)
        }
      } catch { case e => e.printStackTrace }
    }
  }

  val postHandler = new AbstractHandler {
    def handle(target: String, baseReq: BaseRequest, req: HttpServletRequest, resp: HttpServletResponse) = {
      try {
        if (req.getMethod().equals("POST")) {
          val foo = req.getParameter("foo")
          val result = "foo:" + foo
          resp.setCharacterEncoding("UTF-8")
          resp.getWriter().print(result)
          baseReq.setHandled(true)
          resp.setStatus(HttpServletResponse.SC_OK)
        } else {
          resp.setStatus(HttpServletResponse.SC_FORBIDDEN)
        }
      } catch { case e => e.printStackTrace }
    }
  }

  val putHandler = new AbstractHandler {
    def handle(target: String, baseReq: BaseRequest, req: HttpServletRequest, resp: HttpServletResponse) = {
      try {
        if (req.getMethod().equals("PUT")) {
          val foo = req.getParameter("foo")
          val result = "foo:" + foo
          resp.setCharacterEncoding("UTF-8")
          resp.getWriter().print(result)
          baseReq.setHandled(true)
          resp.setStatus(HttpServletResponse.SC_OK)
        } else {
          resp.setStatus(HttpServletResponse.SC_FORBIDDEN)
        }
      } catch { case e => e.printStackTrace }
    }
  }

}