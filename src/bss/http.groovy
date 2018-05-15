package org.activiti.latera.bss.http

import org.activiti.engine.delegate.*
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import java.io.InputStreamReader
import org.apache.commons.io.IOUtils

public class HTTPRestProcessor {
  public RESTClient httpClient

  def HTTPRestProcessor(parameters) {
    this.httpClient = new RESTClient(parameters.baseUrl)
  }

  def protected log(String msg, String level = "info", DelegateExecution execution = null) {
    def logger = getLogger(execution)

    if (logger) {
      logger."${level}"(msg)
    }
  }

  def protected getLogger(DelegateExecution execution) {
    if (execution) {
      execution.getVariable("logger")
    }
  }

  def private responseBlock(failure=false, DelegateExecution execution=null) {
    {resp, reader ->
      def respStatusLine = resp.statusLine

      log("Response status: ${respStatusLine}", "info", execution)
      log("Response data: -----", "info", execution)
      if (reader) {
        if (reader instanceof InputStreamReader) {
          log(IOUtils.toString(reader), "info", execution)
        } else {
          log(reader.toString(), "info", execution)
        }
      }
      log("--------------------", "info", execution)

      if (failure) {
        throw new HttpResponseException(resp)
      } else {
        reader
      }
    }
  }

  def public sendRequest(params, String method) {
    def execution = params.execution
    params.remove('execution')

    if (!params.requestContentType) {
      params.requestContentType = ContentType.JSON
    }

    log("/ Sending HTTP ${method.toUpperCase()} request (${httpClient.defaultURI}${params.path})...", "info", params.execution)
    if (params.body) {
      log("Request data: ------", "info", execution)
      log(params.body.toString(), "info", execution)
      log("--------------------", "info", execution)
    }

    httpClient.handler.success = responseBlock(false, execution)
    httpClient.handler.failure = responseBlock(true, execution)

    def result = httpClient."${method}"(params)
    log("\\ HTTP request sent", "info", execution)
    result
  }
}
