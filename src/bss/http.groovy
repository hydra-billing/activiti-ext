package org.activiti.latera.bss.http

import org.activiti.engine.delegate.*
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import java.io.InputStreamReader
import org.apache.commons.io.IOUtils

public class HTTPRestProcessor {
  DelegateExecution execution
  public RESTClient httpClient
 
  def HTTPRestProcessor(parameters) {
    this.execution = parameters.execution
    this.httpClient = new RESTClient(parameters.baseUrl)
  }

  def private log(String msg, String level = "info") {
    def logger = execution.getVariable('logger')

    if (logger) {
      logger."${level}"(msg)
    }
  }

  def private responseBlock(failure=false) {
    {resp, reader ->
      def respStatusLine = resp.statusLine

      log "Response status: ${respStatusLine}"
      log "Response data: -----"
      if (reader) {
        if (reader instanceof InputStreamReader) {
          log IOUtils.toString(reader); 
        } else {
          log reader.toString()
        }
      }
      log "--------------------"

      if (failure) {
        throw new HttpResponseException(resp)
      } else {
        reader
      }
    }
  }
  
  def public sendRequest(params, String method) {
    if (!params.requestContentType) {
      params.requestContentType = ContentType.JSON
    }
    
    log "/ Sending HTTP ${method.toUpperCase()} request (${httpClient.defaultURI}${params.path})..."
    if (params.body) {
      log "Request data: ------"
      log params.body.toString()
      log "--------------------"
    }

    httpClient.handler.success = responseBlock()
    httpClient.handler.failure = responseBlock(true)
    
    def result = httpClient."${method}"(params)
    log "\\ HTTP request sent"
    result
  }
}
